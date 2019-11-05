package com.cg.citymanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

* 功能：信息详情页
* 作者：cg
* 时间：2019/11/4 14:06
*/
public class InformationDetailActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 信息详情
     */
    private TextView txt_infoTitle;
    private TextView txt_infoTime;
    private TextView txt_infoContent;

    /**
     * 附件信息
     */
    private TextView txt_other;

    /**
     * 回复按钮
     */
    private Button btn_reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_information_detail);
    }

    /**
     *初始化控件
     */
    private void initControls()
    {
        //标题栏
        title_left_btn = (ImageButton)findViewById(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_textview = (TextView) findViewById(R.id.title_textview);
        title_textview.setText("信息详情");

        //信息详情
        txt_infoTitle = (TextView)findViewById(R.id.txt_infoTitle);
        txt_infoTime = (TextView)findViewById(R.id.txt_infoTime);
        txt_infoContent = (TextView)findViewById(R.id.txt_infoContent);

        //附件信息
        txt_other = (TextView)findViewById(R.id.txt_other);
        txt_other.setOnClickListener(this);

        //回复按钮
        btn_reply = (Button)findViewById(R.id.btn_reply);
        btn_reply.setOnClickListener(this);
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
        switch (v.getId())
        {
            //返回
            case R.id.title_left_btn:
                finish();
                break;
            //跳转附件
            case R.id.txt_other:
                Jump_intent(EventPicViewActivity.class,bundle);
                break;
            //回复内容
            case R.id.btn_reply:

                break;
        }
    }
}
