package com.blackjade.subscriber.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blackjade.subscriber.apis.CQueryOwnPage;
import com.blackjade.subscriber.apis.CQueryOwnPageAns;
import com.blackjade.subscriber.apis.CQueryPnSOrder;
import com.blackjade.subscriber.apis.CQueryPnSOrderAns;
import com.blackjade.subscriber.apis.CQueryPnSPage;
import com.blackjade.subscriber.apis.CQueryPnSPageAns;
import com.blackjade.subscriber.apis.ComStatus;
import com.blackjade.subscriber.apis.ComStatus.QueryOwnStatus;
import com.blackjade.subscriber.apis.ComStatus.QueryPnSOrdStatus;
import com.blackjade.subscriber.apis.ComStatus.QueryPnSStatus;
import com.blackjade.subscriber.dao.OrdBookDao;
import com.blackjade.subscriber.dao.PubBookDao;
import com.blackjade.subscriber.domain.OrdBookRow;
import com.blackjade.subscriber.domain.OwnBookRow;
import com.blackjade.subscriber.domain.PubBookRow;

@RestController
public class SubController {
	
	@Autowired
	private PubBookDao pubbook;
	
	@Autowired
	private OrdBookDao ordbook;
	
	
	
	@RequestMapping(value = "/market", method = RequestMethod.POST)
	@ResponseBody
	public CQueryPnSPageAns QueryPnSPage(@RequestBody CQueryPnSPage qpns) {		

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
		
		if(st!=ComStatus.QueryPnSStatus.SUCCESS) {
			ans.setStatus(st);
			return ans;
		}
		
		// select num and PubBookRows
		int totalnum = 0; 
		try {
			totalnum = this.pubbook.selectNumPns(qpns.getPnsgid(), qpns.getPnsid(), qpns.getSide());
			if(totalnum==0) {
				ans.setRecordsFiltered(totalnum);
				ans.setStatus(ComStatus.QueryPnSStatus.PNS_EMPTY);				
				return ans;
			}			
		}
		catch(Exception e) {			
			ans.setRecordsFiltered(totalnum);
			ans.setStatus(ComStatus.QueryPnSStatus.PNS_DB_MISS);
			return ans;
		}
		
		List<PubBookRow> elist = null;
		
		try {
			elist = this.pubbook.selectPubBookRow(qpns.getPnsgid(), qpns.getPnsid(), qpns.getSide(), qpns.getStart());
			if(elist==null) {
				ans.setStatus(ComStatus.QueryPnSStatus.PNS_EMPTY);
				return ans;
			}
		}
		catch(Exception e) {
			ans.setStatus(ComStatus.QueryPnSStatus.PNS_DB_MISS);
			return ans;
		}
		
		ans.setData(elist);
		ans.setRecordsFiltered(totalnum);
		ans.setStatus(ComStatus.QueryPnSStatus.SUCCESS);
		return ans;
	}

	@RequestMapping(value = "/ownpns", method = RequestMethod.POST)
	@ResponseBody
	public CQueryOwnPageAns QueryOwnPage(@RequestBody CQueryOwnPage qpns) {		
		
		QueryOwnStatus st = qpns.reviewData();
		
		// construct ans
		CQueryOwnPageAns ans = new CQueryOwnPageAns(qpns.getRequestid());
		ans.setClientid(qpns.getClientid());
		ans.setPnsgid(qpns.getPnsgid());
		ans.setPnsid(qpns.getPnsid());
		ans.setSide(qpns.getSide());
		ans.setStart(qpns.getStart());
		ans.setLength(qpns.getLength());
		
		if(st!=ComStatus.QueryOwnStatus.SUCCESS) {			
			ans.setStatus(st);
			return ans;
		}
		
		// get number
		int totalnum = 0;
		try {
			totalnum = this.pubbook.selectOwnNumPns(qpns.getClientid(), qpns.getPnsgid(), qpns.getPnsid(), qpns.getSide());
			if(totalnum==0) {
				ans.setRecordsFiltered(totalnum);		
				ans.setStatus(ComStatus.QueryOwnStatus.PNS_EMPTY);
				return ans;
			}
		}
		catch(Exception e) {
			ans.setRecordsFiltered(totalnum);		
			ans.setStatus(ComStatus.QueryOwnStatus.PNS_DB_MISS);
			return ans;
		}
		
		// getlist
		List<OwnBookRow> elist=null;
		try {
			elist = this.pubbook.selectOwnBookRow(qpns.getClientid(), qpns.getPnsgid(), qpns.getPnsid(), qpns.getSide(), qpns.getStart());
			if(elist==null) {
				ans.setStatus(ComStatus.QueryOwnStatus.PNS_DB_MISS);
				return ans;
			}			
		}
		catch(Exception e) {
			ans.setStatus(ComStatus.QueryOwnStatus.PNS_DB_MISS);
			return ans;
		}

		// set list and num then return 
		ans.setRecordsFiltered(totalnum);
		ans.setData(elist);
		ans.setStatus(ComStatus.QueryOwnStatus.SUCCESS);
		return ans;
		
	}
	
