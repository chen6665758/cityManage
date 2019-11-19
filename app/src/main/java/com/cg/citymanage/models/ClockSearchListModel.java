package com.cg.citymanage.models;

/**
/*                       _oo0oo_
/*                      o8888888o
/*                      88" . "88
/*                      (| -_- |)
/*                      0\  =  /0
/*                    ___/`---'\___
/*                  .' \\|     |// '.
/*                 / \\|||  :  |||// \
/*                / _||||| -:- |||||- \
/*               |   | \\\  -  /// |   |
/*               | \_|  ''\---/''  |_/ |
/*               \  .-\__  '-'  ___/-. /
/*             ___'. .'  /--.--\  `. .'___
/*          ."" '<  `.___\_<|>_/___.' >' "".
/*         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
/*         \  \ `_.   \_ __\ /__ _/   .-` /  /
/*     =====`-.____`.___ \_____/___.-`___.-'=====
/*                       `=---='
/*
/*     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*               佛祖保佑         永无BUG

* 功能：考勤打卡，历史数据model
* 作者：cg
* 时间：2019/11/19 10:05
*/
public class ClockSearchListModel {

    private String clockTime;
    private String clockIn;
    private String clockOut;
    private String clockInStatus;
    private String clockOutStatus;

    public ClockSearchListModel() {
    }

    public String getClockTime() {
        return clockTime;
    }

    public void setClockTime(String clockTime) {
        this.clockTime = clockTime;
    }

    public String getClockIn() {
        return clockIn;
    }

    public void setClockIn(String clockIn) {
        this.clockIn = clockIn;
    }

    public String getClockOut() {
        return clockOut;
    }

    public void setClockOut(String clockOut) {
        this.clockOut = clockOut;
    }

    public String getClockInStatus() {
        return clockInStatus;
    }

    public void setClockInStatus(String clockInStatus) {
        this.clockInStatus = clockInStatus;
    }

    public String getClockOutStatus() {
        return clockOutStatus;
    }

    public void setClockOutStatus(String clockOutStatus) {
        this.clockOutStatus = clockOutStatus;
    }
}
