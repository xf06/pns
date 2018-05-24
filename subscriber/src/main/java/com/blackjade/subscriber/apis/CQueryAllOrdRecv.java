package com.blackjade.subscriber.apis;

import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryAllOrdRecvStatus;

//	cQueryAllOrdRecv	0x602D	{requestid, oid, cid, pnsoid, poid,pnsid, pnsgid}	HTTP
public class CQueryAllOrdRecv {

	private String messageid;
	private UUID requestid;
	private int clientid; // this is poid
	private int pnsgid;
	private int pnsid;
	private int start;
	private int length; // always 10

	public QueryAllOrdRecvStatus reviewData() {
		
		if (!("602D").equals(this.messageid))
			return ComStatus.QueryAllOrdRecvStatus.INMSG_ERR;

		return ComStatus.QueryAllOrdRecvStatus.SUCCESS;
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


