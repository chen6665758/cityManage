package com.cg.citymanage;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.citymanage.Adapters.MailListAdpater;
import com.cg.citymanage.infos.Constants;
import com.cg.citymanage.models.MailListModel;
import com.cg.citymanage.untils.LetterComparator;
import com.cg.citymanage.untils.PinnedHeaderDecoration;
import com.cg.citymanage.untils.myUntils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.nanchen.wavesidebar.SearchEditText;
import com.nanchen.wavesidebar.Trans2PinYinUtil;
import com.nanchen.wavesidebar.WaveSideBarView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
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

* 功能：通讯录
* 作者：cg
* 时间：2019/11/18 8:41
*/
public class MailListActivity extends BaseActivity implements View.OnClickListener {

    private String appToken;

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 通讯信息
     */
    private List<MailListModel> mContactModels;
    private List<MailListModel> mShowModels;
    private RecyclerView mRecyclerView;
    private WaveSideBarView mWaveSideBarView;
    private SearchEditText mSearchEditText;
    private MailListAdpater mAdapter;

    /**
     * 无数据布局
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
        setContentView(R.layout.activity_mail_list);
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
        title_textview.setText("通讯录");

        //通讯信息
        mContactModels = new ArrayList<>();
        mShowModels = new ArrayList<>();
        mAdapter = new MailListAdpater(mContext,mShowModels);

        // RecyclerView设置相关
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickLitener(new MailListAdpater.OnItemClickLitener() {
            @Override
            public void OnItemClick(View view, final int positon) {
                EventReportTypeDialogFragment eDialog = EventReportTypeDialogFragment.newInstance("5","",appToken,"");
                eDialog.show(getSupportFragmentManager(),"通讯录");
                eDialog.setOnItemClickLitener(new EventReportTypeDialogFragment.OnItemClickLitener() {
                    @Override
                    public void OnItemClick(View view, String EventTypeId, String EventTypeName) {
                        if("2".equals(EventTypeId))
                        {
                            callPhone(mShowModels.get(positon).getTel());
                        }else{
                            bundle.putString("status","1");
                            bundle.putString("empId",mShowModels.get(positon).getId());
                            bundle.putString("empName",mShowModels.get(positon).getName());
                            Jump_intent(InformationReplyActivity.class,bundle);
                        }
                    }
                });
            }
        });


        // 侧边设置相关
        mWaveSideBarView = (WaveSideBarView) findViewById(R.id.main_side_bar);
        mWaveSideBarView.setOnSelectIndexItemListener(new WaveSideBarView.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String letter) {
                for (int i=0; i<mContactModels.size(); i++) {
                    if (mContactModels.get(i).getIndex().equals(letter)) {
                        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });


        // 搜索按钮相关
        mSearchEditText = (SearchEditText) findViewById(R.id.main_search);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mShowModels.clear();
                for (MailListModel model : mContactModels) {
                    String str = Trans2PinYinUtil.trans2PinYin(model.getName());
                    if (str.contains(s.toString()) || model.getName().contains(s.toString())) {
                        mShowModels.add(model);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });


        //无数据布局
        rela_NoData = (RelativeLayout)findViewById(R.id.rela_NoData);

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData()
    {
        progress_Dialog.show();
        OkGo.<String>post(Constants.MAILLIST_URL)
                .tag(this)//
                .params("access_token", appToken)
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
                                JSONArray array = json.getJSONArray("data");
                                if(array.length() > 0)
                                {
                                    for(int i = 0;i<array.length();i++) {
                                        JSONObject child = array.getJSONObject(i);
                                        MailListModel model = new MailListModel(child.getString("id"),child.getString("tel"),child.getString("name"));

                                        mContactModels.add(model);
                                    }
                                    Collections.sort(mContactModels, new LetterComparator());
                                    mShowModels.addAll(mContactModels);
                                    mAdapter.notifyDataSetChanged();
                                }else{
                                    myUntils.showToast(mContext,"没有新的事件了！");
                                }

                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }

                            //根据数据数量，来判断是否给出无数据提示
                            if(mShowModels.size() > 0)
                            {
                                rela_NoData.setVisibility(View.GONE);
                            }else{
                                rela_NoData.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception ex)
                        {
                            Log.e("MailList", "行数: 241  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");
                            return;
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        progress_Dialog.dismiss();
                        Log.e("MailList", "行数: 252  error:" + response.body());
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
            //返回按钮
            case R.id.title_left_btn:
                finish();
                break;
        }
    }

    /**
     * 调用电话
     * @param phoneNum     电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}
