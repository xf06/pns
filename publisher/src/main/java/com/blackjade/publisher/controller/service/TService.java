package com.blackjade.publisher.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.blackjade.publisher.apis.CCancel;
import com.blackjade.publisher.apis.CDCancel;
import com.blackjade.publisher.apis.CDeal;
import com.blackjade.publisher.apis.CPCancel;
import com.blackjade.publisher.apis.CPCancelAns;
import com.blackjade.publisher.apis.CPaid;
import com.blackjade.publisher.apis.CPayConfirm;
import com.blackjade.publisher.apis.ComStatus;
import com.blackjade.publisher.apis.ComStatus.CancelStatus;
import com.blackjade.publisher.apis.ComStatus.DCancelStatus;
import com.blackjade.publisher.apis.ComStatus.DealStatus;
import com.blackjade.publisher.apis.ComStatus.OrderStatus;
import com.blackjade.publisher.apis.ComStatus.PCancelStatus;
import com.blackjade.publisher.apis.ComStatus.PaidStatus;
import com.blackjade.publisher.apis.ComStatus.PayConfirmStatus;
import com.blackjade.publisher.apis.ComStatus.PnSStatus;
import com.blackjade.publisher.dao.OrderDao;
import com.blackjade.publisher.dao.PnSDao;
import com.blackjade.publisher.domain.OrderRow;
import com.blackjade.publisher.domain.PnSRow;
import com.blackjade.publisher.exception.CapiException;

@Transactional
@Component
public class TService {

	@Autowired
	private OrderDao ord;

	@Autowired
	private PnSDao pns;

	// # deal
	public DealStatus updatePnS(final CDeal deal) {

		DealStatus st = ComStatus.DealStatus.SUCCESS;

		// ## select
		PnSRow pnsrow = null;
		try {
			pnsrow = this.pns.selectPnSRowOid(deal.getPnsoid().toString());
		} finally {
			// if no row found
			if (pnsrow == null)
				return ComStatus.DealStatus.PNS_MISS_MATCH;
		}

		// check input
		if (!pnsrow.getOid().equals(deal.getPnsoid().toString()))
			return ComStatus.DealStatus.PNS_MISS_MATCH;
		if (pnsrow.getPnsid() != deal.getPnsid())
			return ComStatus.DealStatus.PNS_MISS_MATCH;
		if (pnsrow.getPnsgid() != deal.getPnsgid())
			return ComStatus.DealStatus.PNS_MISS_MATCH;
		if (pnsrow.getPoid() != deal.getPoid())
			return ComStatus.DealStatus.PNS_MISS_MATCH;
		if (pnsrow.getSide() == deal.getSide()) // B<->S //S<->B // side checking //need some work here
			return ComStatus.DealStatus.PNS_MISS_MATCH;

		// check status
		ComStatus.PnSStatus ps = ComStatus.PnSStatus.UNKNOWN; // this is the publish status

		try {
			ps = ComStatus.PnSStatus.valueOf(pnsrow.getStatus());
		} catch (Exception e) {
			return ComStatus.DealStatus.PNS_STATUS_MESS;
		}

		// if final status
		if (ps == ComStatus.PnSStatus.FULL_TRADED)
			return ComStatus.DealStatus.PNS_STATUS_FINAL;
		if (ps == ComStatus.PnSStatus.CANCELLED)
			return ComStatus.DealStatus.PNS_STATUS_FINAL;
		if (ps == ComStatus.PnSStatus.HALF_CANCELLED)
			return ComStatus.DealStatus.PNS_STATUS_FINAL;
		// if locked status
		if (ps == ComStatus.PnSStatus.FULL_LOCKED)
			return ComStatus.DealStatus.PNS_STATUS_LOCKED;
		// if still unknown
		if (ps == ComStatus.PnSStatus.UNKNOWN)
			return ComStatus.DealStatus.PNS_STATUS_MESS;

		// *******************************************************
		// price changing check to be implemented in the future
		// *******************************************************

		// check data corruption
		long quant = pnsrow.getQuant();
		long margin = pnsrow.getMargin();
		long traded = pnsrow.getTraded();
		long net = pnsrow.getNet();
		String oid = pnsrow.getOid();

		if (quant != margin + traded + net)// can should be involved as well
			return ComStatus.DealStatus.PNS_DATA_MESS;// should send alarm to monitor as well

		// pns status are not final {#PUBLISHED, #HALF_TRADED}
		if (ps == ComStatus.PnSStatus.PUBLISHED) {

			// check status
			if (margin != 0)
				return ComStatus.DealStatus.PNS_DATA_MESS;

			if (traded != 0)
				return ComStatus.DealStatus.PNS_DATA_MESS;

			if (quant != net)
				return ComStatus.DealStatus.PNS_DATA_MESS;

			// dealing logic
			long tradeamount = deal.getQuant();

			if (net < tradeamount)
				return ComStatus.DealStatus.PNS_NET_NOT_ENOUGH;

			net -= tradeamount;
			margin += tradeamount;

			// update PNS and return
			if (((margin + traded) < quant) && (net != 0)) {
				int retcode = this.pns.updatePnSDeal(oid, margin, net, ComStatus.PnSStatus.HALF_TRADED.toString());
				if (retcode != 0)
					return ComStatus.DealStatus.SUCCESS;
				else
					return ComStatus.DealStatus.DATABASE_ERR;
			}

			if (((margin + traded) == quant) && (net == 0)) {
				int retcode = this.pns.updatePnSDeal(oid, margin, net, ComStatus.PnSStatus.FULL_LOCKED.toString());
				if (retcode != 0)
					return ComStatus.DealStatus.SUCCESS;
				else
					return ComStatus.DealStatus.DATABASE_ERR;
			}

			return ComStatus.DealStatus.UNKNOWN;
		}

		// ### {HALF_TRADED}
		if (ps == ComStatus.PnSStatus.HALF_TRADED) {
			// check status

			long tradeamount = deal.getQuant();
			if (net < tradeamount)
				return ComStatus.DealStatus.PNS_NET_NOT_ENOUGH;

			net -= tradeamount;
			margin += tradeamount;

			if (((margin + traded) < quant) && (net != 0)) {
				int retcode = this.pns.updatePnSDeal(oid, margin, net, ComStatus.PnSStatus.HALF_TRADED.toString());
				if (retcode != 0)
					return ComStatus.DealStatus.SUCCESS;
				else
					return ComStatus.DealStatus.DATABASE_ERR;
			}

			if (((margin + traded) == quant) && (net == 0)) {
				int retcode = this.pns.updatePnSDeal(oid, margin, net, ComStatus.PnSStatus.FULL_LOCKED.toString());
				if (retcode != 0)
					return ComStatus.DealStatus.SUCCESS;
				else
					return ComStatus.DealStatus.DATABASE_ERR;
			}

			return ComStatus.DealStatus.UNKNOWN;
		}

		return ComStatus.DealStatus.UNKNOWN;
	}

