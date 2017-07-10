package com.rain.dto;


import com.rain.entity.SuccessKilled;
import com.rain.enums.SeckillState;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class SeckillExecution {
    private Long seckillId;
    private int state;
    private String stateInfo;
    private SuccessKilled successKilled;

    public SeckillExecution(Long seckillId, SeckillState state, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = state.getState();
        this.stateInfo = state.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(Long seckillId, SeckillState state) {
        this.seckillId = seckillId;
        this.state = state.getState();
        this.stateInfo = state.getStateInfo();
    }

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }
}
