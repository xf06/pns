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

	
	
	
}
