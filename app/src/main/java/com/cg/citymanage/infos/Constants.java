package com.cg.citymanage.infos;

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

* 功能：静态属性类，一些常量在这里定义
* 作者：cg
* 时间：2019/10/21 9:43
*/
public class Constants {

    //列表页每页显示数量
    public static String PAGESIZE = "10";


    //所有接口的IP地址
    public static String MAIN_URL = "http://10.64.8.178:10008/";  //"http://10.64.8.178:10009/";http://10.64.3.110:8000

    //用户登录
    public static String LOGIN_URL = MAIN_URL + "oauth/token";
    //引导页验证用户是否登录，是否有效果
    public static String VALIDTOKEN_URL = MAIN_URL + "oauth/check_token";

    //首页用户信息
    public static String MAINUSERINFO_URL = MAIN_URL + "api/employee/me";
    //首页用户事件数量信息显示
    public static String MAINUSEREVENTNUMBER_URL = MAIN_URL + "api/query_message_count";
    //首页菜单
    public static String MAINUSERMENU_URL = MAIN_URL + "api/query_menu";

    //参与事件列表接口
    public static String EVENTPARTAKELIST_URL = MAIN_URL + "api/query_event";
    //事件传阅列表接口
    public static String EVENTTRANSMIT_URL = MAIN_URL + "api/toViewEventList";
    //被传阅事件列表
    public static String EVENTCIRCULATED_URL = MAIN_URL + "api/showViewEvent";
    //待办事件列表
    public static String EVENTEVENTWAIT_URL = MAIN_URL + "api/getTaskList";
    //催办事件
    public static String EVENTIMPATIENT_URL = MAIN_URL + "api/timeManage";
    //事件详情页事件流程列表接口
    public static String EVENTDETAIL_URL = MAIN_URL + "api/eventPartakeInfo";
    //事件中催办事件详情列表接口
    public static String EVENTIMPATIENTDETAIL_URL = MAIN_URL + "api/eventHasten";
    //事件中详情页事件流程列表中附件接口
    public static String EVENTDETAILENCLOSURE_URL = MAIN_URL + "api/getEventAccessory";


    //升级下载的文件名
    public static String DownLoadFileName = "citymanage.apk";
    //取得版本号信息，用于判断系统是否需求升级
    public static String DOWNLOADFILE_URL = MAIN_URL + "/version/0";


    //*********************************地图地址***************************************
    //底图
    public static String BaseMap_URL = "http://60.15.198.193:5128/iserver/services/map-ugcv5-suihua/rest/maps/suihua";
    //数据层图
    public static String DataMap_URl = "http://60.15.198.193:5128/iserver/services/map-suihuaGrid/rest/maps/habGrid";
}
