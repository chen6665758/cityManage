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
import com.cg.citymanage.untils.myApplication;
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

* 功能：修改密码
* 作者：cg
* 时间：2019/11/25 16:34
*/
public class UpdatePwdActivity extends BaseActivity implements View.OnClickListener {

    private String appToken;

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 密码输入
     */
    private EditText edit_oldpwd;
    private EditText edit_newpwd;
    private EditText edit_pwd2;

    /**
     * 修改
     */
    private Button btn_partSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        appToken = mSharedPreferences.getString("appToken","");

        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_update_pwd);
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
        title_textview.setText("修改密码");

        //密码输入
        edit_oldpwd = (EditText)findViewById(R.id.edit_oldpwd);
        edit_newpwd = (EditText)findViewById(R.id.edit_newpwd);
        edit_pwd2 = (EditText)findViewById(R.id.edit_pwd2);

        //修改
        btn_partSubmit = (Button)findViewById(R.id.btn_partSubmit);
        btn_partSubmit.setOnClickListener(this);

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
            //修改按钮
            case R.id.btn_partSubmit:
                OkCancelFragmentDialog dialog = OkCancelFragmentDialog.newInstance("您确定要修改密码吗？");
                dialog.show(getSupportFragmentManager(),"修改密码");
                dialog.setOnItemClickLitener(new OkCancelFragmentDialog.OnItemClickLitener() {
                    @Override
                    public void OnItemClick(View view, int positon) {
                        updatePwd();
                    }
                });
                break;
        }
    }

    /**
     * 修改密码
     */
    private void updatePwd()
    {
        String oldPassword = edit_oldpwd.getText().toString();
        String newPassword = edit_newpwd.getText().toString();
        String configPassword = edit_pwd2.getText().toString();

        if(TextUtils.isEmpty(oldPassword))
        {
            myUntils.showToast(mContext,"旧密码不能为空！");
            return;
        }

        if(TextUtils.isEmpty(newPassword))
        {
            myUntils.showToast(mContext,"新密码不能为空！");
            return;
        }

        if(TextUtils.isEmpty(configPassword))
        {
            myUntils.showToast(mContext, "确认密码不能为空！");
            return;
        }

        if(!newPassword.equals(configPassword))
        {
            myUntils.showToast(mContext, "两次输入的密码不一样，请重新输入！");
            edit_newpwd.setText("");
            edit_pwd2.setText("");
            return;
        }

        progress_Dialog.show();
        OkGo.<String>post(Constants.PERSONALUPDATEPWD_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("oldPassword",oldPassword)
                .params("newPassword",newPassword)
                .params("configPassword",configPassword)
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
                                myUntils.showToast(mContext,"密码修改成功！重新登录！");
                                shared_editor.putString("appToken","").commit();
                                myApplication.getInstance().exitAllActivity();
                                startActivity(new Intent(mContext,LoginActivity.class));

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
}
