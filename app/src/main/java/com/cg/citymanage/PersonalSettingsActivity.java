package com.cg.citymanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cg.citymanage.untils.myApplication;
import com.cg.citymanage.untils.myUntils;

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

* 功能：个人设置
* 作者：cg
* 时间：2019/11/25 15:50
*/
public class PersonalSettingsActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 显示数据
     */
    private LinearLayout linear_updatepwd;
    private TextView txt_version;

    /**
     * 退出按钮
     */
    private TextView txt_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_personal_settings);
    }

    /**
     * 初始化控件
     */
    private void initControls()
    {
        //标题栏
        title_left_btn = (ImageButton)findViewById(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_textview = (TextView) findViewById(R.id.title_textview);
        title_textview.setText("个人设置");

        //显示数据
        linear_updatepwd = (LinearLayout)findViewById(R.id.linear_updatepwd);
        linear_updatepwd.setOnClickListener(this);
        txt_version = (TextView)findViewById(R.id.txt_version);
        txt_version.setText("版本信息：" + myUntils.getAppVersionName(mContext));

        //退出
        txt_exit = (TextView)findViewById(R.id.txt_exit);
        txt_exit.setOnClickListener(this);
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
            //overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按钮
            case R.id.title_left_btn:
                finish();
                break;
            //跳转到修改密码
            case R.id.linear_updatepwd:
                Jump_intent(UpdatePwdActivity.class,bundle);
                break;
            //退出登录
            case R.id.txt_exit:
                OkCancelFragmentDialog dialog = OkCancelFragmentDialog.newInstance("您确定要退出平台吗？");
                dialog.show(getSupportFragmentManager(),"退出系统");
                dialog.setOnItemClickLitener(new OkCancelFragmentDialog.OnItemClickLitener() {
                    @Override
                    public void OnItemClick(View view, int positon) {
                        shared_editor.putString("appToken","").commit();
                        myApplication.getInstance().exitAllActivity();
                        startActivity(new Intent(mContext,LoginActivity.class));
                    }
                });
                break;
        }
    }
}
