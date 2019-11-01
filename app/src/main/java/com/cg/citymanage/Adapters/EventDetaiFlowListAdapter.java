package com.cg.citymanage.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cg.citymanage.EventPicViewActivity;
import com.cg.citymanage.R;
import com.cg.citymanage.models.EventFlowListModel;

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

* 功能： 事件详情页中事件流程列表Adapter
* 作者：cg
* 时间：2019/10/24 13:55
*/
public class EventDetaiFlowListAdapter extends BaseAdapter {

    private Context mContext;
    private List<EventFlowListModel> list_data;
    private LayoutInflater inflater;

    public EventDetaiFlowListAdapter(Context mContext, List<EventFlowListModel> list_data) {
        this.mContext = mContext;
        this.list_data = list_data;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list_data.size();
    }

    @Override
    public Object getItem(int position) {
        return list_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        myViewHolder mHolder;
        if(convertView == null)
        {
            mHolder = new myViewHolder();
            convertView = inflater.inflate(R.layout.activity_event_detail_flow_listitem,parent,false);
            mHolder.img_flag = (ImageView)convertView.findViewById(R.id.img_flag);
            mHolder.txt_eventFlowHandler = (TextView)convertView.findViewById(R.id.txt_eventFlowHandler);
            mHolder.txt_eventFlowHandleTime = (TextView)convertView.findViewById(R.id.txt_eventFlowHandleTime);
            mHolder.txt_eventFlowInfo = (TextView)convertView.findViewById(R.id.txt_eventFlowInfo);
            mHolder.txt_eventFlowLink = (TextView)convertView.findViewById(R.id.txt_eventFlowLink);
            mHolder.txt_eventFlowStreet = (TextView)convertView.findViewById(R.id.txt_eventFlowStreet);
            mHolder.txt_btnDetail = (TextView)convertView.findViewById(R.id.txt_btnDetail);

            convertView.setTag(mHolder);
        }else{
            mHolder =(myViewHolder)convertView.getTag();
        }

        mHolder.txt_eventFlowStreet.setText(list_data.get(position).getEventStreet());
        mHolder.txt_eventFlowLink.setText(list_data.get(position).getEventFlowLink());
        mHolder.txt_eventFlowInfo.setText(list_data.get(position).getEventFlowInfo());
        mHolder.txt_eventFlowHandler.setText(list_data.get(position).getEventFlowHandler());
        mHolder.txt_eventFlowHandleTime.setText(list_data.get(position).getEventFlowHandleTime());

        if(position == 0)
        {
            mHolder.img_flag.setImageResource(R.drawable.ico_eventflow1);
        }else{
            mHolder.img_flag.setImageResource(R.drawable.ico_eventflow2);
        }

        mHolder.txt_btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext,EventPicViewActivity.class);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    class myViewHolder
    {
        ImageView img_flag;
        private TextView txt_eventFlowLink;
        private TextView txt_eventFlowStreet;
        private TextView txt_eventFlowHandler;
        private TextView txt_eventFlowHandleTime;
        private TextView txt_eventFlowInfo;
        private TextView txt_btnDetail;
    }
}
