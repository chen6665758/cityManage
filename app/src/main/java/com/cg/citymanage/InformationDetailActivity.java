package com.cg.citymanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cg.citymanage.infos.Constants;
import com.cg.citymanage.untils.myUntils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONObject;

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

* 功能：信息详情页
* 作者：cg
* 时间：2019/11/4 14:06
*/
public class InformationDetailActivity extends BaseActivity implements View.OnClickListener {

    private String appToken;
    private String messId;
    private String send;                  //发送人id
    private String sendName;              //发送人姓名
    private String otherInfo;

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 信息详情
     */
    private TextView txt_infoTitle;
    private TextView txt_infoTime;
    private TextView txt_infoContent;

    /**
     * 附件信息
     */
    private TextView txt_other;

    /**
     * 回复按钮
     */
    private Button btn_reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        appToken = mSharedPreferences.getString("appToken","");
        messId = getIntent().getStringExtra("messId");
        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_information_detail);
    }

    /**
     *初始化控件
     */
    private void initControls()
    {
        //标题栏
        title_left_btn = (ImageButton)findViewById(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_textview = (TextView) findViewById(R.id.title_textview);
        title_textview.setText("信息详情");

        //信息详情
        txt_infoTitle = (TextView)findViewById(R.id.txt_infoTitle);
        txt_infoTime = (TextView)findViewById(R.id.txt_infoTime);
        txt_infoContent = (TextView)findViewById(R.id.txt_infoContent);

        //附件信息
        txt_other = (TextView)findViewById(R.id.txt_other);
        txt_other.setOnClickListener(this);

        //回复按钮
        btn_reply = (Button)findViewById(R.id.btn_reply);
        btn_reply.setOnClickListener(this);

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData()
    {
        progress_Dialog.show();
        OkGo.<String>post(Constants.INFORMATIONDETAIL_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("messId",messId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        progress_Dialog.dismiss();
                        String data = response.body();
                        Log.e("InformationDetailActivity.java(onSuccess)", "行数: 137  data:" + data);
                        try {
                            JSONObject json = new JSONObject(data);
                            String resultCode = json.getString("code");
                            if(resultCode.equals("2000"))
                            {
                                JSONObject jData = json.getJSONObject("data");
                                JSONObject message = jData.getJSONObject("message");
                                send = message.getString("send");
                                sendName = message.getString("sendName");
                                txt_infoTitle.setText(message.getString("messageTitle"));
                                txt_infoTime.setText(message.getString("sendTime"));
                                txt_infoContent.setText(message.getString("messageContent"));

                                JSONArray array = jData.getJSONArray("MessageAccessory");
                                if(array.length() > 0)
                                {
                                    txt_other.setVisibility(View.VISIBLE);
                                    otherInfo = data;
                                }else{
                                    txt_other.setVisibility(View.GONE);
                                }

                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }

                        }catch (Exception ex)
                        {
                            Log.e("InformationDetail", "行数: 211  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");
                            return;
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        progress_Dialog.dismiss();
                        Log.e("InformationDetail", "行数: 223  error:" + response.body());
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
            //跳转附件
            case R.id.txt_other:
                bundle.putString("otherInfo",otherInfo);
                Jump_intent(InformationOtherActivity.class,bundle);
                break;
            //回复内容
            case R.id.btn_reply:
                bundle.putString("status","1");
                bundle.putString("empId",send);
                bundle.putString("empName",sendName);
                Jump_intent(InformationReplyActivity.class,bundle);
                break;
        }
    }
}
