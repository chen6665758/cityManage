package com.cg.citymanage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.cg.citymanage.customs.MyLoading;
import com.cg.citymanage.untils.ConnectionDetector;
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

* 功能：基础的fragment
* 作者：cg
* 时间：2019/9/20 9:37
*/
public abstract class BaseFragment extends Fragment {
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {
    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();

    public Context mContext;
    public ConnectionDetector cd;
    public Boolean isInternetPresent = false;       //判断网络是否链接
    public SharedPreferences.Editor shared_editor;
    public static SharedPreferences mSharedPreferences;
    public String pkgName;
    public Bundle bundle;
    public Gson gson;
    public MyLoading progress_Dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        progress_Dialog = MyLoading.createDialog(getActivity());
        //progress_Dialog.setMessage("努力加载中");
        //ApplicationAddActivity(getActivity());
        pkgName = mContext.getApplicationContext().getPackageName();
        bundle = new Bundle();
        cd = new ConnectionDetector(mContext);
        isInternetPresent = cd.isConnectingToInternet();// 判断网络
        mSharedPreferences = mContext.getSharedPreferences("data", 0);// 获取SharedPreferences对象
        shared_editor = mContext.getSharedPreferences("data", 0).edit();// 获取Editor对象。

        gson = new Gson();
    }
}
