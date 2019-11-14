package com.cg.citymanage.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cg.citymanage.R;
import com.cg.citymanage.models.PartListModel;

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

* 功能：部件统计主列表
* 作者：cg
* 时间：2019/11/14 15:17
*/
public class PartStatisticsListMainAdapter extends BaseAdapter {

    private Context mContext;
    private List<PartListModel> list_data;
    private LayoutInflater inflater;

    public PartStatisticsListMainAdapter(Context mContext, List<PartListModel> list_data) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final myHolder mHolder;
        if(convertView == null)
        {
            mHolder = new myHolder();
            convertView = inflater.inflate(R.layout.activity_part_statistics_list_main_item,parent,false);
            mHolder.txt_unitTypeName = (TextView)convertView.findViewById(R.id.txt_unitTypeName);
            mHolder.txt_totalCount = (TextView)convertView.findViewById(R.id.txt_totalCount);
            mHolder.lv_childp = (ListView)convertView.findViewById(R.id.lv_childp);

            convertView.setTag(mHolder);
        }else{
            mHolder = (myHolder)convertView.getTag();
        }

        mHolder.txt_unitTypeName.setText(list_data.get(position).getUnitTypeName());
        mHolder.txt_totalCount.setText(list_data.get(position).getTotalCount());

        List<PartListModel> list_c = new ArrayList<>();
        if(list_data.get(position).getChildren().size() > 0)
        {
            for(int i=0;i<list_data.get(position).getChildren().size();i++)
            {
                list_c.add(list_data.get(position).getChildren().get(i));
            }
        }
        PartStatisticsListChildAdapter cAdapter = new PartStatisticsListChildAdapter(mContext,list_c);
        mHolder.lv_childp.setAdapter(cAdapter);
        setListViewHeightBasedOnChildren(mHolder.lv_childp,cAdapter);
        if(list_c.size() > 0)
        {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("PartStatisticsListMainAdapter.java(onClick)", "行数: 94  值：" + list_data.get(position).getUnitTypeName());
                    if(mHolder.lv_childp.getVisibility() == View.VISIBLE)
                    {
                        mHolder.lv_childp.setVisibility(View.GONE);
                    }else{
                        mHolder.lv_childp.setVisibility(View.VISIBLE);
                    }
                }
            });
        }



        return convertView;
    }

    class myHolder
    {
        TextView txt_unitTypeName;
        TextView txt_totalCount;
        ListView lv_childp;
    }


    public static void setListViewHeightBasedOnChildren(ListView listView,PartStatisticsListChildAdapter cAdapter) {
        PartStatisticsListChildAdapter listAdapter = cAdapter;
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
