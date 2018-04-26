package com.blackjade.publisher.domain;

public class PnSRow {
	// id
	//private int id;
	private long time;
	private String oid;
	private long pnsid;
	private long pnsgid;
	private long poid;
	private char side;
	private long price;
	private long quant;
	// total
	private String status;
	private long traded;
	private long margin;
	private long net;	
	
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public long getPnsid() {
		return pnsid;
	}

	public void setPnsid(long pnsid) {
		this.pnsid = pnsid;
	}

	public long getPnsgid() {
		return pnsgid;
	}

	public void setPnsgid(long pnsgid) {
		this.pnsgid = pnsgid;
	}

	public long getPoid() {
		return poid;
	}

	public void setPoid(long poid) {
		this.poid = poid;
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

	public long getTraded() {
		return traded;
	}

	public void setTraded(long traded) {
		this.traded = traded;
	}

	public long getMargin() {
		return margin;
	}

	public void setMargin(long margin) {
		this.margin = margin;
	}

	public long getNet() {
		return net;
	}

	public void setNet(long net) {
		this.net = net;
	}

}
