package com.cg.citymanage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.citymanage.Adapters.EventOverviewListAdpater;
import com.cg.citymanage.infos.Constants;
import com.cg.citymanage.models.EventListModel;
import com.cg.citymanage.untils.StringUtil;
import com.cg.citymanage.untils.myUntils;
import com.google.gson.JsonArray;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

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

* 功能：参与事件列表显示
* 作者：cg
* 时间：2019/10/25 15:07
*/
public class EventPartakeActivity extends BaseActivity implements View.OnClickListener {

    private String appToken;

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
        appToken = mSharedPreferences.getString("appToken","");

        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_event_partake);
    }

    /**
     * 初始化控件
     */
    private void initControls() {

        //标题栏
        title_left_btn = (ImageButton) findViewById(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_textview = (TextView) findViewById(R.id.title_textview);
        title_textview.setText("参与事件");

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
                initData();
            }

            @Override
            public void onLoadMore() {
                //上拉加载更多
                pageNo++;
                initData();
            }
        });

        eAdapter.setOnItemClickLitener(new EventOverviewListAdpater.OnItemClickLitener() {
            @Override
            public void OnItemClick(View view, int positon) {

                bundle.putString("eventId",list_data.get(positon).getEventId());
                Jump_intent(EventPartakeDetailActivity.class,bundle);
            }
        });

        //无数据时显示
        rela_NoData = (RelativeLayout)findViewById(R.id.rela_NoData);

        //tempData();

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData()
    {
        progress_Dialog.show();
        OkGo.<String>post(Constants.EVENTPARTAKELIST_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("page",String.valueOf(pageNo))
                .params("pageSize",Constants.PAGESIZE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        progress_Dialog.dismiss();
                        String data = response.body();//这个就是返回来的结果
                        try {
                            JSONObject json = new JSONObject(data);
                            String resultCode = json.getString("code");
                            if(resultCode.equals("2000"))
                            {
                                JSONArray array = json.getJSONArray("data");
                                if(array.length() > 0)
                                {
                                    for(int i = 0;i<array.length();i++) {
                                        JSONObject child = array.getJSONObject(i);
                                        EventListModel model = new EventListModel();
                                        model.setEventId(child.getString("id"));
                                        model.setEventName(child.getString("eventTitle"));
                                        model.setEventTime(TextUtils.isEmpty(child.getString("createTime")) ? "" :
                                                myUntils.StringPattern(child.getString("createTime"),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd"));
                                        model.setEventInfo(child.getString("eventCode"));
                                        model.setEventLink("");
                                        model.setImpatient(false);
                                        list_data.add(model);
                                    }
                                    eAdapter.notifyDataSetChanged();

                                }

                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }

                            //根据数据数量，来判断是否给出无数据提示
                            if(list_data.size() > 0)
                            {
                                rela_NoData.setVisibility(View.GONE);
                            }else{
                                rela_NoData.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception ex)
                        {
                            Log.e("EventPartake", "行数: 196  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");
                            return;
                        }

                        pmr_event.setPullLoadMoreCompleted();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        progress_Dialog.dismiss();
                        Log.e("EventPartake", "行数: 208  error:" + response.body());
                    }
                });
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
            model.setEventName("参与第" + i + "事件");
            model.setEventTime("2019-10-" + (i+1));
            model.setEventLink("结果反馈" + i);
            model.setEventInfo("203032019102100"+i+":参与事件");
            list_data.add(model);
        }

        eAdapter.notifyDataSetChanged();
    }
}
