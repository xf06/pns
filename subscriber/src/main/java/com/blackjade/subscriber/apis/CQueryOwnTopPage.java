package com.blackjade.subscriber.apis;

import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryOwnTopStatus;

//cQueryOwnTopPage	0x6001	{requestid, clientid, pnsid, pnsgid}
public class CQueryOwnTopPage {

	private String messageid;
	private UUID requestid;
	private int clientid;
	private int pnsid;
	private int pnsgid;

	public CQueryOwnTopPage() {
		this.messageid = "6001";
	}

	public QueryOwnTopStatus reviewData() {
		return ComStatus.QueryOwnTopStatus.SUCCESS;
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

}
