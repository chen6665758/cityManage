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
    //事件传阅详情页，传阅人列表
    public static String EVENTRANSMITER_URL = MAIN_URL + "api/circulateEmpList";
    //事件传阅提交接口
    public static String EVENTRANSMITSUBMIT_URL = MAIN_URL + "api/insertViewEvent";
    //被传阅事件列表
    public static String EVENTCIRCULATED_URL = MAIN_URL + "api/showViewEvent";
    //待办事件列表
    public static String EVENTEVENTWAIT_URL = MAIN_URL + "api/getTaskList";
    //待办事件详情页面接口
    public static String EVENTWAITDETAIL_URL = MAIN_URL + "api/reEventInfo";
    //待办事件详情回复值中，处理部分部门接口
    public static String EVENTWAITDEP_URL = MAIN_URL + "api/toEmpTree6";
    //待办事件详情中，处理内容回复的接口
    public static String EVENTWAITSUBMIT_URL = MAIN_URL + "api/completeTask"; //"http://10.64.3.114:8000/api/completeTask";//
    //催办事件
    public static String EVENTIMPATIENT_URL = MAIN_URL + "api/timeManage";
    //催办事件详情页面催办事件提交
    public static String EVENTIMPATIENTSUMIT_URL = MAIN_URL + "api/editStatus";
    //事件详情页事件流程列表接口
    public static String EVENTDETAIL_URL = MAIN_URL + "api/eventPartakeInfo";
    //事件中催办事件详情列表接口
    public static String EVENTIMPATIENTDETAIL_URL = MAIN_URL + "api/eventHasten";
    //事件中详情页事件流程列表中附件接口
    public static String EVENTDETAILENCLOSURE_URL = MAIN_URL + "api/getEventAccessory";
    //事件总览查询列表
    public static String EVENTOVERVIEW_URL = MAIN_URL + "api/eventQuery";
    //事件上报接口
    public static String EVENTREPORTADD_URL = MAIN_URL + "api/insertToEvent";

    //事件类别选择接口
    public static String EVENTTYPE_URL = MAIN_URL + "api/getEventTypeByTypeId";
    //事件节点名称选择接口
    public static String EVENTNOTE_URL = MAIN_URL + "api/getOutcomeList";

    //信息列表接口
    public static String INFORMATIONLIST_URL = MAIN_URL + "api/toReceiveMessageList";
    //信息详情接口
    public static String INFORMATIONDETAIL_URL = MAIN_URL + "api/MessageFindOne";
    //信息添加与回复接口
    public static String INFORMATIONADD_URL = MAIN_URL + "api/saveMessage";


    //部件统计接口
    public static String PARTSTATISTICS_URL = MAIN_URL + "api/unit/statistics/tree";
    //部件编码验证接口
    public static String PARTSVAROBJID_URL = MAIN_URL + "api/validatorCityUnit";
    //部件采集接口
    public static String PARTSUBMITADD_URL = MAIN_URL + "api/insertCityUnit";

    //通讯录列表接口
    public static String MAILLIST_URL = MAIN_URL + "api/addressBook/list";

    //考勤当天数据接口
    public static String CLOCKTODAY_URL = MAIN_URL + "api/checkin/today";
    //考勤周统计接口
    public static String CLOCKINFOWEEK_URL = MAIN_URL + "api/checkin/statistics/week";
    //考勤月统计接口
    public static String CLOCKINFOMONTH_URL = MAIN_URL + "api/checkin/statistics/month";
    //考勤保存接口
    public static String CLOCKINSAVE_URL = MAIN_URL + "api/checkin/save";
    //考勤历史记录接口
    public static String CLOCKHISTORYLIST_URL = MAIN_URL + "api/checkin/me/query";


    //文件上传接口
    public static String FILEUPLOAD_URL = MAIN_URL + "api/upLoad/upLoadFile";
    //消息上传接口
    public static String INFORMATIONUPLOAD_URL = MAIN_URL + "api/upLoad/getFileName";

    //轨迹记录上传接口
    public static String TRACKRECORDADD_URL = MAIN_URL + "api/personnelTrajectory/save";
    //轨迹查询接口
    public static String TRACKSEARCH_URL = MAIN_URL + "api/personnelTrajectory/findSearchForOne";


    //个人设置修改密码
    public static String PERSONALUPDATEPWD_URL = MAIN_URL + "api/password/update";


    //升级下载的文件名
    public static String DownLoadFileName = "citymanage.apk";
    public static String DownLoadFilePath = "http://10.64.43.106:9999/citymanage.apk";
    //取得版本号信息，用于判断系统是否需求升级
    public static String DOWNLOADFILE_URL = MAIN_URL + "api/findAppVersion";



    //*********************************地图地址***************************************
    //底图
    //public static String BaseMap_URL = "http://60.15.198.193:5128/iserver/services/map-ugcv5-suihua/rest/maps/suihua";
    public static String BaseMap_URL = "http://221.211.181.10:9013/iserver/services/map-DiTu/rest/maps/北安底图";
    //数据层图
    //public static String DataMap_URl = "http://60.15.198.193:5128/iserver/services/map-suihuaGrid/rest/maps/habGrid";
    public static String DataMap_URl = "http://221.211.181.10:9013/iserver/services/map-wangge0827/rest/maps/habGrid";
}
