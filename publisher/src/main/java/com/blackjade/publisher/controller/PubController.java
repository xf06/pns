package com.blackjade.publisher.controller;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blackjade.publisher.apis.CDCancel;
import com.blackjade.publisher.apis.CDCancelAns;
import com.blackjade.publisher.apis.CDeal;
import com.blackjade.publisher.apis.CDealAns;
import com.blackjade.publisher.apis.CPCancel;
import com.blackjade.publisher.apis.CPCancelAns;
import com.blackjade.publisher.apis.CPaid;
import com.blackjade.publisher.apis.CPaidAns;
import com.blackjade.publisher.apis.CPayConfirm;
import com.blackjade.publisher.apis.CPayConfirmAns;
import com.blackjade.publisher.apis.CPublish;
import com.blackjade.publisher.apis.CPublishAns;
import com.blackjade.publisher.apis.ComStatus;
import com.blackjade.publisher.apis.ComStatus.DCancelStatus;
import com.blackjade.publisher.apis.ComStatus.DealStatus;
import com.blackjade.publisher.apis.ComStatus.PCancelStatus;
import com.blackjade.publisher.apis.ComStatus.PublishStatus;
import com.blackjade.publisher.controller.service.TService;
import com.blackjade.publisher.dao.OrderDao;
import com.blackjade.publisher.dao.PnSDao;
import com.blackjade.publisher.domain.OrderRow;
import com.blackjade.publisher.domain.PnSRow;
import com.blackjade.publisher.exception.CapiException;

@RestController
public class PubController {

	@Autowired
	private OrderDao ord;

	@Autowired
	private PnSDao pns;

	@Autowired
	private TService tsv;
	
	private static final Logger publog = LogManager.getLogger(PubController.class.getName());
	
	// Publish
	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	@ResponseBody
	public CPublishAns cPublish(@RequestBody CPublish pub) {

		publog.info(pub.toString());
		
		PublishStatus st = pub.reviewData();

		UUID oid = UUID.randomUUID();//####### oid created
		// construct answer object
		CPublishAns ans = new CPublishAns(pub.getRequestid());

		ans.setClientid(pub.getClientid());
		ans.setSide(pub.getSide());
		ans.setOid(oid);
		
		ans.setPnsid(pub.getPnsid());
		ans.setPnsgid(pub.getPnsgid());

		ans.setPrice(pub.getPrice());
		ans.setQuant(pub.getQuant());

		ans.setMin(pub.getMin());
		ans.setMax(pub.getMax());

		if (st != ComStatus.PublishStatus.SUCCESS) {
			ans.setStatus(st);
			publog.warn(ans.toString());
			return ans;
		}

		// SQL return code
		int retcode = 0;

		// update product list
		
		PnSRow pnsrow = new PnSRow();
		pnsrow.setTime(System.currentTimeMillis());
		pnsrow.setOid(oid.toString());
		pnsrow.setPnsid(pub.getPnsid());
		pnsrow.setPnsgid(pub.getPnsgid());
		pnsrow.setPoid(pub.getClientid());
		pnsrow.setSide(pub.getSide());
		pnsrow.setPrice(pub.getPrice());
		pnsrow.setQuant(pub.getQuant());
		pnsrow.setStatus(ComStatus.PnSStatus.PUBLISHED.toString());		
		pnsrow.setTraded(0);
		pnsrow.setMargin(0);
		pnsrow.setNet(pub.getQuant());
		pnsrow.setMax(pub.getMax());
		pnsrow.setMin(pub.getMin());
		
		// save 
		try {
			retcode = this.pns.insertPnS(pnsrow);
		}
		catch(Exception e) {
			ans.setStatus(ComStatus.PublishStatus.DATABASE_ERR);
			publog.warn(ans.toString());
			return ans;
		}
		
		if(retcode==0) {
			ans.setStatus(ComStatus.PublishStatus.DATABASE_ERR);
			publog.warn(ans.toString());
			return ans;
		}
			
		ans.setStatus(ComStatus.PublishStatus.SUCCESS);
		publog.info(ans.toString());
		return ans;
	}
	