	// # paid
	public PaidStatus updateOrd(final CPaid paid) {
		// select the deal order check if it matches
		// if so, update order
		// if not return error
		// no need to update PnS for this

		OrderRow ordrow = null;
		try {
			ordrow = this.ord.selectOrder(paid.getOid().toString());// select for update
		} finally {
			if (ordrow == null)
				return ComStatus.PaidStatus.ORD_MISS_MATCH;
		}

		// check input
		if (!ordrow.getOid().equals(paid.getOid().toString()))
			return ComStatus.PaidStatus.ORD_MISS_MATCH;
		if (!ordrow.getPnsoid().equals(paid.getPnsoid().toString()))
			return ComStatus.PaidStatus.ORD_MISS_MATCH;
		if (ordrow.getPnsid() != paid.getPnsid())
			return ComStatus.PaidStatus.ORD_MISS_MATCH;
		if (ordrow.getPnsgid() != paid.getPnsgid())
			return ComStatus.PaidStatus.ORD_MISS_MATCH;
		if (ordrow.getPoid() != paid.getPoid())
			return ComStatus.PaidStatus.ORD_MISS_MATCH;
		if (ordrow.getSide() != paid.getSide()) // B<->S //S<->B // side checking //need some work here
			return ComStatus.PaidStatus.ORD_MISS_MATCH;

		// check data corruption
		if (ordrow.getPrice() != paid.getPrice())
			return ComStatus.PaidStatus.ORD_MISS_MATCH;
		if (ordrow.getQuant() != paid.getQuant())
			return ComStatus.PaidStatus.ORD_MISS_MATCH;

		// check status
		// DEALING PAID PAYCONFIRM CANCELLED DONE REJECT_LOCK REJECT_DONE ERROR

		int retcode = 0;
		try {
			// DEALING
			if (ComStatus.OrderStatus.valueOf(ordrow.getStatus()) == ComStatus.OrderStatus.DEALING) {
				// update status to PAID->DEALING
				retcode = this.ord.updateOrderStatus(paid.getOid().toString(), ComStatus.OrderStatus.PAID.toString());
				if (retcode == 0)
					return ComStatus.PaidStatus.DATABASE_ERR;
				else
					return ComStatus.PaidStatus.SUCCESS;
			}

			// PAID
			if (ComStatus.OrderStatus.valueOf(ordrow.getStatus()) == ComStatus.OrderStatus.PAID)
				return ComStatus.PaidStatus.ORD_STATUS_PAID_ALREADY;

			// PAYCONFIRM
			if (ComStatus.OrderStatus.valueOf(ordrow.getStatus()) == ComStatus.OrderStatus.PAYCONFIRM)
				return ComStatus.PaidStatus.ORD_STATUS_PAYCONFIRM;

			// CANCELLED
			if (ComStatus.OrderStatus.valueOf(ordrow.getStatus()) == ComStatus.OrderStatus.CANCELLED)
				return ComStatus.PaidStatus.ORD_STATUS_FINAL;

			// DONE
			if (ComStatus.OrderStatus.valueOf(ordrow.getStatus()) == ComStatus.OrderStatus.DONE)
				return ComStatus.PaidStatus.ORD_STATUS_FINAL;

			// REJECT_LOCK REJECT_DONE
			if (ComStatus.OrderStatus.valueOf(ordrow.getStatus()) == ComStatus.OrderStatus.REJECT_DONE)
				return ComStatus.PaidStatus.ORD_STATUS_FINAL;
			if (ComStatus.OrderStatus.valueOf(ordrow.getStatus()) == ComStatus.OrderStatus.REJECT_LOCK)
				return ComStatus.PaidStatus.ORD_STATUS_FINAL;

		} catch (Exception e) {
			// print logs
		} finally {
			// logs
			// return ComStatus.PaidStatus.DATABASE_ERR;
		}

		return ComStatus.PaidStatus.DATABASE_ERR;
	}

