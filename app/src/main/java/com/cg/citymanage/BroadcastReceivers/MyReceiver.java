package com.cg.citymanage.BroadcastReceivers;

import android.app.Notification;
import android.content.Context;
import android.util.Log;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

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

* 功能：接收极光推送的数据
* 作者：cg
* 时间：2019/12/6 10:29
*/
public class MyReceiver extends JPushMessageReceiver {

    private static final String TAG = "JPush";

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        Log.e("MyReceiver.java(onNotifyMessageOpened)", "行数: 18  这里走了吗:" + notificationMessage.notificationType);

    }

    @Override
    public Notification getNotification(Context context, NotificationMessage notificationMessage) {

        Log.e("MyReceiver.java(getNotification)", "行数: 30  getNotification" + notificationMessage.notificationAlertType);



        return super.getNotification(context, notificationMessage);
    }

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
        Log.e("MyReceiver.java(onMessage)", "行数: 38  onMessage:" + customMessage.contentType);

    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onTagOperatorResult(context, jPushMessage);
        //Log.e("MyReceiver.java(onTagOperatorResult)", "行数: 51  onTagOperatorResult:" + jPushMessage.getAlias());
    }
}
