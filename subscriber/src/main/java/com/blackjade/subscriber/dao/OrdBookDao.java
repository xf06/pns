package com.blackjade.subscriber.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.blackjade.subscriber.domain.OrdBookRow;

@Component
public interface OrdBookDao {

	// select Deals of a Pub
	public int selectNumPnsOrder(@Param(value = "pnsoid") String pnsoid, @Param(value = "poid") int poid,
			@Param(value = "pnsgid") int pnsgid, @Param(value = "pnsid") int pnsid, @Param(value = "side") char side);

	// select Deals of a Pub
	public List<OrdBookRow> selectOrdBookRow(@Param(value = "poid") int poid, @Param(value = "pnsgid") int pnsgid,
			@Param(value = "pnsid") int pnsid, @Param(value = "side") char side, @Param(value = "num") int num);// num =
																												// pagenum*10
}
