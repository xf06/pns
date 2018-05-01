package com.blackjade.publisher.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.blackjade.publisher.apis.CCancel;
import com.blackjade.publisher.apis.CDeal;
import com.blackjade.publisher.apis.CPaid;
import com.blackjade.publisher.apis.CPayConfirm;
import com.blackjade.publisher.apis.ComStatus;
import com.blackjade.publisher.apis.ComStatus.CancelStatus;
import com.blackjade.publisher.apis.ComStatus.DealStatus;
import com.blackjade.publisher.apis.ComStatus.PaidStatus;
import com.blackjade.publisher.apis.ComStatus.PayConfirmStatus;
import com.blackjade.publisher.dao.OrderDao;
import com.blackjade.publisher.dao.PnSDao;
import com.blackjade.publisher.domain.OrderRow;
import com.blackjade.publisher.domain.PnSRow;

@Transactional
@Component
public class TService {

	@Autowired
	private OrderDao ord;

	@Autowired
	private PnSDao pns;

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

		if (quant != margin + traded + net)
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
			if (margin < quant) {
				int retcode = this.pns.updatePnSDeal(oid, margin, net, ComStatus.PnSStatus.HALF_TRADED.toString());
				if (retcode != 0)
					return ComStatus.DealStatus.SUCCESS;
				else
					return ComStatus.DealStatus.DATABASE_ERR;
			}

			if (margin == quant) {
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

			if (margin < quant) {
				int retcode = this.pns.updatePnSDeal(oid, margin, net, ComStatus.PnSStatus.HALF_TRADED.toString());
				if (retcode != 0)
					return ComStatus.DealStatus.SUCCESS;
				else
					return ComStatus.DealStatus.DATABASE_ERR;
			}

			if (margin == quant) {
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
			this.ord.selectOrder(paid.getOid().toString());// select for update
		} finally {
			if (ordrow == null)
				return ComStatus.PaidStatus.ORD_MISS_MATCH;
		}

		// check input
		if (!ordrow.getOid().equals(paid.getPnsoid().toString()))
			return ComStatus.PaidStatus.ORD_MISS_MATCH;
		if (ordrow.getPnsid() != paid.getPnsid())
			return ComStatus.PaidStatus.ORD_MISS_MATCH;
		if (ordrow.getPnsgid() != paid.getPnsgid())
			return ComStatus.PaidStatus.ORD_MISS_MATCH;
		if (ordrow.getPoid() != paid.getPoid())
			return ComStatus.PaidStatus.ORD_MISS_MATCH;
		if (ordrow.getSide() == paid.getSide()) // B<->S //S<->B // side checking //need some work here
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
	public PayConfirmStatus updateOrdPnS(final CPayConfirm paycon) {
		
		// update order status from Paid->PayConfirmed
		// 1 select if the order is what it is
		// 2 check status 
		
		int retcode=0;
		try {
			retcode = this.ord.updateOrderStatus(paycon.getOid().toString(), ComStatus.OrderStatus.PAYCONFIRM.toString());
		}
		catch(Exception e) {
			// logging data
		}
		finally {
			if(retcode==0) {
				return ComStatus.PayConfirmStatus.PC_DATABASE_ERR;
			}
		}
		
		// update PnS from margin->traded
		PnSRow pnsrow = this.pns.selectPnSRowOid(paycon.getPnsoid().toString());
		if(pnsrow==null)
			return ComStatus.PayConfirmStatus.PC_DB_MISS_MATCH;
		
		// check PnS Status
		long quant = pnsrow.getQuant();
		long margin = pnsrow.getMargin();
		long traded= pnsrow.getTraded();
		long net = pnsrow.getNet();

		if(quant==(margin+traded+net))
			return ComStatus;
		
		PUBLISHED,		//0
		HALF_TRADED,	//0
		//HALF_LOCKED,	//0
		FULL_LOCKED,	//0
		FULL_TRADED,	//final
		CANCELLED,		//final
		HALF_CANCELLED,	//final
		UNKNOWN			//final
		
		// check PnS data 
		
		// update Status 
		
		return ComStatus.PayConfirmStatus.UNKNOWN;
	}

	// # cancel
	public CancelStatus updatePnS(final CCancel can) {
		return ComStatus.CancelStatus.UNKNOWN;
	}

}
