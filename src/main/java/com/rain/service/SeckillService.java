package com.rain.service;


import com.rain.dto.Exposer;
import com.rain.dto.SeckillExecution;
import com.rain.entity.Seckill;
import com.rain.exception.RepeatKillException;
import com.rain.exception.SeckillCloseException;
import com.rain.exception.SeckillException;

import java.util.List;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public interface SeckillService {

    List<Seckill> getSeckillAll();

    Seckill getById(Long seckillId);

    Exposer exportSeckillUrl(Long seckillId);

    SeckillExecution executeSeckill(Long seckillId, Long phone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException;

    List<String> queryName();


}
