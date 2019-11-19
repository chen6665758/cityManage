package com.cg.citymanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

* 功能：部件统计查询页面
* 作者：cg
* 时间：2019/11/14 16:52
*/
public class PartStatisticsSearchActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 查询条件
     */
    private TextView txt_siteValue;
    private int MAP_CODE = 101;
    private String gridId = "";
    private EditText edit_deptMain;
    private EditText edit_deptOwner;
    private EditText edit_deptKeep;

    /**
     * 提交按钮
     */
    private Button btn_partSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        initConrols();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_part_statistics_search);
    }

    /**
     * 初始化控件
     */
    private void initConrols()
    {
        //标题栏
        title_left_btn = (ImageButton)findViewById(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_textview = (TextView) findViewById(R.id.title_textview);
        title_textview.setText("部件统计查询");

        //查询条件
        txt_siteValue = (TextView)findViewById(R.id.txt_siteValue);
        txt_siteValue.setOnClickListener(this);
        edit_deptMain = (EditText)findViewById(R.id.edit_deptMain);
        edit_deptOwner = (EditText)findViewById(R.id.edit_deptOwner);
        edit_deptKeep = (EditText)findViewById(R.id.edit_deptKeep);

        //提交按钮
        btn_partSearch = (Button)findViewById(R.id.btn_partSearch);
        btn_partSearch.setOnClickListener(this);
    }

    @Override
    public void Jump_intent(Class<?> cla, Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.title_left_btn:
                finish();
                break;
            //选择网格
            case R.id.txt_siteValue:
                Intent mapIntent = new Intent();
                mapIntent.setClass(PartStatisticsSearchActivity.this,MapSelectActivity.class);
                //startActivityForResult(mapIntent,MAP_CODE);
                mapIntent.putExtra("mapclass","partsearch");
                startActivityForResult(mapIntent,MAP_CODE);
                break;
            /**
             * 查询
              */
            case R.id.btn_partSearch:
                if(TextUtils.isEmpty(gridId) && TextUtils.isEmpty(edit_deptKeep.getText().toString()) &&
                        TextUtils.isEmpty(edit_deptMain.getText().toString()) && TextUtils.isEmpty(edit_deptOwner.getText().toString()))
                {
                    myUntils.showToast(mContext,"对不起，查询条件不能全为空");
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(mContext, PartStatisticsActivity.class);
                intent.putExtra("gridId",gridId);
                intent.putExtra("deptMainId",edit_deptMain.getText().toString());
                intent.putExtra("deptOwnerId", edit_deptOwner.getText().toString());
                intent.putExtra("deptKeepId", edit_deptKeep.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == MAP_CODE) {

            txt_siteValue.setText(data.getStringExtra("siteValue"));
            gridId = data.getStringExtra("gridId");
        }
    }
}
