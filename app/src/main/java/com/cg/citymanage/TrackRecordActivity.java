package com.cg.citymanage;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.cg.citymanage.infos.Constants;
import com.cg.citymanage.services.TrackService;
import com.cg.citymanage.untils.myUntils;

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

    private boolean isStart;      //判断service是否运行
    private  BC bc=new BC();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        appToken = mSharedPreferences.getString("appToken", "");
        //isStart = true;
        isStart = myUntils.isServiceRunning(mContext,"com.cg.citymanage.services.TrackService");


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
        if(isStart)
        {
            btn_search.setText("停止记录");
        }else{
            btn_search.setText("点击开始\n记录");
        }

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
        latLngs = new ArrayList<>();

        //设置中心点
        target = new LatLng(Constants.BAIDUCENTERLAT,Constants.BAIDUCENTERLNG);
        //latLngs.add(target);
        //设置缩放中点LatLng target，和缩放比例
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(target).zoom(18f);

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

            case R.id.btn_search:
                if(!myUntils.checkGalleryPermission(mContext,this,Manifest.permission.ACCESS_COARSE_LOCATION) ||
                        !myUntils.checkGalleryPermission(mContext,this, Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    myUntils.showToast(mContext,"您没有打开基站和GPS访问权限，无法进行轨迹记录，请打开相关权限");
                }else {

                    if (!isStart) {
                        TrackService.startServer(mContext, appToken);
                        btn_search.setText("停止记录");
                    } else {
                        TrackService.stopServer(mContext);
                        btn_search.setText("点击开始\n记录");
                    }
                    isStart = !isStart;
                }
                break;
            //跳转到查询页面
            case R.id.title_right_btn:
                Jump_intent(TrackSearchActivity.class,bundle);
                break;
        }
    }



    //自定义 BroadcastReceiver
    public class BC extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String lnglat=intent.getStringExtra("DATA");
            //Log.e("TrackRecordActivity", "行数: 256  lnglat:" + lnglat);
            //txt_gps.setText(txt_gps.getText() + "    lnglat:" + lnglat + "\n");
            if(lnglat.contains(",")) {
                String[] array = lnglat.split(",");
                LatLng latLng = new LatLng(Double.valueOf(array[0]),Double.valueOf(array[1]));

                //Log.e("TrackRecordActivity", "行数: 238  lon:" + latLng.longitude + " lat:" + latLng.latitude);
                drawLine(getBaidu(latLng));
            }

        }
    }

    /**
     * 根据得到的经纬度，在百度地图上画出线路
     * @param latLng              百度经纬度
     */
    private void drawLine(LatLng latLng)
    {
        latLngs.add(latLng);
        if(latLngs.size()==1)
        {
            //将中心点移动到所有点的中心
            MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(latLng, 18);
            mBaiduMap.setMapStatus(mapStatus);
        }else {
            OverlayOptions ooPolyline = new PolylineOptions().width(8).color(0xAAFF0000).points(latLngs);

            //在地图上画出线条图层，mPolyline：线条图层
            mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
            mPolyline.setZIndex(3);
        }
    }

    //初始化坐标转换工具类，指定源坐标类型和坐标数据
    // sourceLatLng待转换坐标
    private LatLng getBaidu(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter()
                .from(CoordinateConverter.CoordType.GPS)
                .coord(sourceLatLng);

        //desLatLng 转换后的坐标
        return converter.convert();
    }

}
