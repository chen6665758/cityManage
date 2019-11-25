package com.cg.citymanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cg.citymanage.infos.Constants;
import com.cg.citymanage.untils.myUntils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

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

* 功能：部件采集
* 作者：cg
* 时间：2019/11/15 10:01
*/
public class PartsAddActivity extends BaseActivity implements View.OnClickListener {

    private String appToken;
    private String mainTypeId = "1001";                //部件主id
    private String typeId;

    private String bobjId;	//部件大类
    private String sobjId;	//部件小类

    private String objId;    //部件id
    private String objState; //部件状态
    private String deptName1;//主管单位
    private String deptName2;//权属单位
    private String deptName3;//养护单位

    private String x;
    private String y;
    private String bgId;     //网格id
    private String address;  //详细地址
    private String areas;    //所属社区
    private String dataSource;//来源

    private String photoAddr;//图片地址
    private String note;     //备注

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 部件信息
     */
    private TextView txt_bigTypeName;
    private TextView txt_smallTypeName;

    private EditText edit_objId;
    private Button btn_objId;
    private EditText edit_objState;
    private EditText edit_deptName1;
    private EditText edit_deptName2;
    private EditText edit_deptName3;

    private TextView txt_gridValue;
    private int MAP_CODE = 101;
    private EditText edit_address;
    private EditText edit_areas;
    private EditText edit_dataSource;

    private EditText edit_photoAddr;
    private EditText edit_note;

