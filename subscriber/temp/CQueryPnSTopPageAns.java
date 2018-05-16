package com.blackjade.subscriber.apis;

import java.util.List;
import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryPnSTopStatus;
import com.blackjade.subscriber.domain.PubBookRow;

//0x6006	{requestid, clientid, pnsid, pnsgid, status, list[]}
public class CQueryPnSTopPageAns {

	private String messageid;
	private UUID requestid;
	private int clientid;
	private int pnsid;
	private int pnsgid;
	private char side;
	private QueryPnSTopStatus status;
	private int totalnum;
	
	//-><-// private ComStatus.ComMember[] list;
	
	private List<PubBookRow> list;
	
	
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

	public List<PubBookRow> getList() {
		return list;
	}

	public void setList(List<PubBookRow> list) {
		this.list = list;
	}

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

}
