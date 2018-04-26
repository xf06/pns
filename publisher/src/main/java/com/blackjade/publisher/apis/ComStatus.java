package com.blackjade.publisher.apis;

public class ComStatus {
	
	public static enum PnSStatus{
		PUBLISHED,		//0
		HALF_TRADED,	//0
		FULL_LOCKED,	//0
		FULL_TRADED,	//final
		CANCELLED,		//final
		HALF_CANCELLED,	//final
		UNKNOWN			//final
	}
	
	
	public static enum PublishStatus {
		SUCCESS, 
		WRONG_MSGID,
		DATABASE_ERR
	}
	
	public static enum DealStatus {
		SUCCESS, 
		WRONG_MSGID,
		PNS_MISS_MATCH,
		PNS_DATA_MESS,
		PNS_STATUS_MESS,
		PNS_STATUS_FINAL,
		PNS_STATUS_LOCKED

	}
	
	public static enum PaidStatus {
		SUCCESS, 
		WRONG_MSGID
	}
	
	public static enum PayConfirmStatus {
		SUCCESS, 
		WRONG_MSGID
	}
	
	public static enum CancelStatus {
		SUCCESS, 
		WRONG_MSGID
	}
		
}
