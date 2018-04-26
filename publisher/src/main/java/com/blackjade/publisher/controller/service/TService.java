package com.blackjade.publisher.controller.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.blackjade.publisher.apis.CDeal;
import com.blackjade.publisher.apis.ComStatus;
import com.blackjade.publisher.apis.ComStatus.DealStatus;
import com.blackjade.publisher.dao.OrderDao;
import com.blackjade.publisher.dao.PnSDao;
import com.blackjade.publisher.domain.PnSRow;

@Transactional
@Component
public class TService {

	@Autowired
	private OrderDao ord;

	@Autowired
	private PnSDao pns;
	
	public DealStatus updatePnS(CDeal deal) {
		
		DealStatus st = ComStatus.DealStatus.SUCCESS;
		
		// ## select
		PnSRow pnsrow = null;
		try {
			pnsrow = this.pns.selectPnSRowOid(deal.getPnsoid().toString());
		}finally {
		// if no row found
			if(pnsrow==null)
				return ComStatus.DealStatus.PNS_MISS_MATCH;
		}
		
		// check input
		if(!pnsrow.getOid().equals(deal.getPnsoid().toString()))
			return ComStatus.DealStatus.PNS_MISS_MATCH;
		if(pnsrow.getPnsid()!=deal.getPnsid())
			return ComStatus.DealStatus.PNS_MISS_MATCH;		
		if(pnsrow.getPnsgid()!=deal.getPnsgid())
			return ComStatus.DealStatus.PNS_MISS_MATCH;	
		if(pnsrow.getPoid()!=deal.getPoid())
			return ComStatus.DealStatus.PNS_MISS_MATCH;
		if(pnsrow.getSide()==deal.getSide()) //B<->S //S<->B // side checking //need some work here
			return ComStatus.DealStatus.PNS_MISS_MATCH;
		
		// check status
		ComStatus.PnSStatus ps = ComStatus.PnSStatus.UNKNOWN;
		
		try {
			ps = ComStatus.PnSStatus.valueOf(pnsrow.getStatus());			
		}catch(Exception e){
			return  ComStatus.DealStatus.PNS_STATUS_MESS;
		} 
		
		// if final status
		if(ps == ComStatus.PnSStatus.FULL_TRADED)
			return ComStatus.DealStatus.PNS_STATUS_FINAL;
		if(ps == ComStatus.PnSStatus.CANCELLED) 
			return ComStatus.DealStatus.PNS_STATUS_FINAL;
		if(ps == ComStatus.PnSStatus.HALF_CANCELLED) 
			return ComStatus.DealStatus.PNS_STATUS_FINAL;
		// if locked status
		if(ps == ComStatus.PnSStatus.FULL_LOCKED) 
			return ComStatus.DealStatus.PNS_STATUS_LOCKED;
		// if still unknown
		if(ps == ComStatus.PnSStatus.UNKNOWN)
			return ComStatus.DealStatus.PNS_STATUS_MESS;
		
		
		// check data corruption
		long quant = pnsrow.getQuant();
		long margin = pnsrow.getMargin();
		long traded = pnsrow.getTraded();
		long net = pnsrow.getNet();

		if(quant!=margin+traded+net)
			return ComStatus.DealStatus.PNS_DATA_MESS;// should send alarm to monitor as well
				
		// pns status are not final {#PUBLISHED, #HALF_TRADED}		   
		if(ps==ComStatus.PnSStatus.PUBLISHED)
		{
			
		}
			
		
		
		
		
		
		private String oid;		private UUID pnsoid; // pns order id

		private long pnsid;		private int pnsid;
		private long pnsgid;	private int pnsgid;
		private long poid;		private int poid; // product owner id
		private long price;XXX
		private long quant;XXX
		// total
		private String status;
		private long traded;
		private long margin;
		private long net;	
				
		// -----------------------------------

		private int clientid;
		private char side;
		
		private long price;XXX
		private int quant;XXX
		
		// ## update
		
		this.pns.updatePnSDeal()
		
		return st;
	}
	
	public static String getNextPnSStatus() {
		
		return 0;
	}
	
	
}
