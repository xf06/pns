package com.blackjade.subscriber.apis;

import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryOwnTopStatus;

//cQueryOwnTopPageAns	0x6002	{requestid, clientid, pnsid, pnsgid, status, list[]}

public class CQueryOwnTopPageAns {

	private String messageid;
	private UUID requestid;
	private int clientid;
	private char side;
	private int pnsid;
	private int pnsgid;
	private QueryOwnTopStatus status;
	private int totalnum;
	
	public char getSide() {
		return side;
	}

	public void setSide(char side) {
		this.side = side;
	}

	public int getTotalnum() {
		return totalnum;
	}

	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}

	private ComStatus.ComMember[] list;
	
	
	public CQueryOwnTopPageAns(UUID requestid) {
		this.messageid = "6002";
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

	public QueryOwnTopStatus getStatus() {
		return status;
	}

	public void setStatus(QueryOwnTopStatus status) {
		this.status = status;
	}

	public ComStatus.ComMember[] getList() {
		return list;
	}

	public void setList(ComStatus.ComMember[] list) {
		this.list = list;
	}

}
