package com.blackjade.subscriber.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blackjade.subscriber.apis.CQueryAllOrdRecv;
import com.blackjade.subscriber.apis.CQueryAllOrdRecvAns;
import com.blackjade.subscriber.apis.CQueryAllOrdSent;
import com.blackjade.subscriber.apis.CQueryAllOrdSentAns;
import com.blackjade.subscriber.apis.CQueryOwnAccPage;
import com.blackjade.subscriber.apis.CQueryOwnAccPageAns;
//import com.blackjade.subscriber.apis.CQueryOwnOrd;
//import com.blackjade.subscriber.apis.CQueryOwnOrdAns;
import com.blackjade.subscriber.apis.CQueryOwnPage;
import com.blackjade.subscriber.apis.CQueryOwnPageAns;
import com.blackjade.subscriber.apis.CQueryPnSOrder;
import com.blackjade.subscriber.apis.CQueryPnSOrderAns;
import com.blackjade.subscriber.apis.CQueryPnSPage;
import com.blackjade.subscriber.apis.CQueryPnSPageAns;
import com.blackjade.subscriber.apis.ComStatus;
import com.blackjade.subscriber.apis.ComStatus.QueryAllOrdRecvStatus;
import com.blackjade.subscriber.apis.ComStatus.QueryAllOrdSentStatus;
import com.blackjade.subscriber.apis.ComStatus.QueryOwnAccStatus;
import com.blackjade.subscriber.apis.ComStatus.QueryOwnOrdStatus;
import com.blackjade.subscriber.apis.ComStatus.QueryOwnStatus;
import com.blackjade.subscriber.apis.ComStatus.QueryPnSOrdStatus;
import com.blackjade.subscriber.apis.ComStatus.QueryPnSStatus;
import com.blackjade.subscriber.dao.AccBookDao;
import com.blackjade.subscriber.dao.OrdBookDao;
import com.blackjade.subscriber.dao.PubBookDao;
import com.blackjade.subscriber.domain.AllOrdRecvRow;
import com.blackjade.subscriber.domain.AllOrdSentRow;
import com.blackjade.subscriber.domain.OrdBookRow;
import com.blackjade.subscriber.domain.OwnAccRow;
import com.blackjade.subscriber.domain.OwnBookRow;
import com.blackjade.subscriber.domain.PubBookRow;


@RestController
public class SubController {

	private static final Logger sublog = LogManager.getLogger(SubController.class.getName()); 

	@Autowired
	private PubBookDao pubbook;

	@Autowired
	private OrdBookDao ordbook;

	@Autowired
	private AccBookDao accbook;
	
	
	@RequestMapping(value = "/market", method = RequestMethod.POST)
	@ResponseBody

	public CQueryPnSPageAns QueryPnSPage(@RequestBody CQueryPnSPage qpns) {
		
		sublog.info(qpns.toString());// original message
		
		// check input msg
		QueryPnSStatus st = qpns.reviewData();

		// construct ans
		CQueryPnSPageAns ans = new CQueryPnSPageAns(qpns.getRequestid());

		ans.setClientid(qpns.getClientid());
		ans.setPnsgid(qpns.getPnsgid());
		ans.setPnsid(qpns.getPnsid());
		ans.setSide(qpns.getSide());
		ans.setStart(qpns.getStart());
		ans.setLength(qpns.getLength());

		if (st != ComStatus.QueryPnSStatus.SUCCESS) {
			ans.setStatus(st);
			sublog.warn(ans.toString());
			return ans;
		}

		// select num and PubBookRows
		int totalnum = 0;
		try {
			totalnum = this.pubbook.selectNumPns(qpns.getPnsgid(), qpns.getPnsid(), qpns.getSide());
			if (totalnum == 0) {
				ans.setRecordsFiltered(totalnum);
				ans.setStatus(ComStatus.QueryPnSStatus.PNS_EMPTY);
				sublog.warn(ans.toString());
				return ans;
			}
		} catch (Exception e) {
			ans.setRecordsFiltered(totalnum);
			ans.setStatus(ComStatus.QueryPnSStatus.PNS_DB_MISS);
			sublog.warn(ans.toString());
			return ans;
		}

		List<PubBookRow> elist = null;

		try {
			elist = this.pubbook.selectPubBookRow(qpns.getPnsgid(), qpns.getPnsid(), qpns.getSide(), qpns.getStart());
			if (elist == null) {
				ans.setStatus(ComStatus.QueryPnSStatus.PNS_EMPTY);
				sublog.warn(ans.toString());
				return ans;
			}
		} catch (Exception e) {
			ans.setStatus(ComStatus.QueryPnSStatus.PNS_DB_MISS);
			sublog.warn(ans.toString());
			return ans;
		}

		ans.setData(elist);
		ans.setRecordsFiltered(totalnum);
		ans.setStatus(ComStatus.QueryPnSStatus.SUCCESS);
		sublog.info(ans.toString());
		return ans;
	}

