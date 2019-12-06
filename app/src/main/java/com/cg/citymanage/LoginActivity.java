package com.cg.citymanage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cg.citymanage.untils.StatusBarUtils;
import com.cg.citymanage.untils.myUntils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import static com.cg.citymanage.infos.Constants.LOGIN_URL;

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

* 功能：用户登录
* 作者：cg
* 时间：2019/10/21 10:19
*/
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText edit_userName;
    private EditText edit_userPassword;
    private TextView txt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //沉浸式状态栏与状态栏文字颜色
        StatusBarUtils.setStatusBarColor(this, Color.parseColor("#67ACF5"));
        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);

    }

    /**
     * 初始化控件
     */
    private void initControls()
    {
        edit_userName = (EditText)findViewById(R.id.edit_userName);
        edit_userPassword = (EditText)findViewById(R.id.edit_userPassword);
        txt_login = (TextView)findViewById(R.id.txt_login);
        txt_login.setOnClickListener(this);
    }

    @Override
    public void Jump_intent(Class<?> cla, Bundle bundle) {
        if(mContext !=null)
        {
            Intent intent = new Intent(mContext, cla);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in_slide_in, R.anim.fade_out_slide_out);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.txt_login:
                //myUntils.showToast(mContext,"登录点击");
                if(isInternetPresent) {
                    //myUntils.showToast(mContext,"网络可用");
                    String userName = edit_userName.getText().toString();
                    String userPassword = edit_userPassword.getText().toString();

                    if ("".equals(userName) || "".equals(userPassword)) {
                        myUntils.showToast(mContext, "对不起，用户名或密码不能为空！");
                        return;
                    }

                    //myUntils.showToast(mContext,"读取网络");
                    OkGo.<String>post(LOGIN_URL)
                            .tag(this)//
                            .params("client_id", "digital-urban-management")
                            .params("client_secret", "1234567890")
                            .params("grant_type", "password")
                            .params("username", userName)
                            .params("password", userPassword)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    //注意这里已经是在主线程了
                                    String data = response.body();//这个就是返回来的结果
                                    //myUntils.showToast(mContext,"取得数据");
                                    try {
                                        JSONObject json = new JSONObject(data);
                                        String resultCode = json.getString("code");
                                        if(resultCode.equals("2000"))
                                        {
                                            shared_editor.putString("appToken",json.getString("access_token")).commit();
                                            Jump_intent(MainActivity.class,bundle);
                                            finish();
                                        }else{
                                           myUntils.showToast(mContext,json.getString("message"));
                                        }
                                    }catch (Exception ex)
                                    {
                                        Log.e("LoginActivity", "行数: 135  ex:" + ex.getMessage());
                                        //myUntils.showToast(mContext,"请检查网络是否正常链接！");

                                    }
                                }

                                @Override
                                public void onError(Response<String> response) {
                                    super.onError(response);
                                    Log.e("LoginActivity", "行数: 143  error:" + response.body());
                                    myUntils.showToast(mContext,"网络请求失败");
                                }
                            });
                }else{
                    myUntils.showToast(mContext,"请检查网络是否正常链接！");
                    return;
                }

                break;
        }
    }
}
