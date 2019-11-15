package com.cg.citymanage;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.citymanage.Adapters.PartStatisticsListMainAdapter;
import com.cg.citymanage.infos.Constants;
import com.cg.citymanage.models.PartListModel;
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

* 功能：部件统计
* 作者：cg
* 时间：2019/11/14 15:00
*/
public class PartStatisticsActivity extends BaseActivity implements View.OnClickListener {

    private String appToken;

    private String gridId = "";
    private String deptMainId = "";
    private String deptOwnerId = "";
    private String deptKeepId = "";

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;
    private RelativeLayout rl_2;
    private TextView title_right_btn;
    private int SEARCH_CODE = 101;

    /**
     * 显示数据
     */
    private ListView lv_mainp;
    private List<PartListModel> list_data;
    private PartStatisticsListMainAdapter pAdapter;

    /**
     * 无数据时显示
     */
    private RelativeLayout rela_NoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        appToken = mSharedPreferences.getString("appToken","");

        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_part_statistics);
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
        title_textview.setText("部件统计");
        rl_2 = (RelativeLayout)findViewById(R.id.rl_2);
        rl_2.setVisibility(View.VISIBLE);
        rl_2.setOnClickListener(this);
        title_right_btn = (TextView)findViewById(R.id.title_right_btn);
        title_right_btn.setVisibility(View.VISIBLE);
        //title_right_btn.setBackgroundResource(R.mipmap.ico_mainevent);
        title_right_btn.setText("查询");
        title_right_btn.setOnClickListener(this);

        //信息列表
        lv_mainp = (ListView)findViewById(R.id.lv_mainp);
        list_data = new ArrayList<>();
        pAdapter = new PartStatisticsListMainAdapter(mContext,list_data);
        lv_mainp.setAdapter(pAdapter);

        //无数据时显示
        rela_NoData = (RelativeLayout)findViewById(R.id.rela_NoData);

        initData();
    }

    /**
     * 初始化控件
     */
    private void initData()
    {
        progress_Dialog.show();
        OkGo.<String>post(Constants.PARTSTATISTICS_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("gridId",gridId)
                .params("deptMainId",deptMainId)
                .params("deptOwnerId",deptOwnerId)
                .params("deptKeepId",deptKeepId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        progress_Dialog.dismiss();
                        String data = response.body();

                        Log.e("PartStatistics", "行数: 133  data:" + data);

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
                                        PartListModel model = new PartListModel();
                                        model.setUnitTypeId(object.getString("unitTypeId"));
                                        model.setUnitTypeName(object.getString("unitTypeName"));
                                        model.setTotalCount(object.getString("totalCount"));

                                        //取子类
                                        JSONArray child = object.getJSONArray("children");
                                        List<PartListModel> list_c = new ArrayList<>();
                                        if(child.length() > 0)
                                        {
                                            for(int j=0;j<child.length();j++)
                                            {
                                                JSONObject cobject = child.getJSONObject(j);
                                                PartListModel model_c = new PartListModel();
                                                model_c.setUnitTypeId(cobject.getString("unitTypeId"));
                                                model_c.setUnitTypeName(cobject.getString("unitTypeName"));
                                                model_c.setTotalCount(cobject.getString("totalCount"));
                                                model_c.setChildren(null);
                                                list_c.add(model_c);
                                            }
                                        }
                                        model.setChildren(list_c);

                                        list_data.add(model);
                                    }

                                    pAdapter.notifyDataSetChanged();

                                }else{
                                    myUntils.showToast(mContext,"没有新的事件了！");
                                }

                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }

                            //根据数据数量，来判断是否给出无数据提示
                            if(list_data.size() > 0)
                            {
                                rela_NoData.setVisibility(View.GONE);
                            }else{
                                rela_NoData.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception ex)
                        {
                            Log.e("EventWait", "行数: 199  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");
                            return;
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        progress_Dialog.dismiss();
                        Log.e("EventWait", "行数: 211  error:" + response.body());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SEARCH_CODE) {
            gridId = data.getStringExtra("gridId");
            deptKeepId = data.getStringExtra("deptKeepId");
            deptMainId = data.getStringExtra("deptMainId");
            deptOwnerId = data.getStringExtra("deptOwnerId");

            Log.e("PartStatistics", "行数: 247  gridId:" + gridId + " deptKeepId:" + deptKeepId + " deptMainId:" + deptMainId + " deptOwnerId:" + deptOwnerId);
            list_data.clear();
            initData();
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
            //  跳转到查询页面
            case R.id.rl_2:
                Intent intent = new Intent(mContext,PartStatisticsSearchActivity.class);
                startActivityForResult(intent,SEARCH_CODE);
                break;
            //跳转到查询页面
            case R.id.title_right_btn:
                Intent intent2 = new Intent(mContext,PartStatisticsSearchActivity.class);
                startActivityForResult(intent2,SEARCH_CODE);
                break;
        }
    }
}
