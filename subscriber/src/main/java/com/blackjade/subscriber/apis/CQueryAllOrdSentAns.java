package com.blackjade.subscriber.apis;

import java.util.List;
import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryAllOrdSentStatus;
import com.blackjade.subscriber.domain.AllOrdSentRow;

//cQueryAllOrdSentAns	0x602C	{requestid, clientid, pnsid, pnsgid, status, list[]}	HTTP

public class CQueryAllOrdSentAns {

	private String messageid;
	private UUID requestid;
	private int cid;
	private int pnsgid;
	private int pnsid;
	private QueryAllOrdSentStatus status;

	private int start;
	private int length;

	int recordsFiltered;
	List<AllOrdSentRow> data;

	public CQueryAllOrdSentAns(UUID requestid) {
		this.requestid = requestid;
		this.messageid = "602C";
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

	public QueryAllOrdSentStatus getStatus() {
		return status;
	}

	public void setStatus(QueryAllOrdSentStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CQueryAllOrdSentAns [messageid=" + messageid + ", requestid=" + requestid + ", cid=" + cid + ", pnsgid="
				+ pnsgid + ", pnsid=" + pnsid + ", status=" + status + ", start=" + start + ", length=" + length
				+ ", recordsFiltered=" + recordsFiltered + ", data=" + data + "]";
	}

	
}
