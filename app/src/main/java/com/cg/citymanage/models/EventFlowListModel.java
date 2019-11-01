package com.cg.citymanage.models;

public class EventFlowListModel {

    private String eventFlowId;
    private String eventFlowLink;
    private String eventFlowStreet;
    private String eventFlowHandler;
    private String eventFlowHandleTime;
    private String eventFlowInfo;

    public EventFlowListModel() {
    }

    public String getEventFlowId() {
        return eventFlowId;
    }

    public void setEventFlowId(String eventFlowId) {
        this.eventFlowId = eventFlowId;
    }

    public String getEventFlowLink() {
        return eventFlowLink;
    }

    public void setEventFlowLink(String eventFlowLink) {
        this.eventFlowLink = eventFlowLink;
    }

    public String getEventFlowHandler() {
        return eventFlowHandler;
    }

    public void setEventFlowHandler(String eventFlowHandler) {
        this.eventFlowHandler = eventFlowHandler;
    }

    public String getEventFlowHandleTime() {
        return eventFlowHandleTime;
    }

    public void setEventFlowHandleTime(String eventFlowHandleTime) {
        this.eventFlowHandleTime = eventFlowHandleTime;
    }

    public String getEventFlowInfo() {
        return eventFlowInfo;
    }

    public void setEventFlowInfo(String eventFlowInfo) {
        this.eventFlowInfo = eventFlowInfo;
    }

    public String getEventStreet() {
        return eventFlowStreet;
    }

    public void setEventStreet(String eventFlowStreet) {
        this.eventFlowStreet = eventFlowStreet;
    }
}
