package com.blackjade.subscriber.apis;

import java.util.List;
import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryPnSStatus;
import com.blackjade.subscriber.domain.PubBookRow;

// QueryOwnPageAns	0x6024	{requestid, clientid, pnsid, pnsgid, status, list[]}	HTTP

public class CQueryOwnPageAns {
	
	private String messageid;
	private UUID requestid;
	private int clientid;

	private int pnsid;
	private int pnsgid;
	private char side;
	private int start;
	private int length; // not in use, it must be 10

	private QueryPnSStatus status;
	private int recordsTotal;
	private List<PubBookRow> data;

	public CQueryOwnPageAns(UUID requestid) {
		this.messageid = "6022";
		this.requestid = requestid;
	//	this.data =  new ArrayList<PubBookRow>();
	}
	
	
	
	
	
	
}
