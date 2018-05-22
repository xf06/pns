package com.blackjade.subscriber.apis;

import java.util.List;
import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryAllOrdRecvStatus;
import com.blackjade.subscriber.domain.AllOrdSentRow;

// 0x602E	{requestid, clientid, pnsid, pnsgid, status, list[]}

public class CQueryAllOrdRecvAns {

	private String messageid;
	private UUID requestid;
	private int clientid; 	// this is poid
	private int pnsgid;		//
	private int pnsid;		//
	private int start;		//
	private int length; 	// always 10

	private QueryAllOrdRecvStatus status;
	private int recordsFiltered;
	private List<AllOrdSentRow> data;

	public CQueryAllOrdRecvAns(UUID requestid) {
		this.requestid = requestid;
		this.messageid = "602E";
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

	public QueryAllOrdRecvStatus getStatus() {
		return status;
	}

	public void setStatus(QueryAllOrdRecvStatus status) {
		this.status = status;
	}

	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public List<AllOrdSentRow> getData() {
		return data;
	}

	public void setData(List<AllOrdSentRow> data) {
		this.data = data;
	}
}



