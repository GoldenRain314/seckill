package com.rain.dao;

import com.rain.entity.Seckill;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by GoldenRain on 2016/7/7.
 */
@Mapper
public interface SeckillDao {

    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    Seckill queryById(long seckillId);

    //@Select("SELECT seckill_id,name,number,start_time,end_time,create_time FROM seckill")
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    List<String> queryName();
}
