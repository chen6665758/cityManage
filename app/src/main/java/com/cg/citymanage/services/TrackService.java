package com.cg.citymanage.services;

import android.Manifest;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.cg.citymanage.infos.Constants;
import com.cg.citymanage.untils.LiveDataBus;
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

* 功能：轨迹记录中Service，得到当前的gps值
* 作者：cg
* 时间：2019/11/21 15:23
*/
public class TrackService extends IntentService implements LocationListener {

    private LocationManager locationManager;
    private int count=0;
    private String locationStatus;        //0是GPS  1是NET

    private double oldlat = 0;
    private double oldlng = 0;

    private String appToken;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public TrackService() {
        super("Track");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startId=START_NOT_STICKY;
        appToken = intent.getStringExtra("appToken");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationStatus = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            myUntils.showToast(this, "无法定位，请打开定位服务");
            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location==null){
            locationStatus = LocationManager.NETWORK_PROVIDER;
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        Log.e("TrackService.java(onHandleIntent)", "行数: 83  locationStatus:" + locationStatus);

        if(location != null)
        {
            //设置数据更新的条件，参数分别为1，使用GPS 2，最小时间间隔 3000毫秒 3，最短距离 100  4,设置事件监听者 this(类继承了Locationlistener)
            locationManager.requestLocationUpdates(locationStatus,3000, 1,this);
        }else{
            myUntils.showToast(this, "对不起，您的手机没有可以定位的设备");
            return;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        Log.e("TrackService", "纬度 =" + latitude);
        double longitude = location.getLongitude();

        Log.e("TrackService.java(onLocationChanged)", "行数: 107  appToken:" + appToken);
        //将location作为参数传递给广播
        //当经纬度坐标不一样时，才传数据
        if(oldlat!=latitude && longitude!=oldlng) {
            uploadData(String.valueOf(longitude),String.valueOf(latitude));

            BCL(location);
            oldlat = latitude;
            oldlng = longitude;
        }

    }

    /**
     *   使用广播将经纬度等信息传递出去
     * @param location
     */
    private void BCL(Location location) {
        Log.e("TrackService.java(BCL)", "行数: 119  这里没有跑BCL");
        PendingIntent pi = getPI(location);
        //permission check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //第四个参数为PendingIntent
        locationManager.requestLocationUpdates(locationStatus, 3000, 1, pi);
    }

    /**
     *
     * @param location
     * @return   返回一个已包装数据的PendingIntent
     */
    private PendingIntent getPI(Location location) {
        Intent intent = new Intent();
        intent.setAction("location1");

        intent.putExtra("DATA",location.getLatitude()+ "," + location.getLongitude());
        return PendingIntent.getBroadcast(getApplication(), 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



    /**
     * 开启服务
     */
    public static void startServer(Context mContext,String appToken){
        Intent intent = new Intent(mContext,TrackService.class);
        intent.putExtra("appToken",appToken);
        mContext.startService(intent);
    }

    /**
     * 关闭服务
     * @param mContext        上下文
     */
    public static void stopServer(Context mContext)
    {
        Intent intent = new Intent(mContext,TrackService.class);
        mContext.stopService(intent);
    }


    private void uploadData(String lng,String lat)
    {
        OkGo.<String>post(Constants.TRACKRECORDADD_URL)
                .tag(this)//
                .params("access_token", appToken)
                .params("lng",lng)
                .params("lat",lat)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        String data = response.body();//这个就是返回来的结果
                        Log.e("TrackService.java(onSuccess)", "行数: 208  data:" + data);
                        try {
                            JSONObject json = new JSONObject(data);
                            String resultCode = json.getString("code");

                            if(resultCode.equals("2000")) {
                            }
                        }catch (Exception ex)
                        {
                            Log.e("TrackService", "行数: 196  ex:" + ex.getMessage());
                            return;
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("TrackService", "行数: 257  error:" + response.body());
                    }
                });
    }

}
