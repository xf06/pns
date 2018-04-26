package com.blackjade.publisher.domain;

import java.util.UUID;

import org.apache.ibatis.annotations.Param;

public class OrderRow {

	private long timestamp;
	private String oid;
	private int cid;
	private char type;
	private char side;
	private String pnsoid;
	private long poid;
	private long pnsid;
	private long pnsgid;
	private long price;
	private long quant;
	private long min;
	private long max;
	private String form;

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

	public String getPnsoid() {
		return pnsoid;
	}

	public void setPnsoid(String pnsoid) {
		this.pnsoid = pnsoid;
	}

	public long getPoid() {
		return poid;
	}

	public void setPoid(long poid) {
		this.poid = poid;
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

	public long getMin() {
		return min;
	}

	public void setMin(long min) {
		this.min = min;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

}
