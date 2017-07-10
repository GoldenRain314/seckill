package com.rain.dao;

import com.rain.entity.Seckill;
import com.rain.service.SeckillService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@ImportResource({"classpath:/spring/*.xml"})
public class SeckillApplicationTests {

	@Resource
	private SeckillService seckillService;
	@Test
	public void contextLoads() {
		List<Seckill> list = seckillService.getSeckillAll();
		Assert.assertNotNull(list);
		System.out.println(list.toString());
	}

	@Test
	public void queryNameTest(){
		List<String> asd = seckillService.queryName();
		System.out.println(asd.toString());
	}

}
