package com.blackjade.subscriber.domain;

import java.util.UUID;

public class PubBookRow {

	private String pnsoid;
	private long poid;
	private String poname;
	private long price;
	private long quant;
	private long net;
	private long max;
	private long min;

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

	public String getPoname() {
		return poname;
	}

	public void setPoname(String poname) {
		this.poname = poname;
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

	public long getNet() {
		return net;
	}

	public void setNet(long net) {
		this.net = net;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

	public long getMin() {
		return min;
	}

	public void setMin(long min) {
		this.min = min;
	}

}
