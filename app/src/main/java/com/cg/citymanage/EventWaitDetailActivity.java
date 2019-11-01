package com.cg.citymanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.citymanage.Adapters.EventDetaiFlowListAdapter;
import com.cg.citymanage.Adapters.EventDetailImpatientListAdpater;
import com.cg.citymanage.customs.ListViewForScrollView;
import com.cg.citymanage.models.EventFlowListModel;
import com.cg.citymanage.models.EventImpatientListModel;

import java.util.ArrayList;
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

* 功能：待办事件，详情页
* 作者：cg
* 时间：2019/10/29 10:13
*/
public class EventWaitDetailActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 详细信息
     */
    private TextView txt_eventCodeName;
    private TextView txt_eventypeName;
    private TextView txt_reportName;
    private TextView txt_siteValue;
    private TextView txt_timeValue;
    private TextView txt_infoValue;

    /**
     * 事件流程
     */
    private RelativeLayout rela_detail;
    private ImageView img_detail;

    private ListViewForScrollView lv_eventdetail;
    private List<EventFlowListModel> list_data;
    private EventDetaiFlowListAdapter eAdapter;

    /**
     * 催办流程
     */
    private RelativeLayout rela_Impatientdetail;
    private ImageView img_Impatientdetail;

    private ListViewForScrollView lv_eventImpatientdetail;
    private List<EventImpatientListModel> list_idata;
    private EventDetailImpatientListAdpater eiAdapter;

    /**
     * 操作按钮
     */
    private Button btn_stop;
    private Button btn_proposal;
    private Button btn_close;
    private Button btn_Filing;
    private Button btn_back;
    private Button btn_notice;
    private Button btn_dispatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_event_wait_detail);
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
        title_textview.setText("待办事件");

        //详细信息
        txt_eventCodeName = (TextView)findViewById(R.id.txt_eventCodeName);
        txt_eventypeName = (TextView)findViewById(R.id.txt_eventypeName);
        txt_reportName = (TextView)findViewById(R.id.txt_reportName);
        txt_siteValue = (TextView)findViewById(R.id.txt_siteValue);
        txt_timeValue = (TextView)findViewById(R.id.txt_timeValue);
        txt_infoValue = (TextView)findViewById(R.id.txt_infoValue);

        //事件流程
        rela_detail = (RelativeLayout)findViewById(R.id.rela_detail);
        rela_detail.setOnClickListener(this);
        img_detail = (ImageView)findViewById(R.id.img_detail);

        lv_eventdetail = (ListViewForScrollView)findViewById(R.id.lv_eventdetail);
        list_data = new ArrayList<>();
        temp();
        eAdapter = new EventDetaiFlowListAdapter(mContext,list_data);
        lv_eventdetail.setAdapter(eAdapter);

        //催办流程
        rela_Impatientdetail = (RelativeLayout)findViewById(R.id.rela_Impatientdetail);
        rela_Impatientdetail.setOnClickListener(this);
        img_Impatientdetail = (ImageView)findViewById(R.id.img_Impatientdetail);

        lv_eventImpatientdetail = (ListViewForScrollView)findViewById(R.id.lv_eventImpatientdetail);
        list_idata = new ArrayList<>();
        temp1();
        eiAdapter = new EventDetailImpatientListAdpater(mContext,list_idata);
        lv_eventImpatientdetail.setAdapter(eiAdapter);

        //操作按钮
        btn_stop = (Button)findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(this);
        btn_proposal = (Button)findViewById(R.id.btn_proposal);
        btn_proposal.setOnClickListener(this);
        btn_close = (Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);
        btn_Filing = (Button)findViewById(R.id.btn_Filing);
        btn_Filing.setOnClickListener(this);
        btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_notice = (Button)findViewById(R.id.btn_notice);
        btn_notice.setOnClickListener(this);
        btn_dispatch = (Button)findViewById(R.id.btn_dispatch);
        btn_dispatch.setOnClickListener(this);
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
            //返回
            case R.id.title_left_btn:
                finish();
                break;
            //是否显示事件流程详情
            case R.id.rela_detail:
                if (lv_eventdetail.getVisibility() == View.VISIBLE) {
                    img_detail.setImageResource(R.mipmap.ico_eventflowdown);
                    lv_eventdetail.setVisibility(View.GONE);
                } else {
                    img_detail.setImageResource(R.mipmap.ico_eventflowup);
                    lv_eventdetail.setVisibility(View.VISIBLE);
                }
                break;
            //是否显示事件流程详情
            case R.id.rela_Impatientdetail:
                if (lv_eventImpatientdetail.getVisibility() == View.VISIBLE) {
                    img_Impatientdetail.setImageResource(R.mipmap.ico_eventflowdown);
                    lv_eventImpatientdetail.setVisibility(View.GONE);
                } else {
                    img_Impatientdetail.setImageResource(R.mipmap.ico_eventflowup);
                    lv_eventImpatientdetail.setVisibility(View.VISIBLE);
                }
                break;
            //不实关停
            case R.id.btn_stop:

                break;
            //属实提案
            case R.id.btn_proposal:

                break;
            //申请结案
            case R.id.btn_close:

                break;
            //立案
            case R.id.btn_Filing:

                break;
            //退回
            case R.id.btn_back:

                break;
            //通知协办
            case R.id.btn_notice:

                break;
            //派遣执法
            case R.id.btn_dispatch:

                break;
        }
    }


    public void temp()
    {
        for(int i=0;i<5;i++)
        {
            EventFlowListModel model = new EventFlowListModel();
            model.setEventFlowId(String.valueOf(i+1));
            model.setEventFlowLink("环节" + i);
            model.setEventFlowHandler("处理人" + i);
            model.setEventStreet("处理街巷" + i);
            model.setEventFlowHandleTime("处理时间" + i);
            model.setEventFlowInfo("处理意见" + i);
            list_data.add(model);
        }
    }

    public void temp1(){
        for(int i = 0;i<5;i++)
        {
            EventImpatientListModel model = new EventImpatientListModel();
            model.setEventImpatientId(String.valueOf(i+1));
            model.setEventImpatientLink("催办环节" + i);
            model.setEventImpatienter("催办人" + i);
            model.setEventpImpatienter("被催办人" + i);
            model.setEventImpatientTime("催办时间" + i);
            model.setEventImpatientInfo("处理意见" + i);
            list_idata.add(model);
        }
    }
}
