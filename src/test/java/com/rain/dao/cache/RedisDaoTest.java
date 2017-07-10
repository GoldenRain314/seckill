package com.rain.dao.cache;

import com.rain.dao.SeckillDao;
import com.rain.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2017/2/22 0022.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ImportResource({"classpath:/spring/*.xml"})
public class RedisDaoTest {

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testSeckill(){
        Seckill seckill = redisDao.getSeckill(1000L);
        if(seckill == null){
            seckill = seckillDao.queryById(1000L);
            if (seckill != null){
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
                System.out.println(redisDao.getSeckill(seckill.getSeckillId()));

            }
        }
    }

}
