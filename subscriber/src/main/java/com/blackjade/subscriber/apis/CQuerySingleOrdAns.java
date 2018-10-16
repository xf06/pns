package com.blackjade.subscriber.apis;

import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryOwnOrdStatus;
import com.blackjade.subscriber.domain.OwnSingleOrdRow;

public class CQuerySingleOrdAns {

	private String messageid;
	private UUID requestid;
	private int clientid;
	private int pnsgid;
	private int pnsid;
	private UUID oid;
	private QueryOwnOrdStatus status;
	private OwnSingleOrdRow ordrow;
	
	public CQuerySingleOrdAns(UUID requestid){
		this.messageid = "6032";
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

	public UUID getOid() {
		return oid;
	}

	public void setOid(UUID oid) {
		this.oid = oid;
	}

	public OwnSingleOrdRow getOrdrow() {
		return ordrow;
	}

	public void setOrdrow(OwnSingleOrdRow ordrow) {
		this.ordrow = ordrow;
	}

	public QueryOwnOrdStatus getStatus() {
		return status;
	}

	public void setStatus(QueryOwnOrdStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CQuerySingleOrdAns [messageid=" + messageid + ", requestid=" + requestid + ", clientid=" + clientid
				+ ", pnsgid=" + pnsgid + ", pnsid=" + pnsid + ", oid=" + oid + ", ordrow=" + ordrow + ", status="
				+ status + "]";
	}

}
