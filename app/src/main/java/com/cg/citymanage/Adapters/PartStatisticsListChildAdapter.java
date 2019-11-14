package com.cg.citymanage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cg.citymanage.R;
import com.cg.citymanage.models.PartListModel;

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

* 功能：部件统计子类列表
* 作者：cg
* 时间：2019/11/14 15:57
*/
public class PartStatisticsListChildAdapter extends BaseAdapter {

    private Context mContext;
    private List<PartListModel> list_data;
    private LayoutInflater inflater;

    public PartStatisticsListChildAdapter(Context mContext, List<PartListModel> list_data) {
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
        myHolder mHolder;
        if(convertView == null)
        {
            mHolder = new myHolder();
            convertView = inflater.inflate(R.layout.activity_part_statistics_list_child_item,parent,false);
            mHolder.txt_unitTypeName = (TextView)convertView.findViewById(R.id.txt_unitTypeName);
            mHolder.txt_totalCount = (TextView)convertView.findViewById(R.id.txt_totalCount);

            convertView.setTag(mHolder);
        }else{
            mHolder = (myHolder)convertView.getTag();
        }

        mHolder.txt_unitTypeName.setText(list_data.get(position).getUnitTypeName());
        mHolder.txt_totalCount.setText(list_data.get(position).getTotalCount());


        return convertView;
    }

    class myHolder{
        TextView txt_unitTypeName;
        TextView txt_totalCount;
    }
}
