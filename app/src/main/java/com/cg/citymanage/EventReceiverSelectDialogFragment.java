package com.cg.citymanage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cg.citymanage.Adapters.EventReporterListAdpater;
import com.cg.citymanage.customs.MyLoading;
import com.cg.citymanage.infos.Constants;
import com.cg.citymanage.models.EventReporterModel;
import com.cg.citymanage.untils.OnViewGlobalLayoutListener;
import com.cg.citymanage.untils.myUntils;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

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

* 功能：事件中选择接收人弹窗
* 作者：cg
* 时间：2019/10/25 13:40
*/
public class EventReceiverSelectDialogFragment  extends DialogFragment {

    private RecyclerView rv_report;
    private List<EventReporterModel> list_data;
    private EventReporterListAdpater eAdapter;
    private Button btn_transmit;

    private String appToken;
    private String btnName;    //按钮显示的文字
    private int type;          //类别，0：事件中选择接收人
    private String getIds;     //用户已经选择的接收人id
    private String[] arrayIds; //将传过来的接收人id,转化为列表

    public MyLoading progress_Dialog;

    public static EventReceiverSelectDialogFragment newInstance(String appToken,String btnName,int type,String getIds) {

        EventReceiverSelectDialogFragment b = new EventReceiverSelectDialogFragment();
        Bundle args = new Bundle();
        args.putString("appToken",appToken);
        args.putString("btnName",btnName);
        args.putInt("type",type);
        args.putString("getIds",getIds);
        b.setArguments(args);
        return b;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appToken = getArguments().getString("appToken");
        btnName = getArguments().getString("btnName");
        type = getArguments().getInt("type");
        getIds = getArguments().getString("getIds");
        if(!TextUtils.isEmpty(getIds))
        {
            if(getIds.contains(","))
            {
                arrayIds = getIds.split(",");
            }else{
                arrayIds = new String[]{getIds};
            }
        }else{
            arrayIds = new String[]{};
        }

        progress_Dialog = MyLoading.createDialog(getContext());
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
        //temp();
        eAdapter = new EventReporterListAdpater(getContext(),list_data);
        rv_report.setAdapter(eAdapter);
        eAdapter.setOnItemClickLitener(new EventReporterListAdpater.OnItemClickLitener() {
            @Override
            public void OnItemClick(View view, int positon) {

                list_data.get(positon).setSel(!list_data.get(positon).isSel());
                eAdapter.notifyItemChanged(positon);
            }
        });
        initEvenData();

        btn_transmit = (Button)view.findViewById(R.id.btn_transmit);
        btn_transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempId = "";
                String tempName ="";
                for(EventReporterModel model : list_data)
                {
                    if(model.isSel())
                    {
                        tempId += model.getReporterId() + ",";
                        tempName += model.getReporterName() + ",";
                    }
                }


                String Ids = "";
                String Names = "";
                if(!TextUtils.isEmpty(tempId))
                {
                    Ids = tempId.substring(0, tempId.length() - 1);
                }
                if(!TextUtils.isEmpty(tempName))
                {
                    Names = tempName.substring(0, tempName.length() - 1);
                }

                mOnItemClickLitener.OnItemClick(v, Ids,Names);
                dismiss();

            }
        });
    }

    /**
     * 定义点击每项的接口
     */
    public interface OnItemClickLitener
    {
        void OnItemClick(View view, String id,String name);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    /**
     * 加载事件的详细信息
     */
    private void initEvenData(){

        progress_Dialog.show();

        OkGo.<String>post(Constants.EVENTRANSMITER_URL)
                .tag(this)//
                .params("access_token", appToken)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

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
                                    for(int i=0;i<array.length();i++)
                                    {
                                        JSONObject object = array.getJSONObject(i);
                                        EventReporterModel model = new EventReporterModel();
                                        model.setReporterId(object.getString("id"));
                                        model.setReporterName(object.getString("name"));
                                        if(arrayIds.length > 0) {
                                            for (int j = 0; j < arrayIds.length; j++) {
                                                if (arrayIds[j].equals(object.getString("id"))) {
                                                    model.setSel(true);
                                                }
                                            }
                                        }

                                        list_data.add(model);
                                    }
                                    eAdapter.notifyDataSetChanged();
                                }
                            }else{
                                myUntils.showToast(getContext(),json.getString("message"));
                            }


                        }catch (Exception ex)
                        {
                            Log.e("EventReceiveSelect", "行数: 187  ex:" + ex.getMessage());
                            myUntils.showToast(getContext(),"请检查网络是否正常链接！");
                            return;
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        progress_Dialog.dismiss();
                        Log.e("EventReceiveSelect", "行数: 196  error:" + response.body());
                    }
                });
    }

}
