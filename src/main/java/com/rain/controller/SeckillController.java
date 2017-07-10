package com.rain.controller;

import com.rain.dto.Exposer;
import com.rain.dto.SeckillExecution;
import com.rain.dto.SeckillResult;
import com.rain.entity.Seckill;
import com.rain.enums.SeckillState;
import com.rain.exception.RepeatKillException;
import com.rain.exception.SeckillCloseException;
import com.rain.service.SeckillService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;


/**
 * 秒杀
 * Created by guanchunyu on 2016/8/30.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Resource
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("/list");
        List<Seckill> list = seckillService.getSeckillAll();
        mav.addObject("list", list);
        return mav;
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable("seckillId") Long seckillId) {
        ModelAndView mav = new ModelAndView("/detail");
        if (seckillId == null) {
            return new ModelAndView("/list");
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return new ModelAndView("/list");
        }
        mav.addObject("seckill", seckill);
        return mav;
    }

    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            result = new SeckillResult<>(false, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) Long phone) {
        if (phone == null) {
            return new SeckillResult<>(false, "未注册");
        }
        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
            return new SeckillResult<>(true, execution);
        } catch (RepeatKillException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillState.REPEAT_KILL);
            return new SeckillResult<>(false, execution);
        } catch (SeckillCloseException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillState.END);
            return new SeckillResult<>(false, execution);
        } catch (Exception e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillState.INNER_EROR);
            return new SeckillResult<>(false, execution);
        }
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        Date date = new Date();
        return new SeckillResult(true, date.getTime());
    }

    @RequestMapping(value = "/ceshi", method = RequestMethod.GET)
    public ModelAndView ceshi() {
        ModelAndView mav = new ModelAndView("/ceshi");
        List<String> ceshi = seckillService.queryName();
        mav.addObject("ceshi",ceshi);
        return mav;
    }

}
