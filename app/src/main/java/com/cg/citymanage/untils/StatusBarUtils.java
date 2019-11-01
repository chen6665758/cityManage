package com.cg.citymanage.untils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

* 功能：沉浸式状态栏与状态栏文字颜色
* 作者：cg
* 时间：2019/9/20 9:48
*/
public class StatusBarUtils {

    /**
     * 得到状态栏的高度
     *
     * @param mContext 上下文
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context mContext) {
        int statusBarHeight = 0;

        try {
            int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = mContext.getResources().getDimensionPixelOffset(resourceId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return statusBarHeight;
    }

    /**
     * 设置状态栏的颜色
     *
     * @param window 当前窗口，来自activiy或dialog等持有的窗口
     * @param color  颜色值
     */
    public static void setStatusBarColor(@NonNull Window window, @ColorInt int color) {

        //Android 5.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(color);

            setStatusBarTextDark(window, !isDarkColor(color));
        }
    }

    /**
     * 对于activity进行修改
     *
     * @param context activity的context
     * @param color   颜色
     */
    public static void setStatusBarColor(Context context, @ColorInt int color) {
        if (context instanceof Activity) {
            setStatusBarColor(((Activity) context).getWindow(), color);
        }
    }

    /**
     * 设置状态栏文字的颜色          Android 6.0以上
     *
     * @param window 窗口
     * @param isDark 是否是深色背景
     */
    private static void setStatusBarTextDark(Window window, boolean isDark) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            int systemUiVisibility = decorView.getSystemUiVisibility();
            if (isDark) {
                decorView.setSystemUiVisibility(systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decorView.setSystemUiVisibility(systemUiVisibility & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switch (OSUtils.getRomType()) {
                case MIUI:
                    setMIUIDark(window, isDark);
                    break;
                case Flyme:
                    setFlymeDark(window, isDark);
                    break;
                default:
            }
        }

    }

    public static void setStatusBarTextDark(Context context, boolean isDark) {
        if (context instanceof Activity) {
            setStatusBarTextDark(((Activity) context).getWindow(), isDark);
        }
    }

    /**
     * 根据状态栏背景颜色的深浅而自动设置文字的颜色
     *
     * @param color 颜色
     * @return 返回值
     */
    public static boolean isDarkColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) < 0.5;
    }

    /**
     * 小米手机在android5.0-6.0之间支持的修改状态栏文字颜色
     *
     * @param window window
     * @param isDark 是否是暗色
     */
    private static void setMIUIDark(Window window, boolean isDark) {
        try {
            Class<? extends Window> clazz = window.getClass();
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, isDark ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 魅族手机在android5.0-6.0之间支持的修改状态栏文字颜色
     *
     * @param window window
     * @param isDark 是否是暗色
     */
    private static void setFlymeDark(Window window, boolean isDark) {
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (isDark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 当背景是图片时，设置沉浸试
     *
     * @param window 窗口
     */
    public static void setStatusBarTransparent(@NonNull Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setStatusBarTransparent(Context context) {
        if (context instanceof Activity) {
            setStatusBarTransparent(((Activity) context).getWindow());
        }
    }
}



