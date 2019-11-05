package com.cg.citymanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.citymanage.Adapters.InformationListAdapter;
import com.cg.citymanage.models.InformationListModel;
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

* 功能：信息列表页
* 作者：cg
* 时间：2019/11/4 13:13
*/
public class InformationActivity extends BaseActivity implements View.OnClickListener {

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

        initControls();
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
        temp();
        iAdapter = new InformationListAdapter(mContext,list_data);
        pmr_info.setAdapter(iAdapter);

        pmr_info.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
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

        iAdapter.setOnItemClickLitener(new InformationListAdapter.OnItemClickLitener() {
            @Override
            public void OnItemClick(View view, int positon) {

                bundle.putString("eventId",list_data.get(positon).getInfoId());
                Jump_intent(InformationDetailActivity.class,bundle);
            }
        });

        //添加按钮
        img_add = (ImageView)findViewById(R.id.img_add);
        img_add.setOnClickListener(this);

        //无数据时显示
        rela_NoData = (RelativeLayout)findViewById(R.id.rela_NoData);
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
                Jump_intent(InformationReplyActivity.class,bundle);
                break;
        }
    }


    private void temp()
    {
        for(int i=0;i<10;i++)
        {
            InformationListModel model = new InformationListModel();
            model.setInfoId(String.valueOf(i+1));
            model.setInfoTitle("我是标题" + i);
            model.setInfoTime("2019-11-" + (i + 1));
            list_data.add(model);
        }
    }
}
