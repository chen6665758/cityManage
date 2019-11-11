package com.cg.citymanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cg.citymanage.untils.myUntils;

import java.util.Date;

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

* 功能：事件总览
* 作者：cg
* 时间：2019/10/24 8:27
*/
public class EventOverviewActivity extends BaseActivity implements View.OnClickListener {

    private String appToken;

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 选择条件
     */
    private TextView txt_typeName;
    private TextView txt_bigTypeName;
    private TextView txt_smallTypeName;
    private TextView txt_nodeName;
    private TextView txt_gridValue;
    private TextView txt_startTime;
    private TimePickerView pvTime;
    private TextView txt_endTime;
    private int timeType = 0;              //时间类别 0：开始时间 1：结束时间

    private String typeId;            //事件类别id
    private String typeBigId;         //事件大类id
    private String typeSmallId;       //事件小类id
    private String typeNodeId;        //节点id

    /**
     * 清空与查询按钮
     */
    private Button btn_clear;
    private Button btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        appToken = mSharedPreferences.getString("appToken","");
        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_event_overview);
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
        title_textview.setText("事件总览");

        //选择条件
        txt_typeName = (TextView)findViewById(R.id.txt_typeName);
        txt_typeName.setOnClickListener(this);
        txt_bigTypeName = (TextView)findViewById(R.id.txt_bigTypeName);
        txt_bigTypeName.setOnClickListener(this);
        txt_smallTypeName = (TextView)findViewById(R.id.txt_smallTypeName);
        txt_smallTypeName.setOnClickListener(this);
        txt_nodeName = (TextView)findViewById(R.id.txt_nodeName);
        txt_nodeName.setOnClickListener(this);
        txt_gridValue = (TextView)findViewById(R.id.txt_gridValue);
        txt_gridValue.setOnClickListener(this);
        txt_startTime = (TextView)findViewById(R.id.txt_startTime);
        txt_startTime.setOnClickListener(this);
        txt_endTime = (TextView)findViewById(R.id.txt_endTime);
        txt_endTime.setOnClickListener(this);

        //清空与查询按钮
        btn_clear = (Button)findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);
        btn_search = (Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);

        initDate();
    }

    /**
     * 初始化日期控件
     */
    private void initDate()
    {
        pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //Toast.makeText(mContext, date.toString(), Toast.LENGTH_SHORT).show();
                if(timeType == 0)
                {
                    txt_startTime.setText(myUntils.getTime(date));
                }else if(timeType == 1)
                {
                    txt_endTime.setText(myUntils.getTime(date));
                }
            }
        }).build();
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
            //选择事件分类
            case R.id.txt_typeName:
                EventReportTypeDialogFragment tDialog = EventReportTypeDialogFragment.newInstance("0","",appToken,"");
                tDialog.show(getSupportFragmentManager(),"事件分类");
                tDialog.setOnItemClickLitener(new EventReportTypeDialogFragment.OnItemClickLitener() {
                    @Override
                    public void OnItemClick(View view, String EventTypeId, String EventTypeName) {
                        //Log.e("EventOverview", "行数: 174  EventTypeId:" + EventTypeId + " EventTypeName:" + EventTypeName);
                        txt_typeName.setText(EventTypeName);
                        typeId = EventTypeId;
                    }
                });
                break;
            //事件大类
            case R.id.txt_bigTypeName:

                if(TextUtils.isEmpty(typeId))
                {
                    myUntils.showToast(mContext,"请先选择事件分类！");
                }else {
                    EventReportTypeDialogFragment bDialog = EventReportTypeDialogFragment.newInstance("1", typeId, appToken, "");
                    bDialog.show(getSupportFragmentManager(), "事件大类");
                    bDialog.setOnItemClickLitener(new EventReportTypeDialogFragment.OnItemClickLitener() {
                        @Override
                        public void OnItemClick(View view, String EventTypeId, String EventTypeName) {
                            txt_bigTypeName.setText(EventTypeName);
                            typeBigId = EventTypeId;
                        }
                    });
                }
                break;
            //事件小类
            case R.id.txt_smallTypeName:
                if(TextUtils.isEmpty(typeBigId))
                {
                    myUntils.showToast(mContext,"请先选择事件大类！");
                }else {
                    EventReportTypeDialogFragment sDialog = EventReportTypeDialogFragment.newInstance("2", typeBigId, appToken, "");
                    sDialog.show(getSupportFragmentManager(), "事件小类");
                    sDialog.setOnItemClickLitener(new EventReportTypeDialogFragment.OnItemClickLitener() {
                        @Override
                        public void OnItemClick(View view, String EventTypeId, String EventTypeName) {
                            txt_smallTypeName.setText(EventTypeName);
                            typeSmallId = EventTypeId;
                        }
                    });
                }
                break;
            //节点名称
            case R.id.txt_nodeName:
                EventReportTypeDialogFragment nDialog = EventReportTypeDialogFragment.newInstance("4","",appToken,"");
                nDialog.show(getSupportFragmentManager(),"节点名称");
                nDialog.setOnItemClickLitener(new EventReportTypeDialogFragment.OnItemClickLitener() {
                    @Override
                    public void OnItemClick(View view, String EventTypeId, String EventTypeName) {
                        txt_nodeName.setText(EventTypeName);
                        typeNodeId = EventTypeId;
                    }
                });
                break;
            //所属网络
            case R.id.txt_gridValue:
                Jump_intent(MapSelectActivity.class,bundle);
                break;
            //开始时间
            case R.id.txt_startTime:
                timeType = 0;
                pvTime.show();
                break;
            //结束时间
            case R.id.txt_endTime:
                timeType = 1;
                pvTime.show();
                break;
            //清空事件
            case R.id.btn_clear:
                txt_typeName.setText("");
                txt_bigTypeName.setText("");
                txt_smallTypeName.setText("");
                txt_nodeName.setText("");
                txt_gridValue.setText("");
                txt_startTime.setText("");
                txt_endTime.setText("");
                break;
            //查询跳转页面
            case R.id.btn_search:
                bundle.putString("eventType","事件");
                Jump_intent(EventOverviewListActivity.class,bundle);
                break;
        }
    }
}
