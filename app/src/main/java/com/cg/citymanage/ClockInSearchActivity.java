package com.cg.citymanage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cg.citymanage.Adapters.ClockSearchAdapter;
import com.cg.citymanage.infos.Constants;
import com.cg.citymanage.models.ClockSearchListModel;
import com.cg.citymanage.untils.myUntils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
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

* 功能：考勤打卡历史记录
* 作者：cg
* 时间：2019/11/18 16:20
*/
public class ClockInSearchActivity extends BaseActivity implements View.OnClickListener {

    private String appToken;

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 查询条件
     */
    private TextView txt_checkin;
    private TextView txt_checkout;
    private TimePickerView pvTime;
    private int timeType = 0;              //时间类别 0：开始时间 1：结束时间
    private String checkin;
    private String checkout;

    /**
     * 列表
     */
    private RecyclerView rv_clock;
    private List<ClockSearchListModel> list_data;
    private ClockSearchAdapter cAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        appToken = mSharedPreferences.getString("appToken","");

        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_clock_in_search);
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
        title_textview.setText("历史记录");

        //查询条件
        txt_checkin = (TextView)findViewById(R.id.txt_checkin);
        txt_checkin.setOnClickListener(this);
        txt_checkout = (TextView)findViewById(R.id.txt_checkout);
        txt_checkout.setOnClickListener(this);

        //列表
        rv_clock = (RecyclerView)findViewById(R.id.rv_clock);
        rv_clock.setLayoutManager(new LinearLayoutManager(mContext));
        list_data = new ArrayList<>();
        cAdapter = new ClockSearchAdapter(mContext,list_data);
        rv_clock.setAdapter(cAdapter);

        initDate();
        //initData();
    }

    /**
     * 初始化日期控件
     */
    private void initDate()
    {
        pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //Toast.makeText(mContext, date.toString(), Toast.LENGTH_SHORT).show();
                if(timeType == 0)
                {
                    txt_checkin.setText(myUntils.getTime(date));
                    beginSearch(txt_checkin.getText().toString(),txt_checkout.getText().toString());

                }else if(timeType == 1)
                {
                    txt_checkout.setText(myUntils.getTime(date));
                    beginSearch(txt_checkin.getText().toString(),txt_checkout.getText().toString());
                }
            }
        }).build();
    }

    /**
     * 根据日期来判断是否查询数据库
     * @param beginTime                 开始日期
     * @param endTime                   结束日期
     */
    private void beginSearch(String beginTime,String endTime)
    {
        if(!TextUtils.isEmpty(beginTime.replace("开始时间","")) && !TextUtils.isEmpty(endTime.replace("结束时间","")))
        {

            if(myUntils.timeCompare(beginTime,endTime) != 1)
            {
                checkin = txt_checkin.getText().toString();
                checkout = txt_checkout.getText().toString();
                initData();
            }
        }
    }

    /**
     * 初始化数据
     */
    private void initData()
    {
        progress_Dialog.show();
        OkGo.<String>post(Constants.CLOCKHISTORYLIST_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("checkin",checkin)
                .params("checkout",checkout)
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
                                JSONArray childjson = new JSONArray(json.getString("data"));
                                if(childjson.length() > 0)
                                {
                                    for(int i=0;i<childjson.length();i++)
                                    {
                                        JSONObject object = childjson.getJSONObject(i);
                                        ClockSearchListModel model = new ClockSearchListModel();
                                        model.setClockIn(object.getString("checkin").replace("null",""));
                                        if(!"null".equals(object.getString("checkin"))) {
                                            model.setClockTime(getClockTime(object.getString("checkin")));
                                        }
                                        model.setClockOut(object.getString("checkout").replace("null",""));
                                        if(!"null".equals(object.getString("checkout"))) {
                                            model.setClockTime(getClockTime(object.getString("checkout")));
                                        }
                                        if(object.getString("status").contains(","))
                                        {
                                            String[] status = object.getString("status").split(",");
                                            model.setClockInStatus(status[0]);
                                            model.setClockOutStatus(status[1]);
                                        }else{
                                            if(!"null".equals(object.getString("checkout")) && !"null".equals(object.getString("checkin")))
                                            {
                                                model.setClockInStatus(object.getString("status"));
                                                model.setClockOutStatus(object.getString("status"));
                                            }else if("null".equals(object.getString("checkout")) && !"null".equals(object.getString("checkin")))
                                            {
                                                model.setClockInStatus(object.getString("status"));
                                                model.setClockOutStatus("未打卡");
                                            }else if(!"null".equals(object.getString("checkout")) && "null".equals(object.getString("checkin")))
                                            {
                                                model.setClockInStatus("未打卡");
                                                model.setClockOutStatus(object.getString("status"));
                                            }else{
                                                model.setClockInStatus("未打卡");
                                                model.setClockOutStatus("未打卡");
                                            }
                                        }

                                        list_data.add(model);
                                    }
                                    cAdapter.notifyDataSetChanged();
                                }


                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }
                        }catch (Exception ex)
                        {
                            Log.e("ClockInSearch", "行数: 245  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");

                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        progress_Dialog.dismiss();
                        Log.e("ClockInSearch", "行数: 255  error:" + response.body());
                    }
                });
    }

    /**
     * 转化日期格式
     * @param time           日期
     * @return               转化后日期
     */
    private String getClockTime(String time)
    {
        if("null".equals(time))
        {
            return "";
        }else{
            return myUntils.StringPattern(time,"yyyy-MM-dd HH:mm:ss","yyyy年MM月dd日");
        }
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
            //开始日期
            case R.id.txt_checkin:
                timeType = 0;
                pvTime.show();
                break;
            //结束日期
            case R.id.txt_checkout:
                timeType = 1;
                pvTime.show();
                break;
        }
    }
}
