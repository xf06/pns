package com.blackjade.subscriber.apis;

import java.util.List;
import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryOwnStatus;
import com.blackjade.subscriber.domain.OwnBookRow;

// QueryOwnPageAns	0x6024	{requestid, clientid, pnsid, pnsgid, status, list[]}	HTTP

public class CQueryOwnPageAns {

	private String messageid;
	private UUID requestid;
	private int clientid;

	private int pnsid;
	private int pnsgid;
	private char side;
	private int start;
	private int length; // not in use, it must be 10

	private QueryOwnStatus status;
	private int recordsFiltered;
	private List<OwnBookRow> data;

	public CQueryOwnPageAns(UUID requestid) {
		this.messageid = "6024";
		this.requestid = requestid;
		// this.data = new ArrayList<PubBookRow>();
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

	public QueryOwnStatus getStatus() {
		return status;
	}

	public void setStatus(QueryOwnStatus status) {
		this.status = status;
	}

	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public List<OwnBookRow> getData() {
		return data;
	}

	public void setData(List<OwnBookRow> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CQueryOwnPageAns [messageid=" + messageid + ", requestid=" + requestid + ", clientid=" + clientid
				+ ", pnsid=" + pnsid + ", pnsgid=" + pnsgid + ", side=" + side + ", start=" + start + ", length="
				+ length + ", status=" + status + ", recordsFiltered=" + recordsFiltered + ", data=" + data + "]";
	}

}
