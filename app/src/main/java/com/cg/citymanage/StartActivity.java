package com.cg.citymanage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.cg.citymanage.untils.ConnectionDetector;
import com.cg.citymanage.untils.StatusBarUtils;
import com.cg.citymanage.untils.myUntils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import static com.cg.citymanage.infos.Constants.VALIDTOKEN_URL;

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

* 功能：引导页
* 作者：cg
* 时间：2019/10/16 8:47
*/
public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private final int times = 3000;       //页面停留时长
    public ConnectionDetector cd;
    public Boolean isInternetPresent = false;
    public SharedPreferences mSharedPreferences;

    private Context mContext;

    private String appToken;

    private LinearLayout linear_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mContext = this;
        //沉浸式状态栏与状态栏文字颜色
        //StatusBarUtils.setStatusBarColor(this, Color.parseColor("#3a98f1"));
        StatusBarUtils.setStatusBarTransparent(mContext);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();// 判断网络
        mSharedPreferences = getSharedPreferences("data", 0);// 获取SharedPreferences对象

        initNet();
        initControls();
    }

    /**
     * 判断网络是否通畅
     */
    private void initNet()
    {
        if(isInternetPresent) {
            appToken = mSharedPreferences.getString("appToken", "");
            ValidToken(appToken);
        }else{
            myUntils.showToast(mContext,"请链接网络，保持网络连接，点击屏幕重新加载！");
        }
    }

    /**
     * 初始化控件
     */
    private void initControls()
    {
        linear_start = (LinearLayout)findViewById(R.id.linear_start);

        if(!isInternetPresent) {
            linear_start.setOnClickListener(this);
        }

    }


    /**
     * 判断用户是否已经登录
     * @param appToken           用户唯一标识
     */
    private void ValidToken(String appToken)
    {
        Handler handler = new Handler();
        if(!"".equals(appToken))
        {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    JumpMain();
                }
            }, times);

        }else{

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Jump_intent(LoginActivity.class, null);
                    finish();
                }
            }, times);

        }
    }

    /**
     * 用户再次登录时，先判断apptoken是否过期，然后根据需求进行跳转
     */
    private void JumpMain()
    {

        OkGo.<String>post(VALIDTOKEN_URL)
                .tag(this)//
                .params("token", appToken)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        String data = response.body();//这个就是返回来的结果
                        try{
                            JSONObject json = new JSONObject(data);
                            String resultCode = json.getString("code");
                            if(resultCode.equals("2000"))
                            {
                                Jump_intent(MainActivity.class,null);
                            }else{
                                Jump_intent(LoginActivity.class,null);
                            }
                            finish();
                        }catch (Exception ex)
                        {
                            Log.e("StartActivity", "行数: 160  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！点击屏幕重新加载！");

                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("StartActivity", "行数: 171  error:" + response.body());
                        myUntils.showToast(mContext,"请检查网络是否正常链接！点击屏幕重新加载！");

                    }
                });
    }


    public void Jump_intent(Class<?> cla, Bundle bundle) {
        if(mContext !=null)
        {
            Intent intent = new Intent(mContext, cla);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
            //overridePendingTransition(R.anim.fade_in_slide_in, R.anim.fade_out_slide_out);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.linear_start:
                isInternetPresent = cd.isConnectingToInternet();
                initNet();
                break;
        }
    }
}