	// Deal // price checking need to be done
	@RequestMapping(value = "/deal", method = RequestMethod.POST)
	@ResponseBody
	public CDealAns cDeal(@RequestBody CDeal deal) {
		
		publog.info(deal.toString());
		
		// check input errors
		DealStatus st = deal.reviewData();
		UUID oid = UUID.randomUUID();

		// construct ans obj
		CDealAns ans = new CDealAns(deal.getRequestid());
		ans.setClientid(deal.getClientid());
		ans.setOid(oid);						//deal oid
		
		ans.setPnsoid(deal.getPnsoid());		//PnS oid
		ans.setPoid(deal.getPoid());
		
		ans.setPnsid(deal.getPnsid());
		ans.setPnsgid(deal.getPnsgid());
		
		ans.setPrice(deal.getPrice());
		ans.setQuant(deal.getQuant());
		ans.setSide(deal.getSide());
				
		
		if(st!=ComStatus.DealStatus.SUCCESS) {
			ans.setStatus(st);
			publog.warn(ans.toString());
			return ans;
		}
		
		// saving into orders // this should be done after
		OrderRow ordrow = new OrderRow();
			
		ordrow.setTimestamp(System.currentTimeMillis());
		ordrow.setOid(oid.toString());
		ordrow.setCid(deal.getClientid());
		ordrow.setType('D');
		ordrow.setSide(deal.getSide());
		ordrow.setPnsoid(deal.getPnsoid().toString());
		ordrow.setPoid(deal.getPoid());
		ordrow.setPnsid(deal.getPnsid());
		ordrow.setPnsgid(deal.getPnsgid());
		ordrow.setPrice(deal.getPrice());
		ordrow.setQuant(deal.getQuant());
		ordrow.setForm("normal");
		
		// update PnS database if necessary 
		st = this.tsv.updatePnS(deal);		
		ans.setStatus(st);

		switch(st) {
			case SUCCESS: 			ordrow.setStatus(ComStatus.OrderStatus.DEALING.toString()); publog.info(ans.toString()); break; 
			case PNS_STATUS_FINAL:	ordrow.setStatus(ComStatus.OrderStatus.REJECT_DONE.toString()); publog.warn(ans.toString()); break;
			case PNS_STATUS_LOCKED:	ordrow.setStatus(ComStatus.OrderStatus.REJECT_LOCK.toString()); publog.warn(ans.toString()); break;
			default:				ordrow.setStatus(ComStatus.OrderStatus.ERROR.toString()); publog.warn(ans.toString());
		}
		
		this.ord.insertOrder(ordrow);

		return ans;
	}
	
	// Paid
	@RequestMapping(value = "/paid", method = RequestMethod.POST)
	@ResponseBody
	public CPaidAns cPaid(@RequestBody CPaid paid) {
		
		publog.info(paid.toString());
		
		// check input 
		ComStatus.PaidStatus st = paid.reviewData();
		
		// construct ans
		CPaidAns ans = new CPaidAns(paid.getRequestid());
		ans.setClientid(paid.getClientid());
		ans.setOid(paid.getOid());
		ans.setSide(paid.getSide());
		ans.setPnsoid(paid.getPnsoid());
		ans.setPnsid(paid.getPnsid());
		ans.setPnsgid(paid.getPnsgid());
		ans.setPrice(paid.getPrice());
		ans.setQuant(paid.getQuant());
		
		// report error if occur
		if(st!=ComStatus.PaidStatus.SUCCESS) {
			ans.setStatus(st);
			publog.warn(ans.toString());
			return ans;
		}
				
		// select the deal order check if it matches
		// if so, update order
		// if not return error
		// no need to update PnS for this
		
		st = this.tsv.updateOrd(paid);
		ans.setStatus(st);		
		publog.info(ans.toString());
		
		return ans;
	}
	
