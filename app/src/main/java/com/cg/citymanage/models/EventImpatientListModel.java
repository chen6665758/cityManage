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

* 功能：事件中催办流程列表model
* 作者：cg
* 时间：2019/10/25 10:25
*/
public class EventImpatientListModel {

    private String eventImpatientId;
    private String eventImpatientLink;
    private String eventImpatienter;
    private String eventpImpatienter;
    private String eventImpatientTime;
    private String eventImpatientInfo;

    public EventImpatientListModel() {
    }

    public String getEventImpatientId() {
        return eventImpatientId;
    }

    public void setEventImpatientId(String eventImpatientId) {
        this.eventImpatientId = eventImpatientId;
    }

    public String getEventImpatientLink() {
        return eventImpatientLink;
    }

    public void setEventImpatientLink(String eventImpatientLink) {
        this.eventImpatientLink = eventImpatientLink;
    }

    public String getEventImpatienter() {
        return eventImpatienter;
    }

    public void setEventImpatienter(String eventImpatienter) {
        this.eventImpatienter = eventImpatienter;
    }

    public String getEventpImpatienter() {
        return eventpImpatienter;
    }

    public void setEventpImpatienter(String eventpImpatienter) {
        this.eventpImpatienter = eventpImpatienter;
    }

    public String getEventImpatientTime() {
        return eventImpatientTime;
    }

    public void setEventImpatientTime(String eventImpatientTime) {
        this.eventImpatientTime = eventImpatientTime;
    }

    public String getEventImpatientInfo() {
        return eventImpatientInfo;
    }

    public void setEventImpatientInfo(String eventImpatientInfo) {
        this.eventImpatientInfo = eventImpatientInfo;
    }
}
