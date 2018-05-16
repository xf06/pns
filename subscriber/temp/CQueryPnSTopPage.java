package com.blackjade.subscriber.apis;

import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryPnSTopStatus;

//0x6005	{requestid, clientid, pnsid, pnsgid}
public class CQueryPnSTopPage {
	private String messageid;
	private UUID requestid;
	private int clientid;
	private int pnsid;
	private int pnsgid;
	private char side;

	public CQueryPnSTopPage(){
		this.messageid = "6005";
	}
	
	public QueryPnSTopStatus reviewData(){
		return ComStatus.QueryPnSTopStatus.SUCCESS; 
	}
		
	public String getMessageid() {
		return messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}

	public UUID getRequestid() {
		return requestid;
	}

	public void setRequestid(UUID requestid) {
		this.requestid = requestid;
	}

	public int getClientid() {
		return clientid;
	}

	public void setClientid(int clientid) {
		this.clientid = clientid;
	}

	public int getPnsid() {
		return pnsid;
	}

	public void setPnsid(int pnsid) {
		this.pnsid = pnsid;
	}

	public int getPnsgid() {
		return pnsgid;
	}

	public void setPnsgid(int pnsgid) {
		this.pnsgid = pnsgid;
	}

	public char getSide() {
		return side;
	}

	public void setSide(char side) {
		this.side = side;
	}

}
