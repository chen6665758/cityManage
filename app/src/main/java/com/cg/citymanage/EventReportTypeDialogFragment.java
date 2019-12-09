package com.cg.citymanage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cg.citymanage.Adapters.EventReportTypeAdapter;
import com.cg.citymanage.infos.Constants;
import com.cg.citymanage.models.EventTypeModel;
import com.cg.citymanage.untils.OnViewGlobalLayoutListener;
import com.cg.citymanage.untils.myUntils;
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

* 功能：事件分类、事件大类、事件小类选择时的弹出框
* 作者：cg
* 时间：2019/10/29 13:23
*/
public class EventReportTypeDialogFragment extends DialogFragment {

    private String ertype;                 //事件上报中事件的类别，0：事件分类  1：事件大类  2：事件小类 3:待办事件选人
    private String parentType;             //父类id
    private String appToken;
    private String btnName;    //按钮显示的文字


    private ListView lv_er;
    private List<EventTypeModel> list_data;
    private EventReportTypeAdapter eAdapter;

    public static EventReportTypeDialogFragment newInstance(String ertype,String parentType,String appToken,String btnName) {

        EventReportTypeDialogFragment b = new EventReportTypeDialogFragment();
        Bundle args = new Bundle();
        args.putString("ertype", ertype);
        args.putString("parentType", parentType);
        args.putString("appToken",appToken);
        args.putString("btnName",btnName);
        b.setArguments(args);
        return b;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);

        ertype = getArguments().getString("ertype");
        parentType = getArguments().getString("parentType");
        appToken = getArguments().getString("appToken");
        btnName = getArguments().getString("btnName");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialogfragment_eventreport,null);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout linear_eventreport = view.findViewById(R.id.linear_eventreport);
        linear_eventreport.getViewTreeObserver().addOnGlobalLayoutListener(new OnViewGlobalLayoutListener(view, 600));

        lv_er = (ListView)view.findViewById(R.id.lv_er);
        list_data = new ArrayList<>();
        //tempData();
        eAdapter = new EventReportTypeAdapter(getContext(),list_data);
        lv_er.setAdapter(eAdapter);
        lv_er.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mOnItemClickLitener.OnItemClick(view,list_data.get(position).getEventTypeId(),list_data.get(position).getEventTypeName());
                dismiss();
            }
        });

        initData();

    }

    /**
     * 加载数据
     */
    private void initData()
    {
        if("3".equals(ertype))
        {
            initEvenData();
        }else if("0".equals(ertype) || "1".equals(ertype) || "2".equals(ertype)){
            initEventType();
        }else if("4".equals(ertype))
        {
            initNoteType();
        }else if("5".equals(ertype))
        {
            initMail();
        }
    }

    /**
     * 加载待办事件的处理部门
     */
    private void initEvenData(){

        OkGo.<String>post(Constants.EVENTWAITDEP_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("outcome",btnName)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String data = response.body();//这个就是返回来的结果
                        try {
                            JSONObject json = new JSONObject(data);
                            String resultCode = json.getString("code");
                            if(resultCode.equals("2000"))
                            {
                                JSONObject childData = json.getJSONObject("data");
                                JSONArray array = childData.getJSONArray("personTree");
                                if(array.length() > 0)
                                {
                                    for(int i=0;i<array.length();i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        EventTypeModel model = new EventTypeModel();
                                        model.setEventTypeId(object.getString("id"));
                                        model.setEventTypeName(object.getString("name"));
                                        list_data.add(model);
                                    }
                                    eAdapter.notifyDataSetChanged();
                                }else{
                                    myUntils.showToast(getContext(),"对不起，没有可以选择的部门！");
                                    dismiss();
                                }
                            }else{
                                myUntils.showToast(getContext(),json.getString("message"));
                            }


                        }catch (Exception ex)
                        {
                            Log.e("EventReportDialog", "行数: 187  ex:" + ex.getMessage());
                            myUntils.showToast(getContext(),"请检查网络是否正常链接！");

                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("EventReportDialog", "行数: 196  error:" + response.body());
                    }
                });
    }

    /**
     * 加载事件类别
     */
    private void initEventType()
    {

        OkGo.<String>post(Constants.EVENTTYPE_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("typeId",parentType)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String data = response.body();//这个就是返回来的结果
                        try {
                            JSONObject json = new JSONObject(data);
                            String resultCode = json.getString("code");

                            if(resultCode.equals("2000"))
                            {
                                JSONArray childData = json.getJSONArray("data");
                                if(childData.length() > 0)
                                {
                                    for(int i=0;i<childData.length();i++) {
                                        JSONObject object = childData.getJSONObject(i);
                                        EventTypeModel model = new EventTypeModel();
                                        model.setEventTypeId(object.getString("eventTypeId"));
                                        model.setEventTypeName(object.getString("eventTypeName"));
                                        list_data.add(model);
                                    }
                                    eAdapter.notifyDataSetChanged();
                                }else{
                                    myUntils.showToast(getContext(),"对不起，没有可以选择的部门！");
                                    dismiss();
                                }
                            }else{
                                myUntils.showToast(getContext(),json.getString("message"));
                                dismiss();
                            }


                        }catch (Exception ex)
                        {
                            Log.e("EventReportDialog", "行数: 187  ex:" + ex.getMessage());
                            myUntils.showToast(getContext(),"请检查网络是否正常链接！");

                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("EventReportDialog", "行数: 196  error:" + response.body());
                    }
                });
    }

    /**
     * 加载节点名称类
     */
    private void initNoteType()
    {
        OkGo.<String>post(Constants.EVENTNOTE_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("typeId",parentType)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String data = response.body();//这个就是返回来的结果
                        try {
                            JSONObject json = new JSONObject(data);
                            String resultCode = json.getString("code");

                            if(resultCode.equals("2000"))
                            {
                                JSONArray childData = json.getJSONArray("data");
                                if(childData.length() > 0)
                                {
                                    for(int i=0;i<childData.length();i++) {
                                        JSONObject object = childData.getJSONObject(i);
                                        EventTypeModel model = new EventTypeModel();
                                        model.setEventTypeId(object.getString("id"));
                                        model.setEventTypeName(object.getString("name"));
                                        list_data.add(model);
                                    }
                                    eAdapter.notifyDataSetChanged();
                                }else{
                                    myUntils.showToast(getContext(),"对不起，没有可以选择的部门！");
                                    dismiss();
                                }
                            }else{
                                myUntils.showToast(getContext(),json.getString("message"));
                                dismiss();
                            }


                        }catch (Exception ex)
                        {
                            Log.e("EventReportDialog", "行数: 187  ex:" + ex.getMessage());
                            myUntils.showToast(getContext(),"请检查网络是否正常链接！");
                           ;
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dismiss();
                        myUntils.showToast(getContext(),"数据请求失败，请检查网络连接！");
                        Log.e("EventReportDialog", "行数: 196  error:" + response.body());
                    }
                });
    }

    /**
     * 通讯录选择
     */
    private void initMail()
    {
        EventTypeModel model1 = new EventTypeModel();
        model1.setEventTypeId("1");
        model1.setEventTypeName("发信息");
        list_data.add(model1);

        EventTypeModel model2 = new EventTypeModel();
        model2.setEventTypeId("2");
        model2.setEventTypeName("打电话");
        list_data.add(model2);

        eAdapter.notifyDataSetChanged();
    }

    /**
     * 定义点击每项的接口
     */
    public interface OnItemClickLitener
    {
        void OnItemClick(View view,String EventTypeId, String EventTypeName);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}
