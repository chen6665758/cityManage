package com.cg.citymanage.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cg.citymanage.R;
import com.cg.citymanage.models.ClockSearchListModel;

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

* 功能：考勤打卡，历史记录adatper
* 作者：cg
* 时间：2019/11/19 10:10
*/
public class ClockSearchAdapter extends RecyclerView.Adapter<ClockSearchAdapter.myViewHolder> {

    private Context mContext;
    private List<ClockSearchListModel> list_data;
    private LayoutInflater inflater;

    public ClockSearchAdapter(Context mContext, List<ClockSearchListModel> list_data) {
        this.mContext = mContext;
        this.list_data = list_data;
        this.inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.activity_clock_search_list_item,viewGroup,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {

        myViewHolder.txt_clockTime.setText(list_data.get(i).getClockTime());
        myViewHolder.txt_chockIn.setText(list_data.get(i).getClockIn());
        myViewHolder.txt_instatus.setText(list_data.get(i).getClockInStatus());
        if("正常".equals(list_data.get(i).getClockInStatus()))
        {
            myViewHolder.txt_instatus.setTextColor(Color.parseColor("#07aa39"));
        }else{
            myViewHolder.txt_instatus.setTextColor(Color.parseColor("#d81e06"));
        }
        myViewHolder.txt_chockOut.setText(list_data.get(i).getClockOut());
        myViewHolder.txt_outstatus.setText(list_data.get(i).getClockOutStatus());
        if("正常".equals(list_data.get(i).getClockOutStatus()))
        {
            myViewHolder.txt_outstatus.setTextColor(Color.parseColor("#07aa39"));
        }else{
            myViewHolder.txt_outstatus.setTextColor(Color.parseColor("#d81e06"));
        }
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_clockTime;
        TextView txt_chockIn;
        TextView txt_instatus;
        TextView txt_chockOut;
        TextView txt_outstatus;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_clockTime = (TextView)itemView.findViewById(R.id.txt_clockTime);
            txt_chockIn = (TextView)itemView.findViewById(R.id.txt_chockIn);
            txt_instatus = (TextView)itemView.findViewById(R.id.txt_instatus);
            txt_chockOut = (TextView)itemView.findViewById(R.id.txt_chockOut);
            txt_outstatus = (TextView) itemView.findViewById(R.id.txt_outstatus);
        }
    }
}
