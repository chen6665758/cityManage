package com.cg.citymanage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cg.citymanage.Adapters.EventReporterListAdpater;
import com.cg.citymanage.models.EventReporterModel;
import com.cg.citymanage.untils.OnViewGlobalLayoutListener;

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

* 功能：事件中选择接收人弹窗
* 作者：cg
* 时间：2019/10/25 13:40
*/
public class EventReceiverSelectDialogFragment  extends DialogFragment {

    private RecyclerView rv_report;
    private List<EventReporterModel> list_data;
    private EventReporterListAdpater eAdapter;
    private Button btn_transmit;

    public static EventReceiverSelectDialogFragment newInstance() {

        EventReceiverSelectDialogFragment b = new EventReceiverSelectDialogFragment();
        Bundle args = new Bundle();
        b.setArguments(args);
        return b;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialogfragment_event_report,container,false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout linear_eventreport = view.findViewById(R.id.linear_eventreport);
        linear_eventreport.getViewTreeObserver().addOnGlobalLayoutListener(new OnViewGlobalLayoutListener(view, 1000));

        rv_report = (RecyclerView)view.findViewById(R.id.rv_report);
        rv_report.setLayoutManager(new LinearLayoutManager(getContext()));
        list_data = new ArrayList<>();
        temp();
        eAdapter = new EventReporterListAdpater(getContext(),list_data);
        rv_report.setAdapter(eAdapter);
        eAdapter.setOnItemClickLitener(new EventReporterListAdpater.OnItemClickLitener() {
            @Override
            public void OnItemClick(View view, int positon) {

                list_data.get(positon).setSel(!list_data.get(positon).isSel());
                eAdapter.notifyItemChanged(positon);
            }
        });

        btn_transmit = (Button)view.findViewById(R.id.btn_transmit);
        btn_transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = "";
                for(EventReporterModel model : list_data)
                {
                    if(model.isSel())
                    {
                        temp += model.getReporterName() + ",";
                    }
                }

                mOnItemClickLitener.OnItemClick(v,temp);
                dismiss();
            }
        });
    }

    /**
     * 定义点击每项的接口
     */
    public interface OnItemClickLitener
    {
        void OnItemClick(View view, String str);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    private void temp()
    {
        for(int i=0;i<10;i++)
        {
            EventReporterModel model = new EventReporterModel();
            model.setReporterId(String.valueOf(i+1));
            model.setReporterName("传阅人_" + i);
            model.setSel(false);

            list_data.add(model);
        }
    }
}
