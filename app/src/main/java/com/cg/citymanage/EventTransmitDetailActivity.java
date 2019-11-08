package com.cg.citymanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

* 功能：事件传阅详情页面
* 作者：cg
* 时间：2019/10/25 9:45
*/
public class EventTransmitDetailActivity extends BaseActivity implements View.OnClickListener {

    private String appToken;
    private String eventId;
    private String title;
    private String lng = "0";
    private String lat = "0";
    private String empId;        //传阅人id

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 详细信息
     */
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

    //接收人
    private TextView txt_ReceiverName;
    private ImageView img_ReceiverAdd;

    /**
     * 提交事件
     */
    private Button btn_transmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        appToken = mSharedPreferences.getString("appToken","");
        eventId = getIntent().getStringExtra("eventId");
        title = getIntent().getStringExtra("title");
        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_event_transmit_detail);
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
        title_textview.setText("事件传阅");

        //详细信息
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
        img_ReceiverAdd = (ImageView)findViewById(R.id.img_ReceiverAdd);
        img_ReceiverAdd.setOnClickListener(this);
        txt_ReceiverName = (TextView)findViewById(R.id.txt_ReceiverName);
        txt_ReceiverName.setOnClickListener(this);

        //提交事件
        btn_transmit = (Button)findViewById(R.id.btn_transmit);
        btn_transmit.setOnClickListener(this);
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
        switch (v.getId())
        {
            //返回
            case R.id.title_left_btn:
                finish();
                break;
            //是否显示事件流程详情
            case R.id.rela_detail:
                if(lv_eventdetail.getVisibility() == View.VISIBLE)
                {
                    img_detail.setImageResource(R.mipmap.ico_eventflowdown);
                    lv_eventdetail.setVisibility(View.GONE);
                }else{
                    img_detail.setImageResource(R.mipmap.ico_eventflowup);
                    lv_eventdetail.setVisibility(View.VISIBLE);
                }
                break;
            //是否显示事件流程详情
            case R.id.rela_Impatientdetail:
                if(lv_eventImpatientdetail.getVisibility() == View.VISIBLE) {
                    img_Impatientdetail.setImageResource(R.mipmap.ico_eventflowdown);
                    lv_eventImpatientdetail.setVisibility(View.GONE);
                }else {
                    img_Impatientdetail.setImageResource(R.mipmap.ico_eventflowup);
                    lv_eventImpatientdetail.setVisibility(View.VISIBLE);
                }
                break;
            //显示地图
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
            //选择传阅人
            case R.id.img_ReceiverAdd:
                showReceiverDialog();
                break;
            //选择传阅人
            case R.id.txt_ReceiverName:
                showReceiverDialog();
                break;
            //提交事件
            case R.id.btn_transmit:
                if(TextUtils.isEmpty(empId))
                {
                    myUntils.showToast(mContext,"请选择传阅人！");
                }else {
                    submitTransmit();
                }
                break;


        }
    }

    /**
     * 显示选择传阅人的弹窗
     */
    private void showReceiverDialog()
    {
        EventReceiverSelectDialogFragment eDialog = EventReceiverSelectDialogFragment.newInstance(appToken,"",0,empId);
        eDialog.show(getSupportFragmentManager(),"选择传阅人");
        eDialog.setOnItemClickLitener(new EventReceiverSelectDialogFragment.OnItemClickLitener() {
            @Override
            public void OnItemClick(View view, String id, String name) {
                empId = id;
                txt_ReceiverName.setText(name);

            }
        });
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

//                                EventFlowListModel
                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }


                        }catch (Exception ex)
                        {
                            Log.e("EventTransmitDetail", "行数: 325  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");
                            return;
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        progress_Dialog.dismiss();
                        Log.e("EventTransmitDetail", "行数: 336  error:" + response.body());
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
                        //progress_Dialog.dismiss();
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
                            Log.e("EventTransmitDetail", "行数: 392  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");
                            return;
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("EventTransmitDetail", "行数: 402  error:" + response.body());
                    }
                });
    }

    /**
     * 提交传阅人
     */
    private void submitTransmit()
    {
        OkGo.<String>post(Constants.EVENTRANSMITSUBMIT_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("eventId",eventId)
                .params("empId", empId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String data = response.body();//这个就是返回来的结果
                        try {
                            JSONObject json = new JSONObject(data);
                            String resultCode = json.getString("code");

                            if(resultCode.equals("2000"))
                            {
                                myUntils.showToast(mContext,"传阅成功！");
                                finish();
                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }


                        }catch (Exception ex)
                        {
                            Log.e("EventImpatientDetail", "行数: 459  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");
                            return;
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
