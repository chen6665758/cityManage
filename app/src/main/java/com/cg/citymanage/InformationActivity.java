package com.cg.citymanage;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.citymanage.Adapters.InformationListAdapter;
import com.cg.citymanage.infos.Constants;
import com.cg.citymanage.models.InformationListModel;
import com.cg.citymanage.untils.LiveDataBus;
import com.cg.citymanage.untils.myUntils;
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

* 功能：信息列表页
* 作者：cg
* 时间：2019/11/4 13:13
*/
public class InformationActivity extends BaseActivity implements View.OnClickListener {

    private String appToken;

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 列表
     */
    private PullLoadMoreRecyclerView pmr_info;
    private List<InformationListModel> list_data;
    private InformationListAdapter iAdapter;
    private int pageNo = 1;                        //当前页，从1开始

    /**
     * 添加按钮
     */
    private ImageView img_add;

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

        LiveDataBus.get().with("Information", Boolean.class)
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {

                        if(aBoolean != null) {
                            if (aBoolean) {
                                pageNo = 1;
                                list_data.clear();
                                initData();
                            }
                        }
                    }
                });
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_information);
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
        title_textview.setText("信息");

        //列表
        pmr_info = (PullLoadMoreRecyclerView)findViewById(R.id.pmr_info);
        pmr_info.setLinearLayout();
        list_data = new ArrayList<>();
        //temp();
        iAdapter = new InformationListAdapter(mContext,list_data);
        pmr_info.setAdapter(iAdapter);

        pmr_info.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
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

        iAdapter.setOnItemClickLitener(new InformationListAdapter.OnItemClickLitener() {
            @Override
            public void OnItemClick(View view, int positon) {

                bundle.putString("messId",list_data.get(positon).getInfoId());
                Jump_intent(InformationDetailActivity.class,bundle);
            }
        });

        //添加按钮
        img_add = (ImageView)findViewById(R.id.img_add);
        img_add.setOnClickListener(this);

        //无数据时显示
        rela_NoData = (RelativeLayout)findViewById(R.id.rela_NoData);

        initData();
    }


    /**
     * 初始化数据
     */
    private void initData()
    {
        progress_Dialog.show();
        OkGo.<String>post(Constants.INFORMATIONLIST_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("page",String.valueOf(pageNo))
                .params("pageSize",Constants.PAGESIZE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        progress_Dialog.dismiss();
                        String data = response.body();
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
                                        InformationListModel model = new InformationListModel();
                                        model.setInfoId(child.getString("messId"));
                                        model.setInfoTitle(child.getString("messageTitle"));
                                        model.setInfoTime(child.getString("sendTime"));
//                                        model.setEventInfo(child.getString("eventCode") + " " + child.getString("eventTypeName"));
//                                        model.setEventLink(child.getString("eventStatus"));

                                        list_data.add(model);
                                    }
                                    iAdapter.notifyDataSetChanged();

                                }else{
                                    myUntils.showToast(mContext,"没有新的消息了！");
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
                            Log.e("Information", "行数: 211  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");
                            return;
                        }

                        pmr_info.setPullLoadMoreCompleted();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        progress_Dialog.dismiss();
                        Log.e("Information", "行数: 223  error:" + response.body());
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
            //添加信息
            case R.id.img_add:
                bundle.putString("status","0");
                Jump_intent(InformationReplyActivity.class,bundle);
                break;
        }
    }

}
