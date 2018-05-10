package com.blackjade.subscriber.apis;

public class ComStatus {

	// {poid, price, quant, min, max, comment}
	public static class ComMember {
		private int poid;
		private long price;
		private int quant;
		private long min;
		private long max;

		public int getPoid() {
			return poid;
		}

		public void setPoid(int poid) {
			this.poid = poid;
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

	}

	public static enum QueryOwnTopStatus {
		SUCCESS, 
		WRONG_MSGID,
		INMSG_ERR,
		PNS_DB_MISS,
		UNKNOWN
	}

	public static enum QueryOwnNextStatus {
		SUCCESS, WRONG_MSGID
	}

	public static enum QueryPnSTopStatus {
		SUCCESS, 
		WRONG_MSGID,
		INMSG_ERR,
		PNS_EMPTY,
		PNS_DB_MISS,
		UNKNOWN,
	}

	public static enum QueryPnSNextStatus {
		SUCCESS, WRONG_MSGID
	}

}
