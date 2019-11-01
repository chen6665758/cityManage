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
import com.cg.citymanage.models.EventImpatientListModel;

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

* 功能：事件中页中催办流程列表Adapter
* 作者：cg
* 时间：2019/10/25 10:27
*/
public class EventDetailImpatientListAdpater extends BaseAdapter {

    private Context mContext;
    private List<EventImpatientListModel> list_data;
    private LayoutInflater inflater;

    public EventDetailImpatientListAdpater(Context mContext, List<EventImpatientListModel> list_data) {
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
            convertView = inflater.inflate(R.layout.activity_event_detail_impatient_listitem,parent,false);
            mHolder.img_flag = (ImageView)convertView.findViewById(R.id.img_flag);
            mHolder.txt_eventLink = (TextView)convertView.findViewById(R.id.txt_eventLink);
            mHolder.txt_eventImpatienter = (TextView)convertView.findViewById(R.id.txt_eventImpatienter);
            mHolder.txt_eventpImpatienter = (TextView)convertView.findViewById(R.id.txt_eventpImpatienter);
            mHolder.txt_eventHandleTime = (TextView)convertView.findViewById(R.id.txt_eventHandleTime);
            mHolder.txt_eventInfo = (TextView)convertView.findViewById(R.id.txt_eventInfo);
            mHolder.txt_btnDetail = (TextView)convertView.findViewById(R.id.txt_btnDetail);

            convertView.setTag(mHolder);
        }else{
            mHolder =(myViewHolder)convertView.getTag();
        }

        mHolder.txt_eventImpatienter.setText(list_data.get(position).getEventImpatienter());
        mHolder.txt_eventLink.setText(list_data.get(position).getEventImpatientLink());
        mHolder.txt_eventpImpatienter.setText(list_data.get(position).getEventpImpatienter());
        mHolder.txt_eventHandleTime.setText(list_data.get(position).getEventImpatientTime());
        mHolder.txt_eventInfo.setText(list_data.get(position).getEventImpatientInfo());

        if(position == 0)
        {
            mHolder.img_flag.setBackgroundResource(R.drawable.ico_eventflow1);
        }else{
            mHolder.img_flag.setBackgroundResource(R.drawable.ico_eventflow2);
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
        private TextView txt_eventLink;
        private TextView txt_eventImpatienter;
        private TextView txt_eventpImpatienter;
        private TextView txt_eventHandleTime;
        private TextView txt_eventInfo;
        private TextView txt_btnDetail;
    }
}
