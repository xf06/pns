package com.blackjade.publisher.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import com.blackjade.publisher.domain.OrderRow;

@Component
public interface OrderDao {

	public int insertTest( @Param(value = "timestamp") long timestamp);

	public int insertOrder(OrderRow ordrow);
	
	public OrderRow selectOrder(String oid);

	public int updateOrderStatus(@Param(value = "oid")String oid, @Param(value = "status") String status);
	
}
