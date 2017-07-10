package com.rain.service.impl;

import com.rain.dao.SeckillDao;
import com.rain.dao.SuccessKilledDao;
import com.rain.dao.cache.RedisDao;
import com.rain.dto.Exposer;
import com.rain.dto.SeckillExecution;
import com.rain.entity.Seckill;
import com.rain.entity.SuccessKilled;
import com.rain.enums.SeckillState;
import com.rain.exception.RepeatKillException;
import com.rain.exception.SeckillCloseException;
import com.rain.exception.SeckillException;
import com.rain.service.SeckillService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;



/**
 * 秒杀实现
 * Created by Administrator on 2016/7/27 0027.
 */
@Service("seckillService")
public class SeckillServiceImpl implements SeckillService {

    private final static String slat = "asdfadfhweorjl234lkelasdfle4l";

    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;


    @Cacheable("ceshi11111")
    @Override
    public List<Seckill> getSeckillAll() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(Long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        Long startTime = seckill.getStartTime().getTime();
        Long endTime = seckill.getEndTime().getTime();
        seckill.setStartTimeString(startTime.toString());
        seckill.setEndTimeString(endTime.toString());
        return seckill;
    }

    @Override
    public Exposer exportSeckillUrl(Long seckillId) {

        //这里可以直接用注解式缓存，要简单的多
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null){
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null){
                return new Exposer(false,seckillId);
            }else {
                redisDao.putSeckill(seckill);
            }
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        String md5 = getMd5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMd5(Long seckillId) {
        String base = seckillId + "/" + slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    @Override
    public SeckillExecution executeSeckill(Long seckillId, Long phone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMd5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        try {
            int updateCount = seckillDao.reduceNumber(seckillId, new Date());
            if (updateCount <= 0) {
                throw new SeckillCloseException("seckill is closed");
            } else {
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, phone);
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill re");
                } else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, phone);
                    return new SeckillExecution(seckillId, SeckillState.SUCCESS, successKilled);
                }

            }
        } catch (SeckillCloseException | RepeatKillException e1) {
            throw e1;
        } catch (Exception e) {
            throw new SeckillException("seckill");
        }
    }

    @Override
    public List<String> queryName() {
        return seckillDao.queryName();
    }
}
