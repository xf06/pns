package com.blackjade.subscriber.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.blackjade.subscriber.domain.AllOrdRecvRow;
import com.blackjade.subscriber.domain.AllOrdSentRow;
import com.blackjade.subscriber.domain.OrdBookRow;

@Component
public interface OrdBookDao {

	// select Deals of a Pub
	public int selectNumPnsOrder(@Param(value = "pnsoid") String pnsoid, @Param(value = "poid") int poid,
			@Param(value = "pnsgid") int pnsgid, @Param(value = "pnsid") int pnsid, @Param(value = "side") char side);

	// select Deals of a Pub
	public List<OrdBookRow> selectOrdBookRow(@Param(value = "pnsoid") String pnsoid, @Param(value = "poid") int poid,
			@Param(value = "pnsgid") int pnsgid, @Param(value = "pnsid") int pnsid, @Param(value = "side") char side,
			@Param(value = "num") int num);// num = pagenum*10

	// select a deal from oid
	public OrdBookRow selectOwnOrd(@Param(value = "oid") String oid, @Param(value = "cid") int cid,
			@Param(value = "pnsoid") String pnsoid, @Param(value = "poid") int poid,
			@Param(value = "pnsgid") int pnsgid, @Param(value = "pnsid") int pnsid, @Param(value = "side") char side);

	// select num all ord from sent for one cid
	public int selectNumAllOrdSent(@Param(value = "cid") int cid, @Param(value = "pnsgid") int pnsgid,
			@Param(value = "pnsid") int pnsid);

	// select all ord from sent for one cid
	public List<AllOrdSentRow> selectAllOrdSent(@Param(value = "cid") int cid, @Param(value = "pnsgid") int pnsgid,
			@Param(value = "pnsid") int pnsid, @Param(value = "num") int num);// num = pagenum*10

	// select num all ord from recv for one cid
	public int selectNumAllOrdRecv(@Param(value = "poid") int poid, @Param(value = "pnsgid") int pnsgid,
			@Param(value = "pnsid") int pnsid);

	// select all ord from recv for one cid
	public List<AllOrdRecvRow> selectAllOrdRecv(@Param(value = "poid") int poid, @Param(value = "pnsgid") int pnsgid,
			@Param(value = "pnsid") int pnsid, @Param(value = "num") int num);// num = pagenum*10

}
