package com.blackjade.subscriber.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.blackjade.subscriber.domain.OwnBookRow;
import com.blackjade.subscriber.domain.PubBookRow;

@Component
public interface PubBookDao {

	// select the total number
	public int selectNumPns(@Param(value = "pnsgid") int pnsgid, @Param(value = "pnsid") int pnsid,
			@Param(value = "side") char side); // some condition of selection

	// select own total number
	public int selectOwnNumPns(@Param(value = "poid") int poid, @Param(value = "pnsgid") int pnsgid,
			@Param(value = "pnsid") int pnsid, @Param(value = "side") char side); // some condition of selection

	// select the N-th page of sell list
	public List<PubBookRow> selectSellPubBookRow(@Param(value = "pnsgid") int pnsgid, @Param(value = "pnsid") int pnsid,
			@Param(value = "side") char side, @Param(value = "num") int num); // num = pagenum*10

	// select the N-th page of buy list
	public List<PubBookRow> selectBuyPubBookRow(@Param(value = "pnsgid") int pnsgid, @Param(value = "pnsid") int pnsid,
			@Param(value = "side") char side, @Param(value = "num") int num); // num = pagenum*10
		
	// select the N page list
	public List<OwnBookRow> selectOwnBookRow(@Param(value = "poid") int poid, @Param(value = "pnsgid") int pnsgid,
			@Param(value = "pnsid") int pnsid, @Param(value = "side") char side, @Param(value = "num") int num);// num = pagenum*10
	
}
