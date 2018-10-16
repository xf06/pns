package com.blackjade.subscriber.domain;

public class AllOrdRecvRow {

	private long timestamp;
	private String oid;
	private int cid;
	private String cname;
	private String pnsoid;
	private char side;
	private long price;
	private long quant;
	private String status;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getPnsoid() {
		return pnsoid;
	}

	public void setPnsoid(String pnsoid) {
		this.pnsoid = pnsoid;
	}

	public char getSide() {
		return side;
	}

	public void setSide(char side) {
		this.side = side;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public long getQuant() {
		return quant;
	}

	public void setQuant(long quant) {
		this.quant = quant;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
