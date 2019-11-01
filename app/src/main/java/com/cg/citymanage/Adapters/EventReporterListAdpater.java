package com.cg.citymanage.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cg.citymanage.R;
import com.cg.citymanage.models.EventReporterModel;

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

* 功能：事件中选择传递人列表Adapter
* 作者：cg
* 时间：2019/10/25 13:56
*/
public class EventReporterListAdpater extends RecyclerView.Adapter<EventReporterListAdpater.myViewHolder> {

    private Context mContext;
    private List<EventReporterModel> list_data;
    private LayoutInflater inflater;

    public EventReporterListAdpater(Context mContext, List<EventReporterModel> list_data) {
        this.mContext = mContext;
        this.list_data = list_data;
        this.inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.dialogfragment_report_listitem,viewGroup,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i) {

        myViewHolder.txt_reportName.setText(list_data.get(i).getReporterName());
        if(list_data.get(i).isSel())
        {
            myViewHolder.img_reportsel.setImageResource(R.mipmap.ico_selected);
        }else{
            myViewHolder.img_reportsel.setImageResource(R.mipmap.ico_select);
        }

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = myViewHolder.getLayoutPosition();
                    mOnItemClickLitener.OnItemClick(myViewHolder.itemView, pos);
                }
            });


        }
    }


    @Override
    public int getItemCount() {
        return list_data.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img_reportsel;
        TextView txt_reportName;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img_reportsel = (ImageView)itemView.findViewById(R.id.img_reportsel);
            txt_reportName = (TextView)itemView.findViewById(R.id.txt_reportName);
        }
    }

    /**
     * 定义点击每项的接口
     */
    public interface OnItemClickLitener
    {
        void OnItemClick(View view, int positon);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
