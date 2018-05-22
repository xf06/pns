package com.blackjade.subscriber.apis;

public class ComStatus {

	//------------------- New APIs --------------------------//
	public static enum QueryAllOrdRecvStatus{
		SUCCESS,
		WRONG_MSGID,
		INMSG_ERR,
		ORD_DB_EMPTY,
		ORD_DB_MISS,
		UNKNOWN
	}
	
	public static enum QueryAllOrdSentStatus{
		SUCCESS,
		WRONG_MSGID,
		INMSG_ERR,
		ORD_DB_EMPTY,
		ORD_DB_MISS,
		UNKNOWN
	}
	
	
	public static enum QueryOwnOrdStatus{
		SUCCESS,
		WRONG_MSGID,
		INMSG_ERR,
		ORD_DB_EMPTY,
		ORD_DB_MISS,
		UNKNOWN
	}
	
	public static enum QueryPnSStatus {
		SUCCESS, 
		WRONG_MSGID,
		INMSG_ERR,
		PNS_EMPTY,
		PNS_DB_MISS,
		UNKNOWN
	}
	
	public static enum QueryOwnStatus {
		SUCCESS,
		WRONG_MSGID,
		INMSG_ERR,
		PNS_EMPTY,
		PNS_DB_MISS,
		UNKNOWN,
	}	
	
	public static enum  QueryPnSOrdStatus{
		SUCCESS,
		WRONG_MSGID,
		INMSG_ERR,
		ORD_DB_EMPTY,
		ORD_DB_MISS,
		UNKNOWN
	}
	
	//------------------------------------------------------//
	
}




