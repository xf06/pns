package com.blackjade.subscriber.apis;

import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryOwnAccStatus;

//cQueryOwnAccTopPage	0x5001	{requestid, clientid, pnsid, pnsgid}	HTTP	element: margin, freemargin (balance calculate yourself)

public class CQueryOwnAccPage {

	private String messageid;
	private UUID requestid;
	private int clientid;
	private int start;
	private int length; // 20

	public QueryOwnAccStatus reviewData() {
		if (!this.messageid.equals("5001"))
			return ComStatus.QueryOwnAccStatus.WRONG_MSGID;

		return ComStatus.QueryOwnAccStatus.SUCCESS;
	}

	public UUID getRequestid() {
		return requestid;
	}

	public void setRequestid(UUID requestid) {
		this.requestid = requestid;
	}

	public String getMessageid() {
		return messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}

	public int getClientid() {
		return clientid;
	}

	public void setClientid(int clientid) {
		this.clientid = clientid;
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

	@Override
	public String toString() {
		return "CQueryOwnAccPage [messageid=" + messageid + ", requestid=" + requestid + ", clientid=" + clientid
				+ ", start=" + start + ", length=" + length + "]";
	}

}
