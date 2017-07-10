package com.rain.enums;

/**
 * Created by Administrator on 2016/7/28 0028.
 */
public enum SeckillState {

    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_EROR(-2, "系统异常"),
    DATA_REWRITS(-3, "数据篡改");


    private int state;
    private String stateInfo;

    SeckillState(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static SeckillState stateOf(int index) {
        for (SeckillState state : values()) {
            if (index == state.getState()) {
                return state;
            }
        }
        return null;
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
}