	// # payconfirm
	public PayConfirmStatus updateOrdPnS(final CPayConfirm paycon) throws Exception {

		// update order status from Paid->PayConfirmed
		// 1 select if the order is what it is
		// 2 check status

		// update PnS from margin->traded
		PnSRow pnsrow = this.pns.selectPnSRowOid(paycon.getPnsoid().toString());
		if (pnsrow == null)
			return ComStatus.PayConfirmStatus.PC_DB_MISS_MATCH;

		// check PnS values
		long quant = pnsrow.getQuant();
		long margin = pnsrow.getMargin();
		long traded = pnsrow.getTraded();
		long net = pnsrow.getNet();
		long can = pnsrow.getCan();

		String status = pnsrow.getStatus();

		// check margin, traded
		if (quant != (margin + traded + net + can))
			return ComStatus.PayConfirmStatus.PC_DB_CORRUPT;

		// check PnS status
		PnSStatus st = ComStatus.PnSStatus.UNKNOWN;
		try {
			st = ComStatus.PnSStatus.valueOf(status);
		} catch (Exception e) {

			return ComStatus.PayConfirmStatus.PC_DB_CORRUPT;
		}

		if (st == ComStatus.PnSStatus.HALF_CANCELLED)
			return ComStatus.PayConfirmStatus.STATUS_FINAL;
		if (st == ComStatus.PnSStatus.CANCELLED)
			return ComStatus.PayConfirmStatus.STATUS_FINAL;
		if (st == ComStatus.PnSStatus.FULL_TRADED)
			return ComStatus.PayConfirmStatus.STATUS_FINAL;

		// PUBLISH, dealing operation missing
		if (st == ComStatus.PnSStatus.PUBLISHED)
			return ComStatus.PayConfirmStatus.STATUS_MISS;

		// update ord status

		OrderRow ordrow = null;
		try {
			ordrow = this.ord.selectOrder(paycon.getOid().toString());
		} catch (Exception e) {
			return ComStatus.PayConfirmStatus.DB_ORD_MISS;
		}

		if (ordrow == null)
			return ComStatus.PayConfirmStatus.DB_ORD_MISS;

		// check ordrow
		if (!ordrow.getPnsoid().equals(paycon.getPnsoid().toString()))
			return ComStatus.PayConfirmStatus.DB_ORD_STATUS;

		if (ordrow.getSide() != paycon.getSide())
			return ComStatus.PayConfirmStatus.DB_ORD_STATUS;

		if (ordrow.getCid() != paycon.getCid())
			return ComStatus.PayConfirmStatus.DB_ORD_STATUS;

		if (ordrow.getPnsgid() != paycon.getPnsgid())
			return ComStatus.PayConfirmStatus.DB_ORD_STATUS;

		if (ordrow.getPnsid() != paycon.getPnsid())
			return ComStatus.PayConfirmStatus.DB_ORD_STATUS;

		if (ordrow.getPoid() != paycon.getPoid())
			return ComStatus.PayConfirmStatus.DB_ORD_STATUS;

		if (ordrow.getPrice() != paycon.getPrice())
			return ComStatus.PayConfirmStatus.DB_ORD_STATUS;

		if (ordrow.getQuant() != paycon.getQuant())
			return ComStatus.PayConfirmStatus.DB_ORD_STATUS;

		if (ordrow.getSide() != paycon.getSide())
			return ComStatus.PayConfirmStatus.DB_ORD_STATUS;

		// check ord status

		OrderStatus ost = ComStatus.OrderStatus.UNKNOWN;
		try {
			ost = ComStatus.OrderStatus.valueOf(ordrow.getStatus());
		} catch (Exception e) {
			return ComStatus.PayConfirmStatus.DB_ORD_STATUS;
		}

		switch (ost) {
		case DEALING:
			return ComStatus.PayConfirmStatus.DB_ORD_STATUS;
		case PAID:
			;
			break;
		case PAYCONFIRM:
			return ComStatus.PayConfirmStatus.ORD_FINAL;
		case CANCELLED:
			return ComStatus.PayConfirmStatus.ORD_FINAL;
		case DONE:
			return ComStatus.PayConfirmStatus.ORD_FINAL;
		case REJECT_LOCK:
			return ComStatus.PayConfirmStatus.ORD_FINAL;
		case REJECT_DONE:
			return ComStatus.PayConfirmStatus.ORD_FINAL;
		default:
			return ComStatus.PayConfirmStatus.ORD_ERR;
		}

		// check margin, and update margin
		if (margin < ordrow.getQuant())
			return ComStatus.PayConfirmStatus.DB_PNS_ERR;

		margin = margin - ordrow.getQuant();
		traded = traded + ordrow.getQuant();

		int pnsret = 0;
		int ordret = 0;

		if (st == ComStatus.PnSStatus.HALF_TRADED) {
			if (net <= 0)
				return ComStatus.PayConfirmStatus.DB_PNS_ERR;
			try {
				pnsret = this.pns.updatePnSPayConfirm(ordrow.getPnsoid(), margin, traded,
						ComStatus.PnSStatus.HALF_TRADED.toString());
				if (pnsret == 0)
					return ComStatus.PayConfirmStatus.DB_PNS_ERR;

				ordret = this.ord.updateOrderStatus(ordrow.getOid(), ComStatus.OrderStatus.PAYCONFIRM.toString());
				if (ordret == 0)
					throw new CapiException(ComStatus.PayConfirmStatus.DB_PNS_ERR.toString());
			} catch (CapiException e) {
				throw new CapiException(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				throw new CapiException(ComStatus.PayConfirmStatus.PC_DB_CORRUPT.toString());
			}

			int retcode = 0;
			try {
				retcode = this.ord.updateOrderStatus(paycon.getOid().toString(),
						ComStatus.OrderStatus.PAYCONFIRM.toString());
			} catch (Exception e) {
				// logging data
				e.printStackTrace();
				throw new CapiException(ComStatus.PayConfirmStatus.PC_DATABASE_ERR.toString());

			}

			if (retcode == 0) {
				return ComStatus.PayConfirmStatus.PC_DATABASE_ERR;
			}

			return ComStatus.PayConfirmStatus.SUCCESS;
		}

		if (st == ComStatus.PnSStatus.FULL_LOCKED) {
			if (net != 0)
				return ComStatus.PayConfirmStatus.DB_PNS_ERR;

			try {
				PnSStatus tempst = ComStatus.PnSStatus.UNKNOWN;
				if (margin != 0)
					tempst = ComStatus.PnSStatus.FULL_LOCKED;
				else
					tempst = ComStatus.PnSStatus.FULL_TRADED;

				pnsret = this.pns.updatePnSPayConfirm(ordrow.getPnsoid(), margin, traded, tempst.toString());
				if (pnsret == 0)
					return ComStatus.PayConfirmStatus.DB_PNS_ERR;

				ordret = this.ord.updateOrderStatus(ordrow.getOid(), ComStatus.OrderStatus.PAYCONFIRM.toString());
				if (ordret == 0)
					throw new CapiException(ComStatus.PayConfirmStatus.DB_PNS_ERR.toString());
			} catch (CapiException e) {
				throw new CapiException(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				throw new CapiException(ComStatus.PayConfirmStatus.PC_DB_CORRUPT.toString());
			}

			int retcode = 0;
			try {
				retcode = this.ord.updateOrderStatus(paycon.getOid().toString(),
						ComStatus.OrderStatus.PAYCONFIRM.toString());
			} catch (Exception e) {
				// logging data
			} finally {
				if (retcode == 0) {
					return ComStatus.PayConfirmStatus.PC_DATABASE_ERR;
				}
			}
			return ComStatus.PayConfirmStatus.SUCCESS;
		}

		return ComStatus.PayConfirmStatus.UNKNOWN;
	}

	// # cancel
	public CancelStatus updateOrdPnS(final CCancel can, Integer amnt) throws CapiException, Exception {

		int ordret = 0;
		int pnsret = 0;

		// # DEALING
		if (can.getType() == 'D') {

			// update order status DEALING -> CANCELLED

			OrderRow ordrow = null;
			try {
				ordrow = this.ord.selectOrder(can.getOid().toString());
			} catch (Exception e) {
				return ComStatus.CancelStatus.DB_ORD_MISS;
			}

			if (ordrow == null)
				return ComStatus.CancelStatus.DB_ORD_MISS;

			// check if ordrow match can

			if (ordrow.getPnsgid() != can.getPnsgid())
				return ComStatus.CancelStatus.DB_ORD_MISS;

			if (ordrow.getPnsid() != can.getPnsid())
				return ComStatus.CancelStatus.DB_ORD_MISS;

			if (!ordrow.getPnsoid().equals(can.getPnsoid().toString()))
				return ComStatus.CancelStatus.DB_ORD_MISS;

			if (ordrow.getSide() != can.getSide())
				return ComStatus.CancelStatus.DB_ORD_MISS;

			if (ordrow.getPrice() != can.getPrice())
				return ComStatus.CancelStatus.DB_ORD_MISS;

			if (ordrow.getQuant() != can.getQuant())
				return ComStatus.CancelStatus.DB_ORD_MISS;

			try {
				if (ComStatus.OrderStatus.DEALING != ComStatus.OrderStatus.valueOf(ordrow.getStatus()))
					return ComStatus.CancelStatus.ORD_STATUS_FINAL;
			} catch (Exception e) {
				return ComStatus.CancelStatus.DB_ORD_MESS;
			}

			// select pns and checks
			PnSRow pnsrow = null;
			try {
				pnsrow = this.pns.selectPnSRowOid(can.getPnsoid().toString());// for update
			} catch (Exception e) {
				return ComStatus.CancelStatus.DB_PNS_MISS;
			}

			if (pnsrow == null)
				return ComStatus.CancelStatus.DB_PNS_MISS;

			// check pns status

			PnSStatus pnsst = null;
			try {
				pnsst = ComStatus.PnSStatus.valueOf(pnsrow.getStatus());
			} catch (Exception e) {
				return ComStatus.CancelStatus.DB_PNS_MESS;
			}
			if (pnsst == null)
				return ComStatus.CancelStatus.DB_PNS_MESS;

			if ((pnsst != ComStatus.PnSStatus.HALF_TRADED) && (pnsst != ComStatus.PnSStatus.FULL_LOCKED))
				return ComStatus.CancelStatus.DB_PNS_STATUS;

			long quant = pnsrow.getQuant();
			long margin = pnsrow.getMargin();
			long traded = pnsrow.getTraded();
			long net = pnsrow.getNet();

			// long min = pnsrow.getMin(); // those are in RMB
			// long max = pnsrow.getMax(); // those are in RMB

			// check data
			if (quant != (margin + net + traded))
				return ComStatus.CancelStatus.DB_PNS_MESS;

			if (margin < can.getQuant())
				return ComStatus.CancelStatus.DB_PNS_MESS;

			margin -= can.getQuant();
			net += can.getQuant();

			// update order status DEALING -> CANCELLED
			try {
				ordret = this.ord.updateOrderStatus(can.getOid().toString(),
						ComStatus.OrderStatus.CANCELLED.toString());
				if (ordret == 0)
					return ComStatus.CancelStatus.ORD_UPDATE_FAILED;
			} catch (Exception e) {
				return ComStatus.CancelStatus.ORD_UPDATE_FAILED;
			}

			// update pns status FULL_LOCKED/HALF_TRADED -> HALF_TRADED
			try {
				pnsret = this.pns.updatePnSCancelDeal(can.getPnsoid().toString(), margin, net,
						ComStatus.PnSStatus.HALF_TRADED.toString());
				if (pnsret == 0)
					throw new CapiException(ComStatus.CancelStatus.PNS_UPDATE_FAILED.toString());

				amnt = can.getQuant();
				return ComStatus.CancelStatus.SUCCESS;
			} catch (CapiException e) {
				throw new CapiException(e.getMessage());
			} catch (Exception e) {
				// save into logs
				e.printStackTrace();
				throw new Exception(ComStatus.CancelStatus.PNS_UPDATE_FAILED.toString());
			}

			/*
			 * 
			 * PnS: PUBLISHED, //0 HALF_TRADED, //0 //HALF_LOCKED, //0 FULL_LOCKED, //0
			 * FULL_TRADED, //final CANCELLED, //final HALF_CANCELLED, //final UNKNOWN
			 * //final
			 * 
			 * Order: DEALING, PAID, PAYCONFIRM, CANCELLED, DONE, REJECT_LOCK, REJECT_DONE,
			 * ERROR, UNKNOWN
			 * 
			 */
			// --------- //
			// --------- //

		}

		// # PUBLISH
		if (can.getType() == 'P') {

			// update
			PnSRow pnsrow = null;
			try {
				pnsrow = this.pns.selectPnSRowOid(can.getPnsoid().toString());
				if (pnsrow == null)
					return ComStatus.CancelStatus.DB_PNS_MISS;
			} catch (Exception e) {
				e.printStackTrace();
				return ComStatus.CancelStatus.DB_PNS_MISS;
			}

			// check pns status
			PnSStatus pnsst = null;
			try {
				pnsst = ComStatus.PnSStatus.valueOf(pnsrow.getStatus());
				if (pnsst == null)
					return ComStatus.CancelStatus.DB_PNS_STATUS;
			} catch (Exception e) {
				return ComStatus.CancelStatus.DB_PNS_STATUS;
			}

			if ((pnsst != ComStatus.PnSStatus.PUBLISHED) && (pnsst != ComStatus.PnSStatus.HALF_TRADED))
				return ComStatus.CancelStatus.PNS_STATUS_FINAL;

			//
			long quant = pnsrow.getQuant();
			long margin = pnsrow.getMargin();
			long net = pnsrow.getNet();
			long traded = pnsrow.getTraded();
			long cancelled = pnsrow.getCan();

			// check pnsrow
			if (quant != (margin + net + traded + cancelled))
				return ComStatus.CancelStatus.DB_PNS_MESS;

			// can need to check if can.clientid == poid; in reviewdata

			// update pnsrow
			if (pnsst == ComStatus.PnSStatus.PUBLISHED) {
				if (margin != 0)
					return ComStatus.CancelStatus.DB_PNS_MESS;
				if (net != quant)
					return ComStatus.CancelStatus.DB_PNS_MESS;
				if (traded != 0)
					return ComStatus.CancelStatus.DB_PNS_MESS;
				if (cancelled != 0)
					return ComStatus.CancelStatus.DB_PNS_MESS;

				net -= quant;
				cancelled += quant;

				try {
					pnsret = this.pns.updatePnSCancelPublic(can.getPnsoid().toString(), net, cancelled,
							ComStatus.PnSStatus.CANCELLED.toString());
					if (pnsret != 0)
						return ComStatus.CancelStatus.PNS_UPDATE_FAILED;
				} catch (Exception e) {
					return ComStatus.CancelStatus.PNS_UPDATE_FAILED;
				}
				// need to return some data like cancelling amount;
				amnt = can.getQuant();
				return ComStatus.CancelStatus.SUCCESS;
			}

			if (pnsst == ComStatus.PnSStatus.HALF_TRADED) {
				if (margin != 0) {
					// HALF_TRADED->FULL_LOCKED
					net -= 0;
					cancelled += net;

					pnsret = this.pns.updatePnSCancelPublic(can.getPnsoid().toString(), net, cancelled,
							ComStatus.PnSStatus.FULL_LOCKED.toString());
					if (pnsret != 0)
						return ComStatus.CancelStatus.PNS_UPDATE_FAILED;

					amnt = (int) cancelled;
					return ComStatus.CancelStatus.SUCCESS;
				} else {
					// HALF_TRADED->FULL_CANCELLED
					net -= 0;
					cancelled += net;

					pnsret = this.pns.updatePnSCancelPublic(can.getPnsoid().toString(), net, cancelled,
							ComStatus.PnSStatus.HALF_CANCELLED.toString());
					if (pnsret != 0)
						return ComStatus.CancelStatus.PNS_UPDATE_FAILED;

					amnt = (int) cancelled;
					return ComStatus.CancelStatus.SUCCESS;
				}
			}

		}

		return ComStatus.CancelStatus.TYPE_ERR;
	}

	// # dcancel
	public DCancelStatus updateOrdPnS(CDCancel can) throws CapiException, Exception {

		// update order from DEALING to CANCELLED
		OrderRow ordrow = null;
		try {
			ordrow = this.ord.selectOrder(can.getOid().toString());
		} catch (Exception e) {
			return ComStatus.DCancelStatus.DB_ORD_MISS;
		}

		if (ordrow == null)
			return ComStatus.DCancelStatus.DB_ORD_MISS;

		// check if ordrow match can
		if (ordrow.getPnsgid() != can.getPnsgid())
			return ComStatus.DCancelStatus.DB_ORD_MISS;

		if (ordrow.getPnsid() != can.getPnsid())
			return ComStatus.DCancelStatus.DB_ORD_MISS;

		if (!ordrow.getPnsoid().equals(can.getPnsoid().toString()))
			return ComStatus.DCancelStatus.DB_ORD_MISS;

		if (ordrow.getSide() != can.getSide())
			return ComStatus.DCancelStatus.DB_ORD_MISS;

		if (ordrow.getPrice() != can.getPrice())
			return ComStatus.DCancelStatus.DB_ORD_MISS;

		if (ordrow.getQuant() != can.getQuant())
			return ComStatus.DCancelStatus.DB_ORD_MISS;

		try {
			if (ComStatus.OrderStatus.DEALING != ComStatus.OrderStatus.valueOf(ordrow.getStatus()))
				return ComStatus.DCancelStatus.ORD_STATUS_FINAL;
		} catch (Exception e) {
			return ComStatus.DCancelStatus.DB_ORD_MESS;
		}

		// select pns and checks
		PnSRow pnsrow = null;
		try {
			pnsrow = this.pns.selectPnSRowOid(can.getPnsoid().toString());// for update
		} catch (Exception e) {
			return ComStatus.DCancelStatus.DB_PNS_MISS;
		}

		if (pnsrow == null)
			return ComStatus.DCancelStatus.DB_PNS_MISS;

		// check pns status
		PnSStatus pnsst = null;
		try {
			pnsst = ComStatus.PnSStatus.valueOf(pnsrow.getStatus());
		} catch (Exception e) {
			return ComStatus.DCancelStatus.DB_PNS_MESS;
		}
		if (pnsst == null)
			return ComStatus.DCancelStatus.DB_PNS_MESS;

		if ((pnsst != ComStatus.PnSStatus.HALF_TRADED) && (pnsst != ComStatus.PnSStatus.FULL_LOCKED))
			return ComStatus.DCancelStatus.DB_PNS_STATUS;

		// data process
		long quant = pnsrow.getQuant();
		long margin = pnsrow.getMargin();
		long traded = pnsrow.getTraded();
		long net = pnsrow.getNet();
		long canamt = pnsrow.getCan();

		// check data
		if (quant != (margin + net + traded + canamt))
			return ComStatus.DCancelStatus.DB_PNS_MESS;

		if (margin < can.getQuant())
			return ComStatus.DCancelStatus.DB_PNS_MESS;

		margin -= can.getQuant();
		net += can.getQuant();

		// update order status DEALING -> CANCELLED
		int ordret = 0;
		try {
			ordret = this.ord.updateOrderStatus(can.getOid().toString(), ComStatus.OrderStatus.CANCELLED.toString());
			if (ordret == 0)
				return ComStatus.DCancelStatus.ORD_UPDATE_FAILED;
		} catch (Exception e) {
			return ComStatus.DCancelStatus.ORD_UPDATE_FAILED;
		}

		// update pns status FULL_LOCKED/HALF_TRADED -> HALF_TRADED
		int pnsret = 0;
		try {
			pnsret = this.pns.updatePnSCancelDeal(can.getPnsoid().toString(), margin, net,
					ComStatus.PnSStatus.HALF_TRADED.toString());
			if (pnsret == 0)
				throw new CapiException(ComStatus.CancelStatus.PNS_UPDATE_FAILED.toString());

			return ComStatus.DCancelStatus.SUCCESS; // final success return
		} catch (CapiException e) {
			throw new CapiException(e.getMessage());
		} catch (Exception e) {
			// save into logs
			e.printStackTrace();
			throw new CapiException(ComStatus.DCancelStatus.PNS_UPDATE_FAILED.toString());
		}

	}

	// # pcancel
	public PCancelStatus updateOrdPnS(CPCancel can, CPCancelAns ans) throws CapiException, Exception {

		// update pns
		PnSRow pnsrow = null;
		try {
			pnsrow = this.pns.selectPnSRowOid(can.getPnsoid().toString());
			if (pnsrow == null)
				return ComStatus.PCancelStatus.DB_PNS_MISS;
		} catch (Exception e) {
			e.printStackTrace();
			return ComStatus.PCancelStatus.DB_PNS_MISS;
		}

		// check pns status
		PnSStatus pnsst = null;
		try {
			pnsst = ComStatus.PnSStatus.valueOf(pnsrow.getStatus());
			if (pnsst == null)
				return ComStatus.PCancelStatus.DB_PNS_STATUS;
		} catch (Exception e) {
			return ComStatus.PCancelStatus.DB_PNS_STATUS;
		}

		if ((pnsst != ComStatus.PnSStatus.PUBLISHED) && (pnsst != ComStatus.PnSStatus.HALF_TRADED))
			return ComStatus.PCancelStatus.PNS_STATUS_FINAL;

		// process logic
		long quant = pnsrow.getQuant();
		long margin = pnsrow.getMargin();
		long net = pnsrow.getNet();
		long traded = pnsrow.getTraded();
		long canamt = pnsrow.getCan();

		// check pnsrow
		if (quant != (margin + net + traded + canamt))
			return ComStatus.PCancelStatus.DB_PNS_MESS;

		// update pnsrow
		int pnsret = 0;
		
		if (pnsst == ComStatus.PnSStatus.PUBLISHED) {
			if (margin != 0)
				return ComStatus.PCancelStatus.DB_PNS_MESS;
			if (net != quant)
				return ComStatus.PCancelStatus.DB_PNS_MESS;
			if (traded != 0)
				return ComStatus.PCancelStatus.DB_PNS_MESS;
			if (canamt != 0)
				return ComStatus.PCancelStatus.DB_PNS_MESS;

			net -= quant;
			canamt += quant;
			

			try {
				pnsret = this.pns.updatePnSCancelPublic(can.getPnsoid().toString(), net, canamt, ComStatus.PnSStatus.CANCELLED.toString());
				
				if (pnsret != 0)
					return ComStatus.PCancelStatus.PNS_UPDATE_FAILED;
			} catch (Exception e) {
				return ComStatus.PCancelStatus.PNS_UPDATE_FAILED;
			}
			
			// return updated canamt
			ans.setAmount(canamt);
			return ComStatus.PCancelStatus.SUCCESS;
		}

		if (pnsst == ComStatus.PnSStatus.HALF_TRADED) {
			if (margin != 0) {
				// HALF_TRADED->FULL_LOCKED
				net = 0;
				canamt += net;

				pnsret = this.pns.updatePnSCancelPublic(can.getPnsoid().toString(), net, canamt, ComStatus.PnSStatus.FULL_LOCKED.toString());
				if (pnsret != 0)
					return ComStatus.PCancelStatus.PNS_UPDATE_FAILED;

				ans.setAmount(canamt);
				return ComStatus.PCancelStatus.SUCCESS; // success return
				
			} 
			else {
				// HALF_TRADED->HALF_CANCELLED
				net = 0;
				canamt += net;

				pnsret = this.pns.updatePnSCancelPublic(can.getPnsoid().toString(), net, canamt, ComStatus.PnSStatus.HALF_CANCELLED.toString());
				if (pnsret != 0)
					return ComStatus.PCancelStatus.PNS_UPDATE_FAILED;

				ans.setAmount(canamt);
				return ComStatus.PCancelStatus.SUCCESS; // success return
			}
		}
		
		return ComStatus.PCancelStatus.PNS_STATUS_FINAL;
	}

}
