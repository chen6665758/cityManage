package com.cg.citymanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.citymanage.Adapters.EventOverviewListAdpater;
import com.cg.citymanage.models.EventListModel;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

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

* 功能：待办事件
* 作者：cg
* 时间：2019/10/29 9:59
*/
public class EventWaitActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 列表
     */
    private PullLoadMoreRecyclerView pmr_event;
    private List<EventListModel> list_data;
    private EventOverviewListAdpater eAdapter;
    private int pageNo = 1;                        //当前页，从1开始

    /**
     * 无数据时显示
     */
    private RelativeLayout rela_NoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_event_wait);
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

        //列表
        pmr_event = (PullLoadMoreRecyclerView)findViewById(R.id.pmr_event);
        pmr_event.setLinearLayout();
        list_data = new ArrayList<>();
        eAdapter = new EventOverviewListAdpater(list_data,mContext);
        pmr_event.setAdapter(eAdapter);

        pmr_event.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                pageNo = 1;
                list_data.clear();
            }

            @Override
            public void onLoadMore() {
                //上拉加载更多
            }
        });

        eAdapter.setOnItemClickLitener(new EventOverviewListAdpater.OnItemClickLitener() {
            @Override
            public void OnItemClick(View view, int positon) {

                bundle.putString("eventId",list_data.get(positon).getEventId());
                Jump_intent(EventWaitDetailActivity.class,bundle);
            }
        });

        //无数据时显示
        rela_NoData = (RelativeLayout)findViewById(R.id.rela_NoData);

        tempData();
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
        }
    }

    public void tempData()
    {
        for(int i=0;i<10;i++)
        {
            EventListModel model = new EventListModel();
            model.setEventId(String.valueOf(i+1));
            model.setEventName("待办第" + i + "事件");
            model.setEventTime("2019-10-" + (i+1));
            model.setEventLink("结果反馈" + i);
            model.setEventInfo("203032019102100"+i+":施工废弃料");
            list_data.add(model);
        }

        eAdapter.notifyDataSetChanged();
    }
}
