package com.blackjade.subscriber.apis;

import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryPnSOrdStatus;

//cQueryPnSOrder	0x6025	{requestid, clientid, pnsid, pnsgid}	HTTP

public class CQueryPnSOrder {

	private String messageid;
	private UUID requestid;
	private int clientid;
	private UUID pnsoid;
	private int pnsid;
	private int pnsgid;
	private char side;
	private int start;
	private int length; // not in use it must be 10

	public QueryPnSOrdStatus reviewData() {
		if (!this.messageid.equals("6025"))
			return ComStatus.QueryPnSOrdStatus.INMSG_ERR;

		return ComStatus.QueryPnSOrdStatus.SUCCESS;
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

	public UUID getPnsoid() {
		return pnsoid;
	}

	public void setPnsoid(UUID pnsoid) {
		this.pnsoid = pnsoid;
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

	@Override
	public String toString() {
		return "CQueryPnSOrder [messageid=" + messageid + ", requestid=" + requestid + ", clientid=" + clientid
				+ ", pnsoid=" + pnsoid + ", pnsid=" + pnsid + ", pnsgid=" + pnsgid + ", side=" + side + ", start="
				+ start + ", length=" + length + "]";
	}

}
