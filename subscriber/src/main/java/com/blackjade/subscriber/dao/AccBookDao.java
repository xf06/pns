package com.blackjade.subscriber.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.blackjade.subscriber.domain.OwnAccRow;

@Component
public interface AccBookDao {
	
	// select the number of asset
	public int selectAccBookNum(@Param(value = "cid") int cid);
	
	// select the rows of that asset
	public  List<OwnAccRow> selectAccBookRows(@Param(value = "cid")int cid, @Param(value = "num") int num);//length by default 20
	
	
}
