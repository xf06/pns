package com.blackjade.subscriber.apis;

import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryAllOrdSentStatus;

//cQueryAllOrdSent	0x602B	{requestid, oid, cid, pnsoid, poid,pnsid, pnsgid}	HTTP

public class CQueryAllOrdSent {

	private String messageid;
	private UUID requestid;
	private int cid;
	private int pnsgid;
	private int pnsid;
	private int start;
	private int length; // always 10

	public CQueryAllOrdSent() {
		this.messageid = "602B";
	}

	public QueryAllOrdSentStatus reviewData() {
		if (!this.messageid.equals("602B"))
			return ComStatus.QueryAllOrdSentStatus.WRONG_MSGID;

		return ComStatus.QueryAllOrdSentStatus.SUCCESS;
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

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
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
