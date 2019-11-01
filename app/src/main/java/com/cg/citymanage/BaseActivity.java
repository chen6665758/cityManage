package com.cg.citymanage;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cg.citymanage.customs.MyLoading;
import com.cg.citymanage.untils.ConnectionDetector;
import com.cg.citymanage.untils.StatusBarUtils;
import com.google.gson.Gson;

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

* 功能：基础的acivity，里面定义了一些基础变量，方便调用
* 作者：cg
* 时间：2019/9/20 9:25
*/
public abstract class BaseActivity extends AppCompatActivity {

    public ConnectionDetector cd;
    public Boolean isInternetPresent = false;
    //shared变量的读与取
    public SharedPreferences.Editor shared_editor;
    public SharedPreferences mSharedPreferences;

    public String pkgName;
    public Bundle bundle;
    public Gson gson;
    public Context mContext;

    public MyLoading progress_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress_Dialog = MyLoading.createDialog(BaseActivity.this);
        pkgName = getApplicationContext().getPackageName();
        setContentView();
        //沉浸式状态栏与状态栏文字颜色
        StatusBarUtils.setStatusBarColor(this, Color.parseColor("#3a98f1"));
        bundle = new Bundle();
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();// 判断网络
        mSharedPreferences = getSharedPreferences("data", 0);// 获取SharedPreferences对象
        shared_editor = getSharedPreferences("data", 0).edit();// 获取Editor对象。
        gson = new Gson();
    }


    //加载布局
    public abstract void setContentView();
    //加载跳动功能
    public abstract void Jump_intent(Class<?> cla, Bundle bundle);

}
