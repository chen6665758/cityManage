package com.cg.citymanage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cg.citymanage.infos.Constants;
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

* 功能：轨迹记录查询页面
* 作者：cg
* 时间：2019/11/25 15:15
*/
public class TrackSearchActivity extends BaseActivity implements View.OnClickListener {

    private String appToken;

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 查询条件
     */
    private TextView txt_mapsearch;
    private TimePickerView pvTime;

    /**
     * 地图显示
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    LatLng target;                  //中心点
    private List<LatLng> latLngs;   //点的集合
    public Overlay mPolyline;       //覆盖层

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        appToken = mSharedPreferences.getString("appToken", "");

        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_track_search);
    }

    /**
     * 初始化控件
     */
    private void initControls()
    {
        //标题栏
        title_left_btn = (ImageButton) findViewById(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_textview = (TextView) findViewById(R.id.title_textview);
        title_textview.setText("轨迹查询");

        //查询条件
        txt_mapsearch = (TextView)findViewById(R.id.txt_mapsearch);
        txt_mapsearch.setOnClickListener(this);
        initDate();

        initMap();
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
                txt_mapsearch.setText(myUntils.getTime(date));
                LoadData();
            }
        }).build();
    }


    /**
     * 初始化地图控件
     */
    private void initMap()
    {
        mMapView = (MapView)findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        //获取运动后的定位点
        //coordinateConvert();
        latLngs = new ArrayList<>();

        //设置中心点
        target = new LatLng(45.737774,126.648273);
        latLngs.add(target);
        //设置缩放中点LatLng target，和缩放比例
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(target).zoom(16f);

        //地图设置缩放状态
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }



    @Override
    public void Jump_intent(Class<?> cla, Bundle bundle) {
        if (mContext != null) {
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

            case R.id.txt_mapsearch:
                pvTime.show();
                break;
        }
    }

    /**
     * 获取日期的路径经纬度
     */
    private void LoadData()
    {
        if(TextUtils.isEmpty(txt_mapsearch.getText().toString()))
        {
            myUntils.showToast(mContext,"请先选择一个日期！");

        }else {
            progress_Dialog.show();
            OkGo.<String>post(Constants.TRACKSEARCH_URL)
                    .tag(this)//
                    .params("access_token", appToken)
                    .params("time", txt_mapsearch.getText().toString())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            //注意这里已经是在主线程了
                            progress_Dialog.dismiss();
                            String data = response.body();//这个就是返回来的结果
                            try {
                                JSONObject json = new JSONObject(data);
                                String resultCode = json.getString("code");

                                if (resultCode.equals("2000")) {
                                    JSONArray array = json.getJSONArray("data");
                                    if(array.length() > 0)
                                    {
                                        for(int i=0;i<array.length();i++)
                                        {
                                            JSONObject object = array.getJSONObject(i);
                                            if(!"null".equals(object.getString("lng")) && !"null".equals(object.getString("lat")))
                                            {
                                                LatLng latLng = new LatLng(object.getDouble("lat"),object.getDouble("lng"));
                                                latLngs.add(latLng);
                                            }
                                        }

                                        if(latLngs.size() > 0)
                                        {
                                            //计算中心点，将所有点的中心设置成屏幕的中心点
                                            LatLng center = new LatLng(latLngs.get(0).latitude,latLngs.get(0).longitude);
                                            if(latLngs.size() > 2)
                                            {
                                                center = new LatLng(latLngs.get(latLngs.size()/2).latitude,latLngs.get(latLngs.size()/2).longitude);
                                            }
                                            //将中心点移动到所有点的中心
                                            MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(center, 16);
                                            mBaiduMap.setMapStatus(mapStatus);

                                            //将线画出来
                                            OverlayOptions ooPolyline = new PolylineOptions().width(8).color(0xAAFF0000).points(latLngs);
                                            //在地图上画出线条图层，mPolyline：线条图层
                                            mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
                                            mPolyline.setZIndex(3);


                                            //添加起始点的坐标
                                            //始点图层图标
                                            BitmapDescriptor startBD= BitmapDescriptorFactory
                                                    .fromResource(R.mipmap.icon_start);
                                            //终点图层图标
                                            BitmapDescriptor finishBD= BitmapDescriptorFactory
                                                    .fromResource(R.mipmap.icon_end);
                                            MarkerOptions oStart = new MarkerOptions();//地图标记类型的图层参数配置类
                                            oStart.position(latLngs.get(0));//图层位置点，第一个点为起点
                                            oStart.icon(startBD);//设置图层图片
                                            oStart.zIndex(1);//设置图层Index

                                            //添加起点图层
                                           mBaiduMap.addOverlay(oStart);

                                            if(latLngs.size()>1) {
                                                //添加终点图层
                                                MarkerOptions oFinish = new MarkerOptions().position(latLngs.get(latLngs.size() - 1))
                                                        .icon(finishBD).zIndex(2);
                                                mBaiduMap.addOverlay(oFinish);
                                            }


                                        }else{
                                            myUntils.showToast(mContext,"对不起，当天没有轨迹记录点");
                                        }
                                    }else{
                                        mBaiduMap.clear();
                                        myUntils.showToast(mContext,"对不起，当天没有轨迹记录点");

                                    }
                                }else{
                                    myUntils.showToast(mContext,json.getString("message"));
                                }
                            } catch (Exception ex) {
                                Log.e("TrackSearch", "行数: 240  ex:" + ex.getMessage());

                            }

                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            progress_Dialog.dismiss();
                            Log.e("TrackSearch", "行数: 250  error:" + response.body());
                        }
                    });
        }
    }
}
