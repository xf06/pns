package com.blackjade.subscriber.apis;

import java.util.UUID;

import com.blackjade.subscriber.apis.ComStatus.QueryOwnOrdStatus;

//cQueryOwnOrd	0x6027 {requestid,oid,cid,pnsoid,poid,pnsid,pnsgid,side}

public class CQueryOwnOrd {

	private String messageid;
	private UUID requestid;
	private UUID oid;
	private int cid;
	private UUID pnsoid;
	private int poid;
	private int pnsgid;
	private int pnsid;
	private char side;

	public CQueryOwnOrd() {
		this.messageid = "6027";
	}

	public QueryOwnOrdStatus reviewData() {
		if (!this.messageid.equals("6027"))
			return ComStatus.QueryOwnOrdStatus.WRONG_MSGID;

		return ComStatus.QueryOwnOrdStatus.SUCCESS;
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

	public UUID getOid() {
		return oid;
	}

	public void setOid(UUID oid) {
		this.oid = oid;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
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

}