    /**
     * 提交取消按钮
     */
    private Button btn_partSubmit;
    private Button btn_partCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        appToken = mSharedPreferences.getString("appToken","");

        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_parts_add);
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
        title_textview.setText("部件采集");

        //部件信息
        txt_bigTypeName = (TextView)findViewById(R.id.txt_bigTypeName);
        txt_bigTypeName.setOnClickListener(this);
        txt_smallTypeName = (TextView)findViewById(R.id.txt_smallTypeName);
        txt_smallTypeName.setOnClickListener(this);

        edit_objId = (EditText)findViewById(R.id.edit_objId);
        btn_objId = (Button)findViewById(R.id.btn_objId);
        btn_objId.setOnClickListener(this);
        edit_objState = (EditText)findViewById(R.id.edit_objState);
        edit_deptName1 = (EditText)findViewById(R.id.edit_deptName1);
        edit_deptName2 = (EditText)findViewById(R.id.edit_deptName2);
        edit_deptName3 = (EditText)findViewById(R.id.edit_deptName3);

        txt_gridValue = (TextView)findViewById(R.id.txt_gridValue);
        txt_gridValue.setOnClickListener(this);
        edit_address = (EditText)findViewById(R.id.edit_address);
        edit_areas = (EditText)findViewById(R.id.edit_areas);
        edit_dataSource = (EditText)findViewById(R.id.edit_dataSource);

        edit_photoAddr = (EditText)findViewById(R.id.edit_photoAddr);
        edit_note = (EditText)findViewById(R.id.edit_note);


        //提交取消按钮
        btn_partSubmit = (Button)findViewById(R.id.btn_partSubmit);
        btn_partSubmit.setOnClickListener(this);
        btn_partCancel = (Button)findViewById(R.id.btn_partCancel);
        btn_partCancel.setOnClickListener(this);

    }

    @Override
    public void Jump_intent(Class<?> cla, Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按钮
            case R.id.title_left_btn:
                finish();
                break;
            //选择部件大类
            case R.id.txt_bigTypeName:
                EventReportTypeDialogFragment bDialog = EventReportTypeDialogFragment.newInstance("1", mainTypeId, appToken, "");
                bDialog.show(getSupportFragmentManager(), "部件大类");
                bDialog.setOnItemClickLitener(new EventReportTypeDialogFragment.OnItemClickLitener() {
                    @Override
                    public void OnItemClick(View view, String EventTypeId, String EventTypeName) {
                        txt_bigTypeName.setText(EventTypeName);
                        bobjId = EventTypeId;
                        typeId = EventTypeId;
                    }
                });
                break;
            //选择部件小类
            case R.id.txt_smallTypeName:
                if(TextUtils.isEmpty(typeId))
                {
                    myUntils.showToast(mContext,"请先选择部件大类！");
                }else {
                    EventReportTypeDialogFragment sDialog = EventReportTypeDialogFragment.newInstance("1", typeId, appToken, "");
                    sDialog.show(getSupportFragmentManager(), "部件小类");
                    sDialog.setOnItemClickLitener(new EventReportTypeDialogFragment.OnItemClickLitener() {
                        @Override
                        public void OnItemClick(View view, String EventTypeId, String EventTypeName) {
                            txt_smallTypeName.setText(EventTypeName);
                            sobjId = EventTypeId;
                        }
                    });
                }
                break;
            //验证部件id
            case R.id.btn_objId:
                objId = edit_objId.getText().toString();
                if(TextUtils.isEmpty(objId))
                {
                    myUntils.showToast(mContext,"请先添写部件编码！");
                }else{
                    VerObjId("ver");
                }
                break;
            //取网格数据
            case R.id.txt_gridValue:
                Intent mapIntent = new Intent();
                mapIntent.setClass(PartsAddActivity.this,MapSelectActivity.class);
                //startActivityForResult(mapIntent,MAP_CODE);
                mapIntent.putExtra("mapclass","partadd");
                startActivityForResult(mapIntent,MAP_CODE);
                break;
            //采集部件
            case R.id.btn_partSubmit:
                objId = edit_objId.getText().toString();
                objState = edit_objState.getText().toString();
                deptName1 = edit_deptName1.getText().toString();
                deptName2 = edit_deptName2.getText().toString();
                deptName3 = edit_deptName3.getText().toString();

                address = edit_address.getText().toString();
                areas = edit_areas.getText().toString();
                dataSource = edit_dataSource.getText().toString();

                photoAddr = edit_photoAddr.getText().toString();
                note = edit_note.getText().toString();

                if(TextUtils.isEmpty(objId))
                {
                    myUntils.showToast(mContext,"对不起，部件编码不能为空！");
                    return;
                }
                VerObjId("add");
                break;
            //取消采集
            case R.id.btn_partCancel:
                partCancel();
                break;
        }
    }

    /**
     * 验证部件编码
     */
    private void VerObjId(final String status)
    {
        progress_Dialog.show();
        OkGo.<String>post(Constants.PARTSVAROBJID_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("objId",objId)
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
                                JSONObject object = json.getJSONObject("data");
                                if("0".equals(object.getString("valid"))) {
                                    if("add".equals(status))
                                    {
                                        submitPart();
                                    }else {
                                        myUntils.showToast(mContext, "部件编码可用！");
                                    }
                                }else{
                                    myUntils.showToast(mContext, "部件编码已存在，请更换编码！");
                                    edit_objId.setText("");
                                }

                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }


                        }catch (Exception ex)
                        {
                            progress_Dialog.dismiss();
                            Log.e("PartsAdd", "行数: 191  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");
                            return;
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("PartsAdd", "行数: 191  error:" + response.body());
                    }
                });
    }

    /**
     * 部件采集
     */
    private void submitPart()
    {

        if(TextUtils.isEmpty(bobjId.replace("请选择(必填)","")))
        {
            myUntils.showToast(mContext,"部件大类不能为空");
            return;
        }

        progress_Dialog.show();

        OkGo.<String>post(Constants.PARTSUBMITADD_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("objId",objId)
                .params("x",x)
                .params("y",y)
                .params("bgId",bgId)
                .params("bobjId",bobjId)
                .params("sobjId",sobjId)
                .params("objState",objState)
                .params("deptName1",deptName1)
                .params("deptName2",deptName2)
                .params("deptName3",deptName3)
                .params("dataSource",dataSource)
                .params("areas",areas)
                .params("address",address)
                .params("photoAddr",photoAddr)
                .params("note",note)
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
                                myUntils.showToast(mContext,"部件采集成功！");
                                partCancel();

                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }


                        }catch (Exception ex)
                        {
                            Log.e("PartsAdd", "行数: 339  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");
                            return;
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        progress_Dialog.dismiss();
                        Log.e("PartsAdd", "行数: 348  error:" + response.body());
                    }
                });
    }

    /**
     * 清空数据
     */
    private void partCancel()
    {
        bobjId = "";	//部件大类
        sobjId = "";	//部件小类

        objId = "";    //部件id
        objState = ""; //部件状态
        deptName1 = "";
        deptName2 = "";
        deptName3 = "";

        x = "";
        y = "";
        bgId = "";
        address = "";
        areas = "";
        dataSource = "";

        photoAddr = "";
        note = "";

        txt_bigTypeName.setText("请选择(必填)");
        txt_smallTypeName.setText("");
        edit_objId.setText("");
        edit_objState.setText("");
        edit_deptName1.setText("");
        edit_deptName2.setText("");
        edit_deptName3.setText("");

        txt_gridValue.setText("");
        edit_address.setText("");
        edit_areas.setText("");
        edit_dataSource.setText("");

        edit_photoAddr.setText("");
        edit_note.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == MAP_CODE) {

            txt_gridValue.setText(data.getStringExtra("gridName"));
            edit_address.setText(data.getStringExtra("address"));
            bgId = data.getStringExtra("gridId");
        }
    }
}
