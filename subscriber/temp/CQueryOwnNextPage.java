package com.blackjade.subscriber.apis;

import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryOwnNextStatus;

//0x6003	{requestid, clientid, pnsid, pnsgid, index}

public class CQueryOwnNextPage {
	private String messageid;
	private UUID requestid;
	private int clientid;
	private int pnsid;
	private int pnsgid;
	private int index;

	public CQueryOwnNextPage() {
		this.messageid = "6003";
	}

	public QueryOwnNextStatus reviewData() {
		return ComStatus.QueryOwnNextStatus.SUCCESS;
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
