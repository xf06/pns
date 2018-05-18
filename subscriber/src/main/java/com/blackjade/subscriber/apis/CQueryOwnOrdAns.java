package com.blackjade.subscriber.apis;

import java.util.UUID;
//cQueryOwnOrdAns	0x6028	{requestid, oid, cid, pnsoid, poid,pnsid, pnsgid.side, status}

import com.blackjade.subscriber.apis.ComStatus.QueryOwnOrdStatus;
import com.blackjade.subscriber.domain.OrdBookRow;

/*	private long timestamp;
private String oid;
private long cid;
private long price;
private long quant;
private String status;
*/
public class CQueryOwnOrdAns {

	private String messageid;
	private UUID requestid;
	
	private UUID pnsoid;
	private int poid;
	private int pnsgid;
	private int pnsid;
	private char side;
	
	private OrdBookRow ord;
	private QueryOwnOrdStatus status;

	public CQueryOwnOrdAns(UUID requestid) {
		this.messageid = "6028";
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

	public UUID getPnsoid() {
		return pnsoid;
	}

	public void setPnsoid(UUID pnsoid) {
		this.pnsoid = pnsoid;
	}

	public int getPoid() {
		return poid;
	}

	public void setPoid(int poid) {
		this.poid = poid;
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

	public char getSide() {
		return side;
	}

	public void setSide(char side) {
		this.side = side;
	}

	public QueryOwnOrdStatus getStatus() {
		return status;
	}

	public void setStatus(QueryOwnOrdStatus status) {
		this.status = status;
	}

	public OrdBookRow getOrd() {
		return ord;
	}

	public void setOrd(OrdBookRow ord) {
		this.ord = ord;
	}
	
	
}
