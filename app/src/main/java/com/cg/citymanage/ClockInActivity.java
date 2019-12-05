package com.cg.citymanage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.cg.citymanage.infos.Constants;
import com.cg.citymanage.untils.myUntils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import static com.cg.citymanage.infos.Constants.MAINUSERINFO_URL;

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

* 功能：考勤打卡
* 作者：cg
* 时间：2019/11/18 15:07
*/
public class ClockInActivity extends BaseActivity implements View.OnClickListener {

    private String appToken;

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;
    private RelativeLayout rl_2;
    private TextView title_right_btn;

    /**
     * 用户信息
     */
    private RelativeLayout rela_userInfo;
    private ImageView img_user;
    private TextView txt_username;
    private TextView txt_userinfo;

    /**
     * 考勤信息
     */
    private TextView txt_weekComeLateCount;
    private TextView txt_weekGroupCount;
    private TextView txt_monthComeLateCount;
    private TextView txt_monthGroupCount;
    private String workSts = "0";          //0上班打卡   1下班打卡

    /**
     * 签到按钮
     */
    private Button btn_clockIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        appToken = mSharedPreferences.getString("appToken","");

        initConrols();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_clock_in);
    }

    /**
     * 初始化控件
     */
    private void initConrols()
    {
        //标题栏
        title_left_btn = (ImageButton)findViewById(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_textview = (TextView) findViewById(R.id.title_textview);
        title_textview.setText("考勤打卡");
        rl_2 = (RelativeLayout)findViewById(R.id.rl_2);
        rl_2.setVisibility(View.VISIBLE);
        title_right_btn = (TextView)findViewById(R.id.title_right_btn);
        title_right_btn.setText("历史记录");
        title_right_btn.setOnClickListener(this);

        //用户信息
        rela_userInfo = (RelativeLayout)findViewById(R.id.rela_userInfo);
        img_user = (ImageView)findViewById(R.id.img_user);
        txt_username = (TextView)findViewById(R.id.txt_username);
        txt_userinfo = (TextView)findViewById(R.id.txt_userinfo);
        Glide.with(this)
                .load("https://c-ssl.duitang.com/uploads/item/201811/07/20181107170428_sltox.thumb.700_0.jpeg")
                .apply(RequestOptions.bitmapTransform(new CircleCrop()).placeholder(R.mipmap.ico_loginusertype))
                .into(img_user);

        //考勤信息
        txt_weekComeLateCount = (TextView)findViewById(R.id.txt_weekComeLateCount);
        txt_weekGroupCount = (TextView)findViewById(R.id.txt_weekGroupCount);
        txt_monthComeLateCount = (TextView)findViewById(R.id.txt_monthComeLateCount);
        txt_monthGroupCount = (TextView)findViewById(R.id.txt_monthGroupCount);

        //打卡按钮
        btn_clockIn = (Button)findViewById(R.id.btn_clockIn);
        btn_clockIn.setOnClickListener(this);

        initUserInfo();
        initClockWeekInfo();
        initClockMonthInfo();
        initClockDayInfo();
    }

    /**
     * 加载用户信息
     */
    private void initUserInfo()
    {
        OkGo.<String>get(MAINUSERINFO_URL)
                .tag(this)//
                .params("access_token", appToken)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        String data = response.body();//这个就是返回来的结果
                        try {
                            JSONObject json = new JSONObject(data);
                            String resultCode = json.getString("code");
                            if(resultCode.equals("2000"))
                            {
                                JSONObject childjson = new JSONObject(json.getString("data"));
                                txt_username.setText(childjson.getString("name"));
                                txt_userinfo.setText(childjson.getString("orgName"));
                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }
                        }catch (Exception ex)
                        {
                            Log.e("ClockIn", "行数: 171  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");

                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("ClockIn", "行数: 180  error:" + response.body());
                    }
                });
    }

    /**
     * 周统计数据
     */
    private void initClockWeekInfo()
    {
        OkGo.<String>post(Constants.CLOCKINFOWEEK_URL)
                .tag(this)//
                .params("access_token", appToken)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        String data = response.body();//这个就是返回来的结果
                        try {
                            JSONObject json = new JSONObject(data);
                            String resultCode = json.getString("code");
                            if(resultCode.equals("2000"))
                            {
                                JSONObject childjson = new JSONObject(json.getString("data"));
                                txt_weekComeLateCount.setText(childjson.getString("comeLateCount"));
                                txt_weekGroupCount.setText(childjson.getString("groupCount"));
                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }
                        }catch (Exception ex)
                        {
                            Log.e("ClockIn", "行数: 211  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");

                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("ClockIn", "行数: 220  error:" + response.body());
                    }
                });
    }

    /**
     * 月统计数据
     */
    private void initClockMonthInfo()
    {
        OkGo.<String>post(Constants.CLOCKINFOMONTH_URL)
                .tag(this)//
                .params("access_token", appToken)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        String data = response.body();//这个就是返回来的结果
                        try {
                            JSONObject json = new JSONObject(data);
                            String resultCode = json.getString("code");
                            if(resultCode.equals("2000"))
                            {
                                JSONObject childjson = new JSONObject(json.getString("data"));
                                txt_monthComeLateCount.setText(childjson.getString("comeLateCount"));
                                txt_monthGroupCount.setText(childjson.getString("groupCount"));
                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }
                        }catch (Exception ex)
                        {
                            Log.e("ClockIn", "行数: 251  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");

                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("ClockIn", "行数: 260  error:" + response.body());
                    }
                });
    }

    /**
     * 月统计数据
     */
    private void initClockDayInfo()
    {
        OkGo.<String>post(Constants.CLOCKTODAY_URL)
                .tag(this)//
                .params("access_token", appToken)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        String data = response.body();//这个就是返回来的结果
                        try {
                            JSONObject json = new JSONObject(data);
                            String resultCode = json.getString("code");


                            if(resultCode.equals("2000"))
                            {
                                JSONObject childjson = new JSONObject(json.getString("data"));

                                if("null".equals(childjson.getString("clockIn")) && "null".equals(childjson.getString("clockOut")))
                                {
                                    workSts = "0";
                                    btn_clockIn.setText("上班打卡");
                                }else if(!"null".equals(childjson.getString("clockIn")) && "null".equals(childjson.getString("clockOut")))
                                {
                                    workSts = "1";
                                    btn_clockIn.setText("下班打卡");
                                }else{
                                    btn_clockIn.setEnabled(false);
                                    btn_clockIn.setText("已打卡");
                                }
                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }
                        }catch (Exception ex)
                        {
                            Log.e("ClockIn", "行数: 304  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");

                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("ClockIn", "行数: 313  error:" + response.body());
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
        switch (v.getId())
        {
            //返回
            case R.id.title_left_btn:
                finish();
                break;
            //打卡
            case R.id.btn_clockIn:
                ClockInSave();
                break;
            //跳转到历史记录
            case R.id.title_right_btn:
                Jump_intent(ClockInSearchActivity.class,bundle);
                break;
        }
    }

    /**
     * 考勤打卡保存
     */
    private void ClockInSave()
    {
        progress_Dialog.show();
        OkGo.<String>post(Constants.CLOCKINSAVE_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("workSts",workSts)
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
                                myUntils.showToast(mContext,"打卡成功！");
                                initClockDayInfo();
                                initClockWeekInfo();
                                initClockMonthInfo();

                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }
                        }catch (Exception ex)
                        {
                            progress_Dialog.dismiss();
                            Log.e("ClockIn", "行数: 384  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");

                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        progress_Dialog.dismiss();
                        Log.e("ClockIn", "行数: 394  error:" + response.body());
                    }
                });
    }
}
