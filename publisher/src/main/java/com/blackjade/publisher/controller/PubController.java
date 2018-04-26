package com.blackjade.publisher.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blackjade.publisher.apis.CDeal;
import com.blackjade.publisher.apis.CDealAns;
import com.blackjade.publisher.apis.CPublish;
import com.blackjade.publisher.apis.CPublishAns;
import com.blackjade.publisher.apis.ComStatus;
import com.blackjade.publisher.apis.ComStatus.DealStatus;
import com.blackjade.publisher.apis.ComStatus.PublishStatus;
import com.blackjade.publisher.dao.OrderDao;
import com.blackjade.publisher.dao.PnSDao;
import com.blackjade.publisher.domain.OrderRow;
import com.blackjade.publisher.domain.PnSRow;

@RestController
public class PubController {

	@Autowired
	private OrderDao ord;

	@Autowired
	private PnSDao pns;

	// Publish
	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	@ResponseBody
	public CPublishAns CPublish(@RequestBody CPublish pub) {

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
			return ans;
		}

		// SQL return code
		int retcode = 0;

		// save into database
		OrderRow ordrow = new OrderRow();
		ordrow.setTimestamp(System.currentTimeMillis());
		ordrow.setOid(oid.toString());
		ordrow.setCid(pub.getClientid());
		ordrow.setType('P');
		ordrow.setSide(pub.getSide());
		ordrow.setPnsoid(oid.toString());
		ordrow.setPoid(pub.getClientid());
		ordrow.setPnsid(pub.getPnsid());
		ordrow.setPnsgid(pub.getPnsgid());
		ordrow.setPrice(pub.getPrice());
		ordrow.setQuant(pub.getQuant());
		ordrow.setMin(pub.getMin());
		ordrow.setMax(pub.getMax());
		ordrow.setForm("normal");

		try {
			retcode = this.ord.insertOrder(ordrow);
		} finally {
			if(retcode==0) {
				ans.setStatus(ComStatus.PublishStatus.DATABASE_ERR);
				return ans;
			}
		}
		
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
		
		// save 
		try {
			retcode = this.pns.insertPnS(pnsrow);
		} finally {
			if(retcode==0) {
				ans.setStatus(ComStatus.PublishStatus.DATABASE_ERR);
				return ans;
			}
		}
			
		ans.setStatus(ComStatus.PublishStatus.SUCCESS);
		return ans;
	}

	
	// Deal // price checking need to be done
	@RequestMapping(value = "/deal", method = RequestMethod.POST)
	@ResponseBody
	public CDealAns CDeal(@RequestBody CDeal deal) {
		// check input errors
		DealStatus st = deal.reviewData();
		UUID oid = UUID.randomUUID();

		// construct ans obj
		CDealAns ans = new CDealAns(deal.getRequestid());
		ans.setClientid(deal.getClientid());
		ans.setOid(oid);
		
		ans.setPnsoid(deal.getPnsoid());
		ans.setPoid(deal.getPoid());
		
		ans.setPnsid(deal.getPnsid());
		ans.setPnsgid(deal.getPnsgid());
		
		ans.setPrice(deal.getPrice());
		ans.setQuant(deal.getQuant());
		ans.setSide(deal.getSide());
				
		
		if(st!=ComStatus.DealStatus.SUCCESS) {
			ans.setStatus(st);
			return ans;
		}
		
		// saving into orders 
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
		ordrow.setMin(0);
		ordrow.setMax(0);
		ordrow.setForm("normal");
				
		this.ord.insertOrder(ordrow);
		//-------- transaction begin ------------
		// check if logics are all correct
		
			// check 
			
			// check
		
		
		// update PnS database 
		//-------- transaction commit -----------
		
		
		// no error return success
		
		return ans;
	}
	
	// Paid
	
	// PayConfirm
	
	// Cancel

}
