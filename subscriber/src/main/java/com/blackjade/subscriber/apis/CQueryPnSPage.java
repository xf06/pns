package com.blackjade.subscriber.apis;

import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryPnSStatus;

// QueryPnSPage	0x6021	{requestid, clientid, pnsid, pnsgid}

public class CQueryPnSPage {

	private String messageid;
	private UUID requestid;
	private int clientid;
	private int pnsid;
	private int pnsgid;
	private char side;
	private int start;
	private int length;// not in use it must be 10

	public CQueryPnSPage() {
		this.messageid = "6021";
	}

	public QueryPnSStatus reviewData() {
		return ComStatus.QueryPnSStatus.SUCCESS;
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

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
