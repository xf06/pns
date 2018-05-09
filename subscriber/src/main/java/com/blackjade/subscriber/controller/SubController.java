package com.blackjade.subscriber.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blackjade.subscriber.apis.CQueryPnSNextPage;
import com.blackjade.subscriber.apis.CQueryPnSNextPageAns;
import com.blackjade.subscriber.apis.CQueryPnSTopPage;
import com.blackjade.subscriber.apis.CQueryPnSTopPageAns;
import com.blackjade.subscriber.apis.ComStatus;
import com.blackjade.subscriber.apis.ComStatus.QueryPnSNextStatus;
import com.blackjade.subscriber.apis.ComStatus.QueryPnSTopStatus;
import com.blackjade.subscriber.dao.PubBookDao;
import com.blackjade.subscriber.domain.PubBookRow;

@RestController
public class SubController {
	
	@Autowired
	private PubBookDao pubbook;
	
	//@Autowired
	//private ;
	@RequestMapping(value = "/markettop", method = RequestMethod.POST)
	@ResponseBody
	public CQueryPnSTopPageAns QueryPnSTopPage(@RequestBody CQueryPnSTopPage qpns) {		
		// check input data
		QueryPnSTopStatus st = qpns.reviewData();
		
		// construct ans
		CQueryPnSTopPageAns ans = new CQueryPnSTopPageAns(qpns.getRequestid());
				
		ans.setSide(qpns.getSide());
		ans.setClientid(qpns.getClientid());
		ans.setPnsgid(qpns.getPnsgid());
		ans.setPnsid(qpns.getPnsid());		
		//ans.setTotalnum(0);
				
		if(st!=ComStatus.QueryPnSTopStatus.SUCCESS) {
			ans.setStatus(st);
			return ans;
		}
				
		//--------------------------------
		// if everything OK
		int totalnum = 0; 
		try {
			totalnum = this.pubbook.selectNumPns(qpns.getPnsgid(), qpns.getPnsid(), qpns.getSide()); //
			if(totalnum==0) {
				ans.setTotalnum(totalnum);
				ans.setStatus(ComStatus.QueryPnSTopStatus.PNS_DB_MISS);
				return ans;
			}
		}
		catch(Exception e) {			
			ans.setTotalnum(totalnum);
			ans.setStatus(ComStatus.QueryPnSTopStatus.PNS_DB_MISS);
			return ans;
		}
		
		// there are list
		ans.setTotalnum(totalnum);
		// get list
		List<PubBookRow> elist  = this.pubbook.selectPubBookRow(qpns.getPnsgid(),qpns.getPnsid(),qpns.getSide(), 0); // top page		
		
		//--------------------------------
		
		
		return ans;
	}
	
	@RequestMapping(value = "/marketnext", method = RequestMethod.POST)
	@ResponseBody
	public CQueryPnSNextPageAns QueryPnSNextPage(@RequestBody CQueryPnSNextPage qpns) {		
		
		QueryPnSNextStatus st = qpns.reviewData();
		// construct ans
		CQueryPnSNextPageAns ans = new CQueryPnSTopPageAns(qpns.getRequestid());
		
		if(st!=ComStatus.QueryPnSNextStatus.SUCCESS) {
			ans.setStatus(st);
			return ans;
		}
		
		return ans;
	}
	
	@RequestMapping(value = "/ownords", method = RequestMethod.POST)
	@ResponseBody
	public CQueryPnSTopPageAns QueryOwnTopPage(@RequestBody CQueryPnSTopPage qpns) {		
		
		QueryPnSTopStatus st = qpns.reviewData();
		// construct ans
		CQueryPnSTopPageAns ans = new CQueryPnSTopPageAns(qpns.getRequestid());
		
		if(st!=ComStatus.QueryPnSTopStatus.SUCCESS) {
			ans.setStatus(st);
			return ans;
		}
		
		return ans;
	}
	
	@RequestMapping(value = "/ownpns", method = RequestMethod.POST)
	@ResponseBody
	public CQueryPnSTopPageAns QueryOwnNextPage(@RequestBody CQueryPnSTopPage qpns) {		
		
		QueryPnSTopStatus st = qpns.reviewData();
		// construct ans
		CQueryPnSTopPageAns ans = new CQueryPnSTopPageAns(qpns.getRequestid());
		
		if(st!=ComStatus.QueryPnSTopStatus.SUCCESS) {
			ans.setStatus(st);
			return ans;
		}		
		return ans;
	}
	
}

