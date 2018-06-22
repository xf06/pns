package com.blackjade.subscriber.apis;

import java.util.List;
import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryOwnAccStatus;
import com.blackjade.subscriber.domain.OwnAccRow;

//cQueryOwnAccTopPageAns	0x5002	{requestid, clientid, pnsid, pnsgid, status, list[]}	HTTP	

public class CQueryOwnAccPageAns {

	private String messageid;
	private UUID requestid;
	private int clientid;
	private int start;
	private int length;
	private QueryOwnAccStatus status;
	private int recordsFiltered;
	private List<OwnAccRow> data;

	public CQueryOwnAccPageAns(UUID requestid) {
		this.messageid = "5002";
		this.requestid = requestid;
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

	public QueryOwnAccStatus getStatus() {
		return status;
	}

	public void setStatus(QueryOwnAccStatus status) {
		this.status = status;
	}

	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public List<OwnAccRow> getData() {
		return data;
	}

	public void setData(List<OwnAccRow> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CQueryOwnAccPageAns [messageid=" + messageid + ", requestid=" + requestid + ", clientid=" + clientid
				+ ", start=" + start + ", length=" + length + ", status=" + status + ", recordsFiltered="
				+ recordsFiltered + ", data=" + data + "]";
	}

}
