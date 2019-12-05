package com.cg.citymanage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.citymanage.Adapters.EventDetaiFlowListAdapter;
import com.cg.citymanage.Adapters.EventDetailImpatientListAdpater;
import com.cg.citymanage.customs.ListViewForScrollView;
import com.cg.citymanage.infos.Constants;
import com.cg.citymanage.models.EventFlowListModel;
import com.cg.citymanage.models.EventImpatientListModel;
import com.cg.citymanage.untils.LiveDataBus;
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

* 功能：催办事件详情页
* 作者：cg
* 时间：2019/10/25 16:01
*/
public class EventImpatientDetailActivity extends BaseActivity implements View.OnClickListener {

    private String appToken;
    private String eventId;
    private String isImpatient;
    private String title;
    private String lng;
    private String lat;

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 详细信息
     */
    private TextView txt_eventName;
    private LinearLayout linear_event;
    private TextView txt_eventCodeName;
    private TextView txt_eventypeName;
    private TextView txt_reportName;
    private TextView txt_siteValue;
    private TextView txt_timeValue;
    private TextView txt_infoValue;

    /**
     * 事件流程
     */
    private RelativeLayout rela_detail;
    private ImageView img_detail;

    private ListViewForScrollView lv_eventdetail;
    private List<EventFlowListModel> list_data;
    private EventDetaiFlowListAdapter eAdapter;

    /**
     * 催办流程
     */
    private LinearLayout linear_impatient;
    private RelativeLayout rela_Impatientdetail;
    private ImageView img_Impatientdetail;

    private ListViewForScrollView lv_eventImpatientdetail;
    private List<EventImpatientListModel> list_idata;
    private EventDetailImpatientListAdpater eiAdapter;

    //处理结果
    private RelativeLayout rela_Opinion;
    private EditText edit_HandlingOpinions;