	@RequestMapping(value = "/ownpns", method = RequestMethod.POST)
	@ResponseBody
	public CQueryOwnPageAns QueryOwnPage(@RequestBody CQueryOwnPage qpns) {
		
		sublog.info(qpns.toString());

		QueryOwnStatus st = qpns.reviewData();

		// construct ans
		CQueryOwnPageAns ans = new CQueryOwnPageAns(qpns.getRequestid());
		ans.setClientid(qpns.getClientid());
		ans.setPnsgid(qpns.getPnsgid());
		ans.setPnsid(qpns.getPnsid());
		ans.setSide(qpns.getSide());
		ans.setStart(qpns.getStart());
		ans.setLength(qpns.getLength());

		if (st != ComStatus.QueryOwnStatus.SUCCESS) {
			ans.setStatus(st);
			sublog.warn(ans.toString());
			return ans;
		}

		// get number
		int totalnum = 0;
		try {
			totalnum = this.pubbook.selectOwnNumPns(qpns.getClientid(), qpns.getPnsgid(), qpns.getPnsid(),
					qpns.getSide());
			if (totalnum == 0) {
				ans.setRecordsFiltered(totalnum);
				ans.setStatus(ComStatus.QueryOwnStatus.PNS_EMPTY);
				sublog.warn(ans.toString());
				return ans;
			}
		} catch (Exception e) {
			ans.setRecordsFiltered(totalnum);
			ans.setStatus(ComStatus.QueryOwnStatus.PNS_DB_MISS);
			sublog.warn(ans.toString());
			return ans;
		}

		// getlist
		List<OwnBookRow> elist = null;
		try {
			elist = this.pubbook.selectOwnBookRow(qpns.getClientid(), qpns.getPnsgid(), qpns.getPnsid(), qpns.getSide(),
					qpns.getStart());
			if (elist == null) {
				ans.setStatus(ComStatus.QueryOwnStatus.PNS_DB_MISS);
				sublog.warn(ans.toString());
				return ans;
			}
		} catch (Exception e) {
			ans.setStatus(ComStatus.QueryOwnStatus.PNS_DB_MISS);
			sublog.warn(ans.toString());
			return ans;
		}

		// set list and num then return
		ans.setRecordsFiltered(totalnum);
		ans.setData(elist);
		ans.setStatus(ComStatus.QueryOwnStatus.SUCCESS);
		sublog.info(ans.toString());
		return ans;

	}

