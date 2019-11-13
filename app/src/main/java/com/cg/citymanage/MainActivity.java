package com.cg.citymanage;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.cg.citymanage.Adapters.MainMenuAdapter;
import com.cg.citymanage.infos.Constants;
import com.cg.citymanage.models.MainMenuModel;
import com.cg.citymanage.untils.myUntils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.cg.citymanage.infos.Constants.DOWNLOADFILE_URL;
import static com.cg.citymanage.infos.Constants.LOGIN_URL;
import static com.cg.citymanage.infos.Constants.MAINUSEREVENTNUMBER_URL;
import static com.cg.citymanage.infos.Constants.MAINUSERINFO_URL;
import static com.cg.citymanage.infos.Constants.MAINUSERMENU_URL;

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

* 功能：主页面
* 作者：cg
* 时间：2019/10/21 14:42
*/
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private String appToken;

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;
    private RelativeLayout rl_2;
    private TextView title_right_btn;

    private long firstTime = 0;      //连续点击两次退出程序的计数器

    /**
     * 用户信息
     */
    private RelativeLayout rela_userInfo;
    private ImageView img_user;
    private TextView txt_username;
    private TextView txt_userinfo;

    /**
     * 事件数据
     */
    private TextView txt_willwork;
    private TextView txt_work;
    private TextView txt_workedpre;
    private TextView txt_noread;

    /**
     * 菜单
     */
    private RecyclerView rv_menu;
    private List<MainMenuModel> list_data;
    private MainMenuAdapter mAdapter;

    /***
     * 自动更新
     */

    private String dirName;

    AlertDialog alertDia;
    private SeekBar pbDownloadApk;
    private TextView pbTv;
    private LinearLayout lin_update;
    private TextView go_update;
    private TextView tv_ver;
    private Type verType;
    private String DownUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        appToken = mSharedPreferences.getString("appToken","");

        //权限的设置
        myUntils.JudgePermission(this,mContext,"您拒绝了读取文件的功能，会造成软件无法更新下载,并且会影响到维修与巡更功能！",Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

        //有权限时候 我要上传我的方法
        dirName = Environment.getExternalStorageDirectory().getPath() + "/sdcard/download/";
        File file = new File(dirName);
        if (!file.exists()) {
            file.mkdirs();
        }

        initControls();

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    /**
     * 初始化控件
     */
    private void initControls()
    {

        //标题栏
        title_left_btn = (ImageButton)findViewById(R.id.title_left_btn);
        title_left_btn.setVisibility(View.GONE);
        title_textview = (TextView)findViewById(R.id.title_textview);
        title_textview.setText("工作主页");
        rl_2 = (RelativeLayout)findViewById(R.id.rl_2);
        rl_2.setVisibility(View.VISIBLE);
        rl_2.setOnClickListener(this);
        title_right_btn = (TextView)findViewById(R.id.title_right_btn);
        title_right_btn.setBackgroundResource(R.mipmap.ico_mainevent);
        title_right_btn.setText("");
        title_right_btn.setOnClickListener(this);

        //用户信息
        rela_userInfo = (RelativeLayout)findViewById(R.id.rela_userInfo);
        rela_userInfo.setOnClickListener(this);
        img_user = (ImageView)findViewById(R.id.img_user);
        txt_username = (TextView)findViewById(R.id.txt_username);
        txt_userinfo = (TextView)findViewById(R.id.txt_userinfo);
        Glide.with(this)
                .load("https://c-ssl.duitang.com/uploads/item/201811/07/20181107170428_sltox.thumb.700_0.jpeg")
                .apply(RequestOptions.bitmapTransform(new CircleCrop()).placeholder(R.mipmap.ico_loginusertype))
                .into(img_user);

        //事件数据
        txt_willwork = (TextView)findViewById(R.id.txt_willwork);
        txt_work = (TextView)findViewById(R.id.txt_work);
        txt_workedpre = (TextView)findViewById(R.id.txt_workedpre);
        txt_noread = (TextView)findViewById(R.id.txt_noread);

        //菜单
        rv_menu = (RecyclerView)findViewById(R.id.rv_menu);
        rv_menu.setLayoutManager(new GridLayoutManager(mContext,4));
        list_data = new ArrayList<>();
        //tempData();
        mAdapter = new MainMenuAdapter(mContext,list_data);
        rv_menu.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new MainMenuAdapter.OnItemClickLitener() {
            @Override
            public void OnItemClick(View view, int positon) {
                switch (list_data.get(positon).getMenuId())
                {
                    case "1":
                        Jump_intent(EventReportActivity.class,bundle);
                        break;
                    case "13":
                        Jump_intent(EventOverviewActivity.class,bundle);
                        break;
                    case "14":
                        Jump_intent(EventTransmitActivity.class,bundle);
                        break;
                    case "19":
                        Jump_intent(EventImpatientActivity.class,bundle);
                        break;
                    case "2":
                        Jump_intent(EventWaitActivity.class,bundle);
                        break;
                    case "3":
                        Jump_intent(EventPartakeActivity.class,bundle);
                        break;
                    case "9":
                        Jump_intent(InformationActivity.class,bundle);
                        break;
                }
            }
        });

        //升级
        alertDia = new AlertDialog.Builder(mContext).create();

        GetVersion();

        initUserInfo();

        myEventNumber();

        GetMenu();
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
                            Log.e("MainActivity", "行数: 259  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");
                            return;
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("MainActivity", "行数: 122  error:" + response.body());
                    }
                });
    }

    /**
     * 加载我的事件处理的数据
     */
    private void myEventNumber()
    {
        OkGo.<String>post(MAINUSEREVENTNUMBER_URL)
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
                                JSONObject child = json.getJSONObject("data");
                                txt_willwork.setText(child.getString("waitEventCount"));
                                txt_work.setText(child.getString("joinEventCount"));
                                txt_workedpre.setText(child.getString("completionRate"));
                                txt_noread.setText(child.getString("unReadMessageCount"));
                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }
                        }catch (Exception ex)
                        {
                            Log.e("MainActivity", "行数: 135  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");
                            return;
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("MainActivity", "行数: 122  error:" + response.body());
                    }
                });
    }

    /**
     * 得到相应的菜单项
     */
    private void GetMenu()
    {

        OkGo.<String>post(MAINUSERMENU_URL)
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
                                JSONArray array = json.getJSONArray("data");
                                for(int i=0;i<array.length();i++)
                                {
                                    JSONObject child = array.getJSONObject(i);

                                    MainMenuModel model = new MainMenuModel();
                                    if("1".equals(child.getString("id")) || "13".equals(child.getString("id")) || "14".equals(child.getString("id"))
                                            || "19".equals(child.getString("id")) || "2".equals(child.getString("id")) || "3".equals(child.getString("id"))
                                            || "9".equals(child.getString("id")))
                                    {
                                        model.setMenuId(child.getString("id"));
                                        model.setMenuName(child.getString("name"));
                                        list_data.add(model);
                                    }
                                }

                                if(list_data.size() > 0)
                                {
                                    mAdapter.notifyDataSetChanged();
                                }
                            }else{
                                myUntils.showToast(mContext,json.getString("message"));
                            }
                        }catch (Exception ex)
                        {
                            Log.e("MainActivity", "行数: 336  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");
                            return;
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("MainActivity", "行数: 345  error:" + response.body());
                    }
                });
    }

    /**
     * 从服务器取得版本号
     */
    private void GetVersion()
    {
        OkGo.<String>post(DOWNLOADFILE_URL)
                .params("access_token",appToken)
                .tag(this)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        String data = response.body();//这个就是返回来的结果
                        try {
                            try {

                                JSONObject json = new JSONObject(data);
                                String resultCode = json.getString("code");
                                if("2000".equals(resultCode))
                                {

                                    JSONObject mData = json.getJSONObject("data");
                                    VerUpdate(Integer.parseInt(mData.getString("versionId")),mData.getString("version"));
//                                    if(!"[]".equals(json.getString("data"))) {
//
////                                        VersionModel model = gson.fromJson(json.getString("data"),verType);
////
////                                        DownUrl = model.getDownLoadUrl();
//                                        VerUpdate(model.getVersionNum(),model.getVersion());
//                                    }else{
//
//                                        //VerUpdate(1,"1.0.0");
//
//                                    }

                                }else{
                                    myUntils.showToast(mContext,json.getString("message"));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("Main","错误：" + e.getMessage());
                                //myUntils.showToast(mContext,mContext.getResources().getString("ss"));
                            }
                        }catch (Exception ex)
                        {
                            Log.e("LoginActivity", "行数: 135  ex:" + ex.getMessage());
                            myUntils.showToast(mContext,"请检查网络是否正常链接！");
                            return;
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("LoginActivity", "行数: 122  error:" + response.body());
                    }
                });
    }

    /**
     * 升级
     */
    public void VerUpdate(final int appVersionId, final String version) {


        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (appVersionId > myUntils.getLocalVersion(getApplicationContext())) {
                    alertDia.show();
//                            alertDialog.setCanceledOnTouchOutside(false);//调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
                    alertDia.setCancelable(false);//调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用
                    alertDia.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    alertDia.getWindow().setContentView(R.layout.dialog_version);
                    alertDia.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (event.getAction() == KeyEvent.ACTION_UP
                                    && keyCode == KeyEvent.KEYCODE_BACK
                                    && event.getRepeatCount() == 0) {

                                if (myUntils.getLocalVersion(getApplicationContext()) < appVersionId) {
                                    //判断强制更新：点击返回键无效果
                                    alertDia.setCancelable(false);//调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用
                                }
                            }
                            return false;
                        }
                    });


                    pbDownloadApk = alertDia.getWindow().findViewById(R.id.pb_removeview_downloadapk);
                    pbDownloadApk.setEnabled(false);
                    pbTv = alertDia.getWindow().findViewById(R.id.pb_tv);
                    lin_update = alertDia.getWindow().findViewById(R.id.lin_update);
                    go_update = alertDia.getWindow().findViewById(R.id.go_update);
                    tv_ver = alertDia.getWindow().findViewById(R.id.tv_ver);
                    tv_ver.setText(version);

                    go_update.setOnClickListener(new View.OnClickListener() { //自动更新
                        @Override
                        public void onClick(View view) {

                            GoDown();//开始下载
                            //Toast.makeText(mContext, "开启极速下载", Toast.LENGTH_SHORT).show();
                            lin_update.setVisibility(View.VISIBLE);
                            go_update.setVisibility(View.GONE);


                        }
                    });

                }
            }
        }, 0);
    }

    /***
     * 自动更新，断点续传
     */
    private void GoDown() {

        OkGo.<File>get(Constants.DownLoadFilePath)
                .execute(new FileCallback(dirName, Constants.DownLoadFileName) {   //指定下载的路径  下载文件名
                    @Override
                    public void onSuccess(Response<File> response) {
                        installApk();
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        Log.e("MainActivity", "行数: 520  error:" + response.message());

                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        int percent = (int)Math.floor(progress.fraction * 100);
                        pbDownloadApk.setProgress(percent);

                        if (percent < 0) {//遏制有负数的情况
                            percent = 0;
                        }
                        pbTv.setText(percent + "%");

                        go_update.setText(percent + "%");
                    }
                });

    }


    /**
     * 安装apk,第一步
     */
    private void installApk() {

        File file = new File(dirName + Constants.DownLoadFileName);
        if (file.exists()) {
            openFile(file, mContext);
        } else {
            myUntils.showToast(mContext,"下载失败");
        }

    }

    /**
     * 安装apk,第二步
     * @param var0
     * @param var1
     */
    public void openFile(File var0, Context var1) {
        Intent var2 = new Intent();
        var2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        var2.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uriForFile = FileProvider.getUriForFile(var1, var1.getApplicationContext().getPackageName() + ".provider", var0);
            var2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            var2.setDataAndType(uriForFile, var1.getContentResolver().getType(uriForFile));
        } else {
            var2.setDataAndType(Uri.parse("file://" + var0.toString()),
                    "application/vnd.android.package-archive");
        }
        try {
            var1.startActivity(var2);
        } catch (Exception var5) {
            var5.printStackTrace();
            myUntils.showToast(mContext,"没有找到打开此类文件的程序");
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
        switch (v.getId())
        {
            //用户信息
            case R.id.rela_userInfo:

                break;
            //被传阅事件
            case R.id.title_right_btn:
                Jump_intent(EventCirculatedActivity.class,bundle);
                break;
        }
    }




    /**
     * 连续点击两次退出系统
     * @param keyCode     按钮code
     * @param event       事件
     * @return            返回值
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            } else {
                myUntils.showToast(mContext,"再按一次退出程序");
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