	@RequestMapping(value = "/pnsorder", method = RequestMethod.POST)
	@ResponseBody
	public CQueryPnSOrderAns QueryPnSOrder(@RequestBody CQueryPnSOrder qpns) {

		// check input data			
		QueryPnSOrdStatus st = qpns.reviewData();
		
		// construct ans
		CQueryPnSOrderAns ans = new CQueryPnSOrderAns(qpns.getRequestid());
		ans.setClientid(qpns.getClientid());		
		ans.setPnsgid(qpns.getPnsgid());   
		ans.setPnsid(qpns.getPnsid());
		
		ans.setSide(qpns.getSide());
		
		
		if(st!=ComStatus.QueryPnSOrdStatus.SUCCESS) {
			ans.setStatus(st);
			return ans;
		}
				
		ans.setStatus(ComStatus.QueryPnSOrdStatus.SUCCESS);
		
		// query numbers			
		//<-->//
		int totalnum = 0;
		char side='S';
		if(qpns.getSide()=='B') {
			side = 'S';
		}else {
			if(qpns.getSide()=='S') {
				side = 'B';
			}else {
				// there isn't a need if reviewdata works
				ans.setStatus(ComStatus.QueryPnSOrdStatus.UNKNOWN);
				return ans;
			}
		}
		
		try {
			totalnum=this.ordbook.selectNumPnsOrder(qpns.getPnsoid().toString(), qpns.getClientid(), qpns.getPnsgid(), qpns.getPnsid(), side);
			if(totalnum==0) {
				ans.setStatus(ComStatus.QueryPnSOrdStatus.ORD_DB_EMPTY);
				return  ans;
			}
		}
		catch(Exception e) {
			ans.setStatus(ComStatus.QueryPnSOrdStatus.ORD_DB_MISS);
			return  ans;
		}
		
		// query lists
		List<OrdBookRow> elist = null;
		
		try {
			elist=this.ordbook.selectOrdBookRow(qpns.getClientid(), qpns.getPnsgid(), qpns.getPnsid(), side, qpns.getStart());
			if((elist==null)||(elist.isEmpty())) {
				ans.setStatus(ComStatus.QueryPnSOrdStatus.ORD_DB_MISS);
				return ans;
			}
		}
		catch(Exception e)
		{
			ans.setStatus(ComStatus.QueryPnSOrdStatus.ORD_DB_MISS);
			return ans;
		}
		
		ans.setRecordsFiltered(totalnum);// not in use		
		ans.setData(elist);
		ans.setLength(10);// always as it is
		return ans;
	}
		
}	
	
	/*
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
	*/
	
	/*
		
	@RequestMapping(value = "/ownpnstop", method = RequestMethod.POST)
	@ResponseBody
	public CQueryOwnTopPageAns QueryOwnTopPage(@RequestBody CQueryOwnTopPage qpns) {		
		
		QueryOwnTopStatus st = qpns.reviewData();
		// construct ans
		CQueryOwnTopPageAns ans = new CQueryOwnTopPageAns(qpns.getRequestid());
		ans.setClientid(qpns.getClientid());
		ans.setPnsgid(qpns.getPnsgid());
		ans.setPnsid(qpns.getPnsid());
		ans.setSide(qpns.getSide());
						
		if(st!=ComStatus.QueryOwnTopStatus.SUCCESS) {
			ans.setStatus(st);
			return ans;
		}
		
		int totalnum = 0;
		try {
			totalnum = this.pubbook.selectOwnNumPns(qpns.getClientid(), qpns.getPnsgid(), qpns.getPnsid(), qpns.getSide());
		}
		catch(Exception e) {
			
			
		}
		
		ans.setTotalnum(totalnum);
		ans.setStatus(ComStatus.QueryOwnTopStatus.SUCCESS);
		return ans;
	}
	
	@RequestMapping(value = "/ownpnsnext", method = RequestMethod.POST)
	@ResponseBody
	public CQueryOwnTopPageAns QueryOwnNextPage(@RequestBody CQueryOwnTopPage qpns) {		
		
		QueryOwnTopStatus st = qpns.reviewData();
		
		// construct ans
		CQueryOwnTopPageAns ans = new CQueryOwnTopPageAns(qpns.getRequestid());
		
		if(st!=ComStatus.QueryOwnTopStatus.SUCCESS) {
			ans.setStatus(st);
			return ans;
		}		
		return ans;
	}
	
	*/
	


