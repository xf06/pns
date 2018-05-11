package com.blackjade.subscriber.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blackjade.subscriber.apis.CQueryOwnTopPage;
import com.blackjade.subscriber.apis.CQueryOwnTopPageAns;
import com.blackjade.subscriber.apis.CQueryPnSNextPage;
import com.blackjade.subscriber.apis.CQueryPnSNextPageAns;
import com.blackjade.subscriber.apis.CQueryPnSTopPage;
import com.blackjade.subscriber.apis.CQueryPnSTopPageAns;
import com.blackjade.subscriber.apis.ComStatus;
import com.blackjade.subscriber.apis.ComStatus.QueryOwnTopStatus;
import com.blackjade.subscriber.apis.ComStatus.QueryPnSNextStatus;
import com.blackjade.subscriber.apis.ComStatus.QueryPnSTopStatus;
import com.blackjade.subscriber.dao.PubBookDao;
import com.blackjade.subscriber.domain.PubBookRow;

@RestController
public class SubController {
	
	@Autowired
	private PubBookDao pubbook;
	
	@RequestMapping(value = "/markettop", method = RequestMethod.POST)
	@ResponseBody
	public CQueryPnSTopPageAns QueryPnSTopPage(@RequestBody CQueryPnSTopPage qpns) {		

		// check input data
		QueryPnSTopStatus st = qpns.reviewData();
		
		// construct ans
		CQueryPnSTopPageAns ans = new CQueryPnSTopPageAns(qpns.getRequestid());
		ans.setClientid(qpns.getClientid());
		ans.setSide(qpns.getSide());
		ans.setPnsgid(qpns.getPnsgid());
		ans.setPnsid(qpns.getPnsid());		
		//ans.setTotalnum(0);
				
		if(st!=ComStatus.QueryPnSTopStatus.SUCCESS) {
			ans.setStatus(st);
			return ans;
		}
		
		// select num and PubBookRows
		int totalnum = 0; 
		try {
			totalnum = this.pubbook.selectNumPns(qpns.getPnsgid(), qpns.getPnsid(), qpns.getSide());
			if(totalnum==0) {
				ans.setTotalnum(totalnum);
				ans.setStatus(ComStatus.QueryPnSTopStatus.PNS_EMPTY);				
				return ans;
			}			
		}
		catch(Exception e) {			
			ans.setTotalnum(totalnum);
			ans.setStatus(ComStatus.QueryPnSTopStatus.PNS_DB_MISS);
			return ans;
		}
		
		// set totalnum and list
		ans.setTotalnum(totalnum);
		List<PubBookRow> elist = null; // java list container
		
		try {
			// top page (num=0)
			elist  = this.pubbook.selectPubBookRow(qpns.getPnsgid(),qpns.getPnsid(),qpns.getSide(), 0); 
			if(elist==null) {
				ans.setTotalnum(totalnum);
				ans.setStatus(ComStatus.QueryPnSTopStatus.PNS_DB_MISS);
				return ans;
			}
		}
		catch(Exception e) {
			ans.setTotalnum(0);
			ans.setStatus(ComStatus.QueryPnSTopStatus.PNS_DB_MISS);
			return ans;
		}
		
		if(elist.isEmpty()) {
			ans.setTotalnum(0);
			ans.setStatus(ComStatus.QueryPnSTopStatus.PNS_DB_MISS);
			return ans;
		}
		
		ans.setStatus(ComStatus.QueryPnSTopStatus.SUCCESS);
		ans.setList(elist);		
		return ans;
	}
	
	@RequestMapping(value = "/marketnext", method = RequestMethod.POST)
	@ResponseBody
	public CQueryPnSNextPageAns QueryPnSNextPage(@RequestBody CQueryPnSNextPage qpns) {		

		// check input msg 
		QueryPnSNextStatus st = qpns.reviewData();
		
		// construct ans
		CQueryPnSNextPageAns ans = new CQueryPnSNextPageAns(qpns.getRequestid());
		
		ans.setClientid(qpns.getClientid());
		ans.setPnsgid(qpns.getPnsgid());
		ans.setPnsid(qpns.getPnsid());
		ans.setSide(qpns.getSide());
		ans.setIndex(qpns.getIndex());
		
		if(st!=ComStatus.QueryPnSNextStatus.SUCCESS) {
			ans.setStatus(st);
			return ans;
		}
						
		List<PubBookRow> elist = null;
		
		try {
			elist = this.pubbook.selectPubBookRow(qpns.getPnsgid(), qpns.getPnsid(), qpns.getSide(), qpns.getIndex());
			if(elist==null) {
				ans.setStatus(ComStatus.QueryPnSNextStatus.PNS_EMPTY);
				return ans;				
			}
		}
		catch(Exception e) {
			ans.setStatus(ComStatus.QueryPnSNextStatus.PNS_DB_MISS);
			return ans;
		}
		
		ans.setList(elist);
		ans.setStatus(ComStatus.QueryPnSNextStatus.SUCCESS);
		return ans;
	}
	
	@RequestMapping(value = "/ownords", method = RequestMethod.POST)
	@ResponseBody
	public CQueryOwnTopPageAns QueryOwnTopPage(@RequestBody CQueryOwnTopPage qpns) {		
		
		QueryOwnTopStatus st = qpns.reviewData();
		// construct ans
		CQueryOwnTopPageAns ans = new CQueryOwnTopPageAns(qpns.getRequestid());
		
		if(st!=ComStatus.QueryOwnTopStatus.SUCCESS) {
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