	// PayConfirm
	@RequestMapping(value = "/payconfirm", method = RequestMethod.POST)
	@ResponseBody
	public CPayConfirmAns cPayConfirm(@RequestBody CPayConfirm paycon) {
		// check input 
		publog.info(paycon.toString());
		
		ComStatus.PayConfirmStatus st = paycon.reviewData();
		
		// construct ans
		CPayConfirmAns ans = new CPayConfirmAns(paycon.getRequestid());		
		
		ans.setRequestid(paycon.getRequestid());
		ans.setClientid(paycon.getClientid());	// message owner
		
		ans.setOid(paycon.getOid()); 			// deal orderid
		ans.setSide(paycon.getSide()); 			// deal side
		ans.setCid(paycon.getCid());			// deal side
				
		ans.setPnsoid(paycon.getPnsoid());		// deal side
		ans.setPoid(paycon.getPoid());			// publish side
		ans.setPnsid(paycon.getPnsid());		// deal side
		ans.setPnsgid(paycon.getPnsgid());		// deal side
		ans.setPrice(paycon.getPrice());
		ans.setQuant(paycon.getQuant());		
		
		if(st!=ComStatus.PayConfirmStatus.SUCCESS) {
			ans.setStatus(st);
			publog.warn(ans.toString());
			return ans;
		}
		
		// update order status from Paid->PayConfirmed
		// update PnS from margin->traded
		try {
			st = this.tsv.updateOrdPnS(paycon);
		}
		catch(CapiException e) {
			ans.setStatus(ComStatus.PayConfirmStatus.valueOf(e.getMessage()));
			publog.warn(ans.toString());
			return ans;
		}
		catch(Exception e) {
			ans.setStatus(ComStatus.PayConfirmStatus.ORD_ERR);
			publog.warn(ans.toString());
			return ans;
		}
		
		if(st!=ComStatus.PayConfirmStatus.SUCCESS) {
			ans.setStatus(st);
			publog.warn(ans.toString());
			return ans;
		}		
		
		// *************************************************** //
		// send msg to update APM // here it will be very slow //
		// *************************************************** //
		
		ans.setStatus(st);
		publog.info(ans.toString());
		return ans;
	}
	
	// CDCancel
	@RequestMapping(value = "/dcancel", method = RequestMethod.POST)
	@ResponseBody
	public CDCancelAns cDCancel(@RequestBody CDCancel can) {
				
		publog.info(can.toString());

		DCancelStatus st = can.reviewData();			
		CDCancelAns ans =  new CDCancelAns(can.getRequestid());
		
		ans.setClientid(can.getClientid());
		ans.setOid(ans.getOid());
		ans.setCid(ans.getCid());
		ans.setSide(ans.getSide());
		
		ans.setPnsoid(ans.getPnsoid());
		ans.setPoid(ans.getPoid());
		ans.setPnsgid(ans.getPnsgid());
		ans.setPnsid(ans.getPnsid());
		
		ans.setPrice(ans.getPrice());
		ans.setQuant(ans.getQuant());
		
		if(ComStatus.DCancelStatus.SUCCESS!=st) {
			ans.setStatus(st);
			publog.warn(ans.toString());
			return ans;
		}
		
		try{
			
			st = this.tsv.updateOrdPnS(can);
			
			if(ComStatus.DCancelStatus.SUCCESS!=st) {
				ans.setStatus(st);
				publog.warn(ans.toString());
				return ans;
			}
			
			ans.setStatus(st);
			publog.info(ans.toString());
			return ans; // success return
		}
		catch(CapiException e) {
			ans.setStatus(ComStatus.DCancelStatus.valueOf(e.getMessage()));
			publog.warn(ans.toString());
			return ans;
		}
		catch(Exception e) {			
			ans.setStatus(ComStatus.DCancelStatus.UNKNOWN);
			publog.warn(ans.toString());
			return ans;
		}
		
	}
	
	// CPCancel
	@RequestMapping(value = "/pcancel", method = RequestMethod.POST)
	@ResponseBody
	public CPCancelAns cPCancel(@RequestBody CPCancel can) {
		
		publog.info(can.toString());
		
		PCancelStatus st =  can.reviewData();
		CPCancelAns ans = new CPCancelAns(can.getRequestid());
		
		ans.setClientid(can.getClientid());
		ans.setSide(can.getSide());
		
		ans.setPnsoid(can.getPnsoid());
		ans.setPoid(can.getPoid());
		ans.setPnsgid(can.getPnsgid());
		ans.setPnsid(can.getPnsid());
		ans.setAmount(can.getAmount());// this will be updated
		
		if(ComStatus.PCancelStatus.SUCCESS!=st) {
			ans.setStatus(st);
			publog.warn(ans.toString());
			return ans;
		}
		
		try{
			
			st = this.tsv.updateOrdPnS(can, ans); // canamt set here
			
			if(ComStatus.PCancelStatus.SUCCESS!=st) {
				ans.setStatus(st);
				publog.warn(ans.toString());
				return ans;
			}
			
			ans.setStatus(st);
			publog.warn(ans.toString());
			return ans; // success return
		}
		catch(CapiException e) {
			ans.setStatus(ComStatus.PCancelStatus.valueOf(e.getMessage()));
			publog.warn(ans.toString());
			return ans;
		}
		catch(Exception e) {			
			ans.setStatus(ComStatus.PCancelStatus.UNKNOWN);
			publog.warn(ans.toString());
			return ans;
		}
		
	}
	
}