    /**
     * 提交事件
     */
    private LinearLayout linear_add;
    private Button btn_impatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        appToken = mSharedPreferences.getString("appToken","");
        eventId = getIntent().getStringExtra("eventId");
        isImpatient = getIntent().getStringExtra("isImpatient");
        title = getIntent().getStringExtra("title");
        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_event_impatient_detail);
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
        title_textview.setText("催办事件");

        //详细信息
        txt_eventName = (TextView)findViewById(R.id.txt_eventName);
        txt_eventName.setText(title);
        linear_event = (LinearLayout)findViewById(R.id.linear_event);
        txt_eventCodeName = (TextView)findViewById(R.id.txt_eventCodeName);
        txt_eventypeName = (TextView)findViewById(R.id.txt_eventypeName);
        txt_reportName = (TextView)findViewById(R.id.txt_reportName);
        txt_siteValue = (TextView)findViewById(R.id.txt_siteValue);
        txt_siteValue.setOnClickListener(this);
        txt_timeValue = (TextView)findViewById(R.id.txt_timeValue);
        txt_infoValue = (TextView)findViewById(R.id.txt_infoValue);

        //事件流程
        rela_detail = (RelativeLayout)findViewById(R.id.rela_detail);
        rela_detail.setOnClickListener(this);
        img_detail = (ImageView)findViewById(R.id.img_detail);

        lv_eventdetail = (ListViewForScrollView)findViewById(R.id.lv_eventdetail);
        list_data = new ArrayList<>();
        //temp();
        eAdapter = new EventDetaiFlowListAdapter(mContext,list_data);
        lv_eventdetail.setAdapter(eAdapter);
        initEvenData();

        //催办流程
        linear_impatient = (LinearLayout)findViewById(R.id.linear_impatient);
        rela_Impatientdetail = (RelativeLayout)findViewById(R.id.rela_Impatientdetail);
        rela_Impatientdetail.setOnClickListener(this);
        img_Impatientdetail = (ImageView)findViewById(R.id.img_Impatientdetail);

        lv_eventImpatientdetail = (ListViewForScrollView)findViewById(R.id.lv_eventImpatientdetail);
        list_idata = new ArrayList<>();
        //temp1();
        eiAdapter = new EventDetailImpatientListAdpater(mContext,list_idata);
        lv_eventImpatientdetail.setAdapter(eiAdapter);
        initImpatientData();

        //接收人
        rela_Opinion = (RelativeLayout)findViewById(R.id.rela_Opinion);
        edit_HandlingOpinions = (EditText)findViewById(R.id.edit_HandlingOpinions);

        //提交事件
        linear_add = (LinearLayout)findViewById(R.id.linear_add);
        btn_impatient = (Button)findViewById(R.id.btn_impatient);
        btn_impatient.setOnClickListener(this);

        if("1".equals(isImpatient))
        {
            rela_Opinion.setVisibility(View.GONE);
            linear_add.setVisibility(View.GONE);
        }else{
            rela_Opinion.setVisibility(View.VISIBLE);
            linear_add.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载事件的详细信息
     */
    private void initEvenData(){
        progress_Dialog.show();
        OkGo.<String>post(Constants.EVENTDETAIL_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("eventId",eventId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        progress_Dialog.dismiss();
                        String data = response.body();//这个就是返回来的结果
                        try {
                            JSONObject json = new JSONObject(data);
                            String resultCode = json.getString("code");
                            if(resultCode.equals("2000"))
                            {
                                JSONObject childData = json.getJSONObject("data");
                                //事件信息
                                JSONObject info = childData.getJSONObject("CgEvent");
                                txt_eventCodeName.setText(info.getString("eventCode"));
                                txt_eventypeName.setText(info.getString("eventTypeName"));
                                txt_reportName.setText(info.getString("empName"));
//                                txt_handleName.setText(childData.getString("cgEmp"));
//                                txt_gridName.setText(info.getString("gridName"));
                                txt_siteValue.setText(info.getString("eventLocation"));
                                txt_timeValue.setText(info.getString("cdate"));
                                txt_infoValue.setText(info.getString("eventContent"));

                                lng = info.getString("lng");
                                lat = info.getString("lat");

                                //事件流程数据
                                JSONArray eventList = childData.getJSONArray("CommentBean");
                                if(eventList.length() > 0)
                                {
                                    linear_event.setVisibility(View.VISIBLE);

                                    for(int i=0;i<eventList.length();i++) {

                                        JSONObject event = eventList.getJSONObject(i);
                                        String[] userinfo = event.getString("userId").split("_");
                                        EventFlowListModel model = new EventFlowListModel();
                                        model.setEventFlowId(String.valueOf(i+1));
                                        model.setEventFlowLink(userinfo[1]);
                                        model.setEventStreet(userinfo[0]);
                                        model.setEventFlowHandler(userinfo[2]);
                                        model.setEventFlowHandleTime(TextUtils.isEmpty(event.getString("time")) ? "" :
                                                myUntils.StringPattern(event.getString("time"),"yyyy年MM月dd日 HH时mm分ss秒","yyyy-MM-dd HH:mm:ss"));
                                        model.setEventFlowInfo(event.getString("fullMessage"));

                                        if(userinfo.length > 3)
                                        {
                                            model.setEventEnclosure(userinfo[3]);

                                        }else{
                                            model.setEventEnclosure("");
                                        }

                                        list_data.add(model);
                                    }
                                    eAdapter.notifyDataSetChanged();

                                }else{
                                    linear_event.setVisibility(View.GONE);
                                }

                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }


                        }catch (Exception ex)
                        {
                            Log.e("EventImpatientDetail", "行数: 274  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");

                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        progress_Dialog.dismiss();
                        Log.e("EventImpatientDetail", "行数: 285  error:" + response.body());
                    }
                });
    }

    /**
     * 催办流程数据
     */
    private void initImpatientData()
    {
        OkGo.<String>post(Constants.EVENTIMPATIENTDETAIL_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("eventId",eventId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        progress_Dialog.dismiss();
                        String data = response.body();//这个就是返回来的结果
                        try {
                            JSONObject json = new JSONObject(data);
                            String resultCode = json.getString("code");
                            //Log.e("EventPartakeDetail", "行数: 283  data:" + data);
                            if(resultCode.equals("2000"))
                            {
                                JSONArray array = json.getJSONArray("data");
                                if(array.length()>0)
                                {
                                    linear_impatient.setVisibility(View.VISIBLE);
                                    for(int i=0;i<array.length();i++)
                                    {
                                        JSONObject child = array.getJSONObject(i);
                                        EventImpatientListModel model = new EventImpatientListModel();
                                        model.setEventImpatientId(String.valueOf(i+1));
                                        model.setEventImpatientLink(child.getString("hastenFrequency"));
                                        model.setEventImpatienter(child.getString("empName"));
                                        model.setEventpImpatienter(child.getString("hastenEmpName"));
                                        model.setEventImpatientTime(child.getString("cdate"));
                                        model.setEventImpatientInfo(child.getString("hastenContent"));

                                        list_idata.add(model);
                                    }
                                    eiAdapter.notifyDataSetChanged();
                                }else{
                                    linear_impatient.setVisibility(View.GONE);
                                }
                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }


                        }catch (Exception ex)
                        {
                            Log.e("EventImpatient", "行数: 339  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");

                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("EventImpatient", "行数: 349  error:" + response.body());
                    }
                });
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
        switch (v.getId()) {
            //返回
            case R.id.title_left_btn:
                finish();
                break;
            //是否显示事件流程详情
            case R.id.rela_detail:
                if (lv_eventdetail.getVisibility() == View.VISIBLE) {
                    img_detail.setImageResource(R.mipmap.ico_eventflowdown);
                    lv_eventdetail.setVisibility(View.GONE);
                } else {
                    img_detail.setImageResource(R.mipmap.ico_eventflowup);
                    lv_eventdetail.setVisibility(View.VISIBLE);
                }
                break;
            //是否显示事件流程详情
            case R.id.rela_Impatientdetail:
                if (lv_eventImpatientdetail.getVisibility() == View.VISIBLE) {
                    img_Impatientdetail.setImageResource(R.mipmap.ico_eventflowdown);
                    lv_eventImpatientdetail.setVisibility(View.GONE);
                } else {
                    img_Impatientdetail.setImageResource(R.mipmap.ico_eventflowup);
                    lv_eventImpatientdetail.setVisibility(View.VISIBLE);
                }
                break;
            //显示地图信息页
            case R.id.txt_siteValue:
                try {
                    double dlnt = Double.valueOf(lng);
                    double dlat = Double.valueOf(lat);
                    if(dlnt==0 || dlat==0)
                    {
                        myUntils.showToast(mContext,"对不起，坐标数据有误，请与管理员联系确认此事件正确性！");
                    }
                    else{
                        bundle.putDouble("lng",dlnt);
                        bundle.putDouble("lat",dlat);
                        Jump_intent(MapViewActivity.class,bundle);
                    }
                }catch (Exception ex)
                {
                    myUntils.showToast(mContext,"对不起，坐标数据有误，请与管理员联系确认此事件正确性！");
                }
                break;

            //提交事件
            case R.id.btn_impatient:
                String message = edit_HandlingOpinions.getText().toString();
                if(TextUtils.isEmpty(message))
                {
                    myUntils.showToast(mContext,"处理意见不能为空！");
                }else{
                    submitImpatient(message);
                }
                break;
        }
    }

    /**
     * 提交催办意见
     * @param message       处理意见
     */
    private void submitImpatient(String message)
    {
        OkGo.<String>post(Constants.EVENTIMPATIENTSUMIT_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("eventId",eventId)
                .params("message",message)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String data = response.body();//这个就是返回来的结果
                        try {
                            JSONObject json = new JSONObject(data);
                            String resultCode = json.getString("code");

                            if(resultCode.equals("2000"))
                            {
                                LiveDataBus.get().with("EventImpatient").setValue(true);
                                myUntils.showToast(mContext,"催办成功！");
                                finish();
                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }


                        }catch (Exception ex)
                        {
                            Log.e("EventImpatientDetail", "行数: 459  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");

                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("EventImpatientDetail", "行数: 469  error:" + response.body());
                    }
                });
    }

}
