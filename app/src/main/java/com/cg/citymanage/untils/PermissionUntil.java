package com.cg.citymanage.untils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.cg.citymanage.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

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

* 功能：权限相关的方法
* 作者：cg
* 时间：2019/9/20 10:47
*/
public class PermissionUntil {

    /**
     * 判断权限
     * @param mActivity               所在acivity
     * @param mContext                上下文
     * @param warnText                提示语
     * @param permission              权限
     */
    public static void JudgePermission(Activity mActivity, final Context mContext, final String warnText, String... permission)
    {
        AndPermission.with(mActivity)
                .runtime()
                .permission(permission)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {

                        //授权失败的提示
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setIcon(R.mipmap.ico_warning);
                        builder.setTitle("权限申请失败");
                        builder.setMessage(warnText);
                        builder.setPositiveButton("确定", null);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }).start();
    }

    /**
     * 判断权限是否开通
     * @param mContext              上下文
     * @param mActivity             调用的页面
     * @param permission            权限
     * @return                      true:开通      false:未开通
     */
    public static boolean checkGalleryPermission(Context mContext,Activity mActivity,String permission) {
        int ret = ActivityCompat.checkSelfPermission(mContext, permission);
        if (ret != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[] {permission},
                    1000);
            return false;
        }
        return true;
    }
}
