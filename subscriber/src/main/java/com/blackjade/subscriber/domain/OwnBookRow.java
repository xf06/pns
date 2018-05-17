package com.blackjade.subscriber.domain;

public class OwnBookRow { //own pns book
	
	private String pnsoid;
	private long poid;
	// private String poname;
	private long price;
	private long quant;
	private long traded;
	private long margin;
	private long net;
	private long can;
	private long max;
	private long min;
	private String Status;
	
	
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
	public long getCan() {
		return can;
	}
	public void setCan(long can) {
		this.can = can;
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
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	} 	
	
	
}
