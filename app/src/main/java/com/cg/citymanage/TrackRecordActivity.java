package com.cg.citymanage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.cg.citymanage.services.TrackService;
import com.cg.citymanage.services.tempService;
import com.cg.citymanage.untils.CoordinateConversion;
import com.cg.citymanage.untils.LiveDataBus;
import com.cg.citymanage.untils.myUntils;
import com.supermap.services.util.CoordinateConversionTool;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.mapapi.utils.CoordinateConverter.CoordType.COMMON;

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

 * 功能：轨迹记录
 * 作者：cg
 * 时间：2019/11/20 9:49
 */
public class TrackRecordActivity extends BaseActivity implements View.OnClickListener {

    private String appToken;

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;
    private RelativeLayout rl_2;
    private TextView title_right_btn;

    /**
     * 地图显示
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    LatLng target;                  //中心点
    private List<LatLng> latLngs;   //点的集合
    public Overlay mPolyline;       //覆盖层


    private TextView txt_gps;
    private Button btn_search;

    private boolean isStart;
    private  BC bc=new BC();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        appToken = mSharedPreferences.getString("appToken", "");
        isStart = true;

        //权限的设置
        myUntils.JudgePermission(this, mContext, "您拒绝了读取当前定位，轨迹记录将无法使用！", Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);

        initControls();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //动态注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("location1");
        registerReceiver(bc, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bc);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_track_record);
    }

    /**
     * 初始化控件
     */
    private void initControls() {
        //标题栏
        title_left_btn = (ImageButton) findViewById(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_textview = (TextView) findViewById(R.id.title_textview);
        title_textview.setText("轨迹记录");
        rl_2 = (RelativeLayout)findViewById(R.id.rl_2);
        rl_2.setVisibility(View.VISIBLE);
        title_right_btn = (TextView)findViewById(R.id.title_right_btn);
        title_right_btn.setOnClickListener(this);
        title_right_btn.setBackgroundResource(R.mipmap.ico_details_search_pre);

        initMap();


        txt_gps = (TextView) findViewById(R.id.txt_gps);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);


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
        /**
         * 配置线段图层参数类： PolylineOptions
         * ooPolyline.width(13)：线宽
         * ooPolyline.color(0xAAFF0000)：线条颜色红色
         * ooPolyline.points(latLngs)：List<LatLng> latLngs位置点，将相邻点与点连成线就成了轨迹了
         */
//        OverlayOptions ooPolyline = new PolylineOptions().width(8).color(0xAAFF0000).points(latLngs);
//
//        //在地图上画出线条图层，mPolyline：线条图层
//        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
//        mPolyline.setZIndex(3);

        //始点图层图标
        BitmapDescriptor startBD= BitmapDescriptorFactory
                .fromResource(R.mipmap.loginuser);
//        //终点图层图标
//        BitmapDescriptor finishBD= BitmapDescriptorFactory
//                .fromResource(R.mipmap.loginpassword);
//
        MarkerOptions oStart = new MarkerOptions();//地图标记类型的图层参数配置类
        oStart.position(latLngs.get(0));//图层位置点，第一个点为起点
        oStart.icon(startBD);//设置图层图片
        oStart.zIndex(1);//设置图层Index

        //添加起点图层
        Marker mMarkerA = (Marker) (mBaiduMap.addOverlay(oStart));
//
//        //添加终点图层
//        MarkerOptions oFinish = new MarkerOptions().position(latLngs.get(latLngs.size()-1))
//                .icon(finishBD).zIndex(2);
//        Marker mMarkerB = (Marker) (mBaiduMap.addOverlay(oFinish));
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

            case R.id.btn_search:
                if(isStart) {
                    TrackService.startServer(mContext,appToken);
                    btn_search.setText("停止记录");
//                    Intent start= new Intent(mContext,tempService.class);
//                    startService(start);
                }
                else{
                    TrackService.stopServer(mContext);
                    btn_search.setText("点击开始\n记录");
//                    Intent stop= new Intent(mContext,tempService.class);
//                    stopService(stop);
                }
                isStart = !isStart;
                break;
            //跳转到查询页面
            case R.id.title_right_btn:
                Jump_intent(TrackSearchActivity.class,bundle);
                break;
        }
    }



    //自定义 BroadcastReceiver
    public class BC extends BroadcastReceiver {
        public static final String TAG="BC";

        @Override
        public void onReceive(Context context, Intent intent) {
            String lnglat=intent.getStringExtra("DATA");
            Log.e("TrackRecordActivity", "行数: 256  lnglat:" + lnglat);
            txt_gps.setText(txt_gps.getText() + "    lnglat:" + lnglat + "\n");
            if(lnglat.contains(",")) {
                String[] array = lnglat.split(",");
                LatLng latLng = new LatLng(Double.valueOf(array[0]),Double.valueOf(array[1]));

                drawLine(getBaidu(latLng));
            }

        }
    }


    private void drawLine(LatLng latLng)
    {
        Log.e("TrackRecordActivity.java(drawLine)", "行数: 290  latLng:" + latLng);
        latLngs.add(latLng);
        OverlayOptions ooPolyline = new PolylineOptions().width(8).color(0xAAFF0000).points(latLngs);

        //在地图上画出线条图层，mPolyline：线条图层
        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
        mPolyline.setZIndex(3);
    }

    //初始化坐标转换工具类，指定源坐标类型和坐标数据
    // sourceLatLng待转换坐标

    private LatLng getBaidu(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter()
                .from(CoordinateConverter.CoordType.GPS)
                .coord(sourceLatLng);

        //desLatLng 转换后的坐标
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

}
