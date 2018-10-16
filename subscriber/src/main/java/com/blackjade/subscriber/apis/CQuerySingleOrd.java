package com.blackjade.subscriber.apis;

import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryOwnOrdStatus;


//cQuerySingleOrd	0x6031	{requestid, clientid, pnsgid, pnsid, oid}	HTTP

public class CQuerySingleOrd {

	private String messageid;
	private UUID requestid;
	private int clientid;
	private int pnsgid;
	private int pnsid;
	private UUID oid;

	public QueryOwnOrdStatus reviewData() {
		
		if (!this.messageid.equals("6031"))
			return ComStatus.QueryOwnOrdStatus.WRONG_MSGID;
		
		if((this.pnsgid<0)||(this.pnsid<0))
			return ComStatus.QueryOwnOrdStatus.INMSG_ERR;
		
		return ComStatus.QueryOwnOrdStatus.SUCCESS;
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

	public int getPnsgid() {
		return pnsgid;
	}

	public void setPnsgid(int pnsgid) {
		this.pnsgid = pnsgid;
	}

	public int getPnsid() {
		return pnsid;
	}

	public void setPnsid(int pnsid) {
		this.pnsid = pnsid;
	}

	public UUID getOid() {
		return oid;
	}

	public void setOid(UUID oid) {
		this.oid = oid;
	}

	@Override
	public String toString() {
		return "CQuerySingleOrd [messageid=" + messageid + ", requestid=" + requestid + ", clientid=" + clientid
				+ ", pnsgid=" + pnsgid + ", pnsid=" + pnsid + ", oid=" + oid + "]";
	}	
	
}
