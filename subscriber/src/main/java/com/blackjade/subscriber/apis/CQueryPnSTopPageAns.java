package com.blackjade.subscriber.apis;

import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryPnSTopStatus;

//0x6006	{requestid, clientid, pnsid, pnsgid, status, list[]}
public class CQueryPnSTopPageAns {

	private String messageid;
	private UUID requestid;
	private int clientid;
	private int pnsid;
	private int pnsgid;
	private QueryPnSTopStatus status;
	private ComStatus.ComMember[] list;

	public CQueryPnSTopPageAns(UUID requestid) {
		this.messageid = "6006";
		this.requestid = requestid;
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
	
	public QueryPnSTopStatus getStatus() {
		return status;
	}

	public void setStatus(QueryPnSTopStatus status) {
		this.status = status;
	}

	public ComStatus.ComMember[] getList() {
		return list;
	}

	public void setList(ComStatus.ComMember[] list) {
		this.list = list;
	}

}
