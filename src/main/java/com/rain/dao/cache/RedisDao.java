package com.rain.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.rain.entity.Seckill;
import com.rain.utils.RedisCacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

/**
 * Created by Administrator on 2017/2/22 0022.
 */
@Repository
public class RedisDao {

    @Autowired
    private RedisCacheConfiguration redisCacheConfiguration;

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public Seckill getSeckill(Long seckillId){
        try {
            Jedis jedis = redisCacheConfiguration.redisPoolFactory().getResource();
            try {
                String key = "seckill:" + seckillId;
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null){
                    Seckill seckill = schema.newMessage();
                    ProtobufIOUtil.mergeFrom(bytes,seckill,schema);
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String putSeckill(Seckill seckill){
        try {
            Jedis jedis = redisCacheConfiguration.redisPoolFactory().getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                byte[] bytes = ProtobufIOUtil.toByteArray(seckill,schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                return jedis.setex(key.getBytes(),60*60,bytes);
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
