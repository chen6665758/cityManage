package com.cg.citymanage.untils;

import android.view.View;
import android.view.ViewTreeObserver;


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

* 功能：为控件设置最大高度
* 作者：cg
* 时间：2019/7/3 10:44
*/
public class OnViewGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

    private int maxHeight = 500;
    private View view;

    public OnViewGlobalLayoutListener(View view, int height) {
        this.view = view;
        this.maxHeight = height;
    }

    @Override
    public void onGlobalLayout() {
        if (view.getHeight() > maxHeight)
            view.getLayoutParams().height = maxHeight;
    }
}