	@RequestMapping(value = "/pnsorder", method = RequestMethod.POST)
	@ResponseBody
	public CQueryPnSOrderAns QueryPnSOrder(@RequestBody CQueryPnSOrder qpns) {

		sublog.info(qpns.toString());
		
		// check input data
		QueryPnSOrdStatus st = qpns.reviewData();

		// construct ans
		CQueryPnSOrderAns ans = new CQueryPnSOrderAns(qpns.getRequestid());
		ans.setClientid(qpns.getClientid());
		ans.setPnsgid(qpns.getPnsgid());
		ans.setPnsid(qpns.getPnsid());

		ans.setSide(qpns.getSide());

		if (st != ComStatus.QueryPnSOrdStatus.SUCCESS) {
			ans.setStatus(st);
			sublog.warn(ans.toString());
			return ans;
		}

		ans.setStatus(ComStatus.QueryPnSOrdStatus.SUCCESS);

		// query numbers
		// <-->//
		int totalnum = 0;
		char side = 'S';
		if (qpns.getSide() == 'B') {
			side = 'S';
		} else {
			if (qpns.getSide() == 'S') {
				side = 'B';
			} else {
				// there isn't a need if reviewdata works
				ans.setStatus(ComStatus.QueryPnSOrdStatus.UNKNOWN);
				sublog.warn(ans.toString());
				return ans;
			}
		}

		try {
			totalnum = this.ordbook.selectNumPnsOrder(qpns.getPnsoid().toString(), qpns.getClientid(), qpns.getPnsgid(),
					qpns.getPnsid(), side);
			if (totalnum == 0) {
				ans.setStatus(ComStatus.QueryPnSOrdStatus.ORD_DB_EMPTY);
				sublog.warn(ans.toString());
				return ans;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ans.setStatus(ComStatus.QueryPnSOrdStatus.ORD_DB_MISS);
			sublog.warn(ans.toString());
			return ans;
		}

		// query lists
		List<OrdBookRow> elist = null;

		try {
			elist = this.ordbook.selectOrdBookRow(qpns.getPnsoid().toString(), qpns.getClientid(), qpns.getPnsgid(),
					qpns.getPnsid(), side, qpns.getStart());
			if ((elist == null) || (elist.isEmpty())) {
				ans.setStatus(ComStatus.QueryPnSOrdStatus.ORD_DB_MISS);
				sublog.warn(ans.toString());
				return ans;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ans.setStatus(ComStatus.QueryPnSOrdStatus.ORD_DB_MISS);
			sublog.warn(ans.toString());
			return ans;
		}

		ans.setRecordsFiltered(totalnum);// not in use
		ans.setData(elist);
		ans.setLength(10);// always as it is
		sublog.info(ans.toString());
		return ans;
	}
//
//	@RequestMapping(value = "/ownord", method = RequestMethod.POST)
//	@ResponseBody
//	public CQueryOwnOrdAns QueryOwnOrd(@RequestBody CQueryOwnOrd qord) {
//
//		sublog.info(qord.toString());
//		
//		// check input
//		QueryOwnOrdStatus st = qord.reviewData();
//
//		// construct ans
//		CQueryOwnOrdAns ans = new CQueryOwnOrdAns(qord.getRequestid());
//		ans.setPnsgid(qord.getPnsgid());
//		ans.setPnsid(qord.getPnsid());
//		ans.setPnsoid(qord.getPnsoid());
//		ans.setPoid(qord.getPoid());
//		ans.setSide(qord.getSide());
//
//		if (st != ComStatus.QueryOwnOrdStatus.SUCCESS) {
//			ans.setStatus(ComStatus.QueryOwnOrdStatus.INMSG_ERR);
//			sublog.warn(ans.toString());
//			return ans;
//		}
//
//		// get obr from database
//		OrdBookRow obr = null;
//		try {
//			obr = this.ordbook.selectOwnOrd(qord.getOid().toString(), qord.getCid(), qord.getPnsoid().toString(),
//					qord.getPoid(), qord.getPnsgid(), qord.getPnsid(), qord.getSide());
//
//			if (obr == null) {
//				ans.setStatus(ComStatus.QueryOwnOrdStatus.ORD_DB_EMPTY);
//				sublog.warn(ans.toString());
//				return ans;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			ans.setStatus(ComStatus.QueryOwnOrdStatus.ORD_DB_MISS);
//			sublog.warn(ans.toString());
//			return ans;
//		}
//
//		ans.setOrd(obr);
//		ans.setStatus(ComStatus.QueryOwnOrdStatus.SUCCESS);
//		sublog.info(ans.toString());
//		return ans;
//	}

	@RequestMapping(value = "/allordsent", method = RequestMethod.POST)
	@ResponseBody
	public CQueryAllOrdSentAns QueryAllOrdSent(@RequestBody CQueryAllOrdSent qaos) {
		
		sublog.info(qaos.toString());
		
		// input review data
		QueryAllOrdSentStatus st = qaos.reviewData();

		// construct output ans
		CQueryAllOrdSentAns ans = new CQueryAllOrdSentAns(qaos.getRequestid());
		ans.setPnsgid(qaos.getPnsgid());
		ans.setPnsid(qaos.getPnsid());
		ans.setClientid(qaos.getClientid());
		ans.setStart(qaos.getStart());
		ans.setLength(10);

		if (st != ComStatus.QueryAllOrdSentStatus.SUCCESS) {
			ans.setStatus(st);
			sublog.warn(ans.toString());
			return ans;
		}

		// select the number of ord that sent
		int num = 0;
		try {
			num = this.ordbook.selectNumAllOrdSent(qaos.getClientid(), qaos.getPnsgid(), qaos.getPnsid());
			if (num == 0) {
				ans.setRecordsFiltered(num);
				ans.setStatus(ComStatus.QueryAllOrdSentStatus.ORD_DB_EMPTY);
				sublog.warn(ans.toString());
				return ans;
			}
		} catch (Exception e) {
			ans.setRecordsFiltered(num);
			ans.setStatus(ComStatus.QueryAllOrdSentStatus.ORD_DB_MISS);
			sublog.warn(ans.toString());
			return ans;
		}

		// select the list of ord that sent
		List<AllOrdSentRow> elist = null;
		try {
			elist = this.ordbook.selectAllOrdSent(qaos.getClientid(), qaos.getPnsgid(), qaos.getPnsid(), qaos.getStart());
			if (elist == null) {
				ans.setRecordsFiltered(0);
				ans.setStatus(ComStatus.QueryAllOrdSentStatus.ORD_DB_MISS);
				sublog.warn(ans.toString());
				return ans;
			}

		} catch (Exception e) {
			ans.setRecordsFiltered(0);
			ans.setStatus(ComStatus.QueryAllOrdSentStatus.ORD_DB_MISS);
			sublog.warn(ans.toString());
			return ans;
		}

		ans.setRecordsFiltered(num);
		ans.setData(elist);
		ans.setStatus(ComStatus.QueryAllOrdSentStatus.SUCCESS);
		sublog.info(ans.toString());
		return ans;
	}

	@RequestMapping(value = "/allordrecv", method = RequestMethod.POST)
	@ResponseBody
	public CQueryAllOrdRecvAns QueryAllOrdRecv(@RequestBody CQueryAllOrdRecv qaor) {

		sublog.info(qaor.toString());

		QueryAllOrdRecvStatus st = qaor.reviewData();

		CQueryAllOrdRecvAns ans = new CQueryAllOrdRecvAns(qaor.getRequestid());

		ans.setClientid(qaor.getClientid());
		ans.setPnsgid(qaor.getPnsgid());
		ans.setPnsid(qaor.getPnsid());
		ans.setStart(qaor.getStart());
		ans.setLength(10);

		if (st != ComStatus.QueryAllOrdRecvStatus.SUCCESS) {
			ans.setStatus(st);
			sublog.warn(ans.toString());
			return ans;
		}

		// select the num of ord that recv
		int num = 0;
		try {
			num = this.ordbook.selectNumAllOrdRecv(qaor.getClientid(), qaor.getPnsgid(), qaor.getPnsid());
			if (num == 0) {
				ans.setRecordsFiltered(num);
				ans.setStatus(ComStatus.QueryAllOrdRecvStatus.ORD_DB_EMPTY);
				sublog.warn(ans.toString());
				return ans;
			}
		} catch (Exception e) {
			ans.setRecordsFiltered(0);
			ans.setStatus(ComStatus.QueryAllOrdRecvStatus.ORD_DB_MISS);
			sublog.warn(ans.toString());
			return ans;
		}

		// select the list of ord that recv
		List<AllOrdRecvRow> elist = null;
		try {
			elist = this.ordbook.selectAllOrdRecv(qaor.getClientid(), qaor.getPnsgid(), qaor.getPnsid(),
					qaor.getStart());
			if (elist == null) {
				ans.setRecordsFiltered(0);
				ans.setStatus(ComStatus.QueryAllOrdRecvStatus.ORD_DB_MISS);
				sublog.warn(ans.toString());
				return ans;
			}
		} catch (Exception e) {
			ans.setRecordsFiltered(0);
			ans.setStatus(ComStatus.QueryAllOrdRecvStatus.ORD_DB_MISS);
			sublog.warn(ans.toString());
			return ans;
		}

		ans.setRecordsFiltered(num);
		ans.setData(elist);
		ans.setStatus(ComStatus.QueryAllOrdRecvStatus.SUCCESS);
		sublog.info(ans.toString());
		return ans;
	}

	// ----------- the ACC query ---------------//
	@RequestMapping(value = "/ownacc", method = RequestMethod.POST)
	@ResponseBody
	public CQueryOwnAccPageAns QueryOwnAccPage(@RequestBody CQueryOwnAccPage qacc) {
		
		sublog.info(qacc.toString());
		
		// review income data
		QueryOwnAccStatus st = qacc.reviewData();
		
		// construct ans
		CQueryOwnAccPageAns ans = new CQueryOwnAccPageAns(qacc.getRequestid());
		
		ans.setClientid(qacc.getClientid());				
		ans.setStart(qacc.getStart());
		ans.setLength(20);
		
		// this is from select
		
		if(ComStatus.QueryOwnAccStatus.SUCCESS!=st) {
			ans.setStatus(st);
			sublog.warn(ans.toString());
			return ans;
		}
		
		int num=0;		
		try {
			num = this.accbook.selectAccBookNum(qacc.getClientid());
			if(0==num) {
				ans.setStatus(ComStatus.QueryOwnAccStatus.ACC_DB_EMPTY);
				ans.setRecordsFiltered(0);
				sublog.warn(ans.toString());
				return ans;
			}
		}
		catch(Exception e) {
			//>>//
			e.printStackTrace();
			ans.setRecordsFiltered(0);
			ans.setStatus(ComStatus.QueryOwnAccStatus.ACC_DB_MISS);
			sublog.warn(ans.toString());
			return ans;
		}

		ans.setRecordsFiltered(num);
		
		List<OwnAccRow> elist = null;
		
		try {
			elist = this.accbook.selectAccBookRows(qacc.getClientid(), qacc.getStart());
			if(elist==null) {
				//ans.setRecordsFiltered();
				ans.setStatus(ComStatus.QueryOwnAccStatus.ACC_DB_EMPTY);
				sublog.warn(ans.toString());
				return ans;
			}
		}
		catch(Exception e) {
			ans.setStatus(ComStatus.QueryOwnAccStatus.ACC_DB_MISS);
			sublog.warn(ans.toString());
			return ans;
		}
		
		ans.setData(elist);
		ans.setStatus(ComStatus.QueryOwnAccStatus.SUCCESS);
		sublog.info(ans.toString());
		return ans;
	}
	
}

