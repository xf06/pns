package com.blackjade.publisher.apis;

import java.util.UUID;

import com.blackjade.publisher.apis.ComStatus.PayConfirmStatus;

//0x7008	{requestid, clientid, oid, side, pnsid, pnsgid, price, quant, status}

public class CPayConfirmAns {

	private String messageid;
	private UUID requestid;
	private int clientid;
	private UUID oid;
	private char side;
	private int pnsid;
	private int pnsgid;
	private long price;
	private int quant;
	private PayConfirmStatus status;

	public CPayConfirmAns() {}
	
	public CPayConfirmAns(UUID requestid) {
		this.messageid = "7008";
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

	public UUID getOid() {
		return oid;
	}

	public void setOid(UUID oid) {
		this.oid = oid;
	}

	public char getSide() {
		return side;
	}

	public void setSide(char side) {
		this.side = side;
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

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public int getQuant() {
		return quant;
	}

	public void setQuant(int quant) {
		this.quant = quant;
	}

	public PayConfirmStatus getStatus() {
		return status;
	}

	public void setStatus(PayConfirmStatus status) {
		this.status = status;
	}

}
