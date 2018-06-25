package com.blackjade.publisher.apis;

import java.util.UUID;

import com.blackjade.publisher.apis.ComStatus.CancelStatus;

// 0x700F	{requestid, clientid, oid, side, pnsid, pnsgid, price, quant, status }

public class CCancelAns {
	private String messageid;
	private UUID requestid;
	private int clientid;
	private UUID oid;
	private char type; // D and P
	private char side;
	private UUID pnsoid; // pns order id
	private int poid; // product owner id
	private int pnsid;
	private int pnsgid;
	private long price;
	private int quant;
	private CancelStatus status;

	public CCancelAns() {
	}

	public CCancelAns(UUID requestid) {
		this.messageid = "700F";
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

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public char getSide() {
		return side;
	}

	public void setSide(char side) {
		this.side = side;
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

	public CancelStatus getStatus() {
		return status;
	}

	public void setStatus(CancelStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CCancelAns [messageid=" + messageid + ", requestid=" + requestid + ", clientid=" + clientid + ", oid="
				+ oid + ", type=" + type + ", side=" + side + ", pnsoid=" + pnsoid + ", poid=" + poid + ", pnsid="
				+ pnsid + ", pnsgid=" + pnsgid + ", price=" + price + ", quant=" + quant + ", status=" + status + "]";
	}

}
