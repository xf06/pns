package com.blackjade.publisher.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.blackjade.publisher.domain.PnSRow;

@Component
public interface PnSDao {

	public int insertPnS(PnSRow pnsrow);
	// public PnSRow selectPnSRowCDeal(CDeal deal);

	// Select and LOCK
	public PnSRow selectPnSRowOid(@Param(value = "oid") String pnsoid);

	// Deal UPDATE
	public int updatePnSDeal(@Param(value = "oid") String oid, @Param(value = "margin") long margin,
			@Param(value = "net") long net, @Param(value = "status") String status);

	// PayConfirm UPDATE
	public int updatePnSPayConfirm(@Param(value = "oid") String oid, @Param(value = "margin") long margin,
			@Param(value = "traded") long traded, @Param(value = "status") String status);

	// Cancel Deal UPDATE
	public int updatePnSCancelDeal(@Param(value = "oid") String oid, @Param(value = "margin") long margin,
			@Param(value = "net") long net, @Param(value = "status") String status);

	// Cancel Publish UPDATE
	public int updatePnSCancelPublic(@Param(value = "oid") String oid, @Param(value = "net") long net,
			@Param(value = "can") long can, @Param(value = "status") String status);
	
}
