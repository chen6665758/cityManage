package com.cg.citymanage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.cg.citymanage.untils.CoordinateConversion;
import com.cg.citymanage.untils.myUntils;
import com.supermap.imobilelite.commons.EventStatus;
import com.supermap.imobilelite.maps.DefaultItemizedOverlay;
import com.supermap.imobilelite.maps.LayerView;
import com.supermap.imobilelite.maps.MapView;
import com.supermap.imobilelite.maps.Overlay;
import com.supermap.imobilelite.maps.OverlayItem;
import com.supermap.imobilelite.maps.Point2D;
import com.supermap.imobilelite.maps.query.FilterParameter;
import com.supermap.imobilelite.maps.query.QueryByDistanceParameters;
import com.supermap.imobilelite.maps.query.QueryByDistanceService;
import com.supermap.imobilelite.maps.query.QueryByGeometryParameters;
import com.supermap.imobilelite.maps.query.QueryByGeometryService;
import com.supermap.imobilelite.maps.query.QueryEventListener;
import com.supermap.imobilelite.maps.query.QueryResult;
import com.supermap.imobilelite.maps.query.SpatialQueryMode;
import com.supermap.services.components.commontypes.Feature;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.services.components.commontypes.GeometryType;

import static com.cg.citymanage.infos.Constants.BaseMap_URL;
import static com.cg.citymanage.infos.Constants.DataMap_URl;

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

* 功能：地图点选页面
* 作者：cg
* 时间：2019/10/30 13:31
*/
public class MapSelectActivity extends BaseActivity implements View.OnClickListener {

    private String mapclass;
    private String siteValue;
    private String gridId;
    private String gridName;
    private String address;
    private String lng;
    private String lat;

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;
    private RelativeLayout rl_2;
    private TextView title_right_btn;

    /**
     * 地图
     */
    private MapView mapview;
    //地图图层
    private LayerView layerView1;
    private LayerView layerView2;
    private LayerView[] layerViews;
    private Point2D centerPoint2D;//中心点坐标

    private DefaultItemizedOverlay overlay;

    // 触屏的x坐标
    private int touchX;

    // 触屏的y坐标
    private int touchY;

    private int lasttouchX;
    private int lasttouchY;

    //屏幕坐标转化
    private Point2D longTouchGeoPoint = null;
    private boolean isSelectPoint; //是否点击点

    /**
     * 地图坐标转化
     */
    private WebView locationWebview;
    private GeoCoder geoCoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mapclass = getIntent().getStringExtra("mapclass");
        //centerPoint2D = new Point2D(575215.42774951, 5167223.4071475);
        centerPoint2D = new Point2D(536982.8194663200, 5345986.7709373999);

        initControls();
    }

    @Override
    protected void onDestroy() {

        if(mapview != null)
        {
            mapview.destroy();
        }

        super.onDestroy();

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_map_select);
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
        title_textview.setText("选择网格");
        rl_2 = (RelativeLayout)findViewById(R.id.rl_2);
        //rl_2.setVisibility(View.VISIBLE);
        title_right_btn = (TextView)findViewById(R.id.title_right_btn);
        title_right_btn.setText("提交数据");
        title_right_btn.setVisibility(View.VISIBLE);
        title_right_btn.setOnClickListener(this);


        //地图
        mapview = (MapView)findViewById(R.id.mapview);
        //创建地图图层，并指向iServer提供的地图服务
        layerView1 = new LayerView(this);
        layerView1.setURL(BaseMap_URL);
        layerView2 = new LayerView(this);
        layerView2.setURL(DataMap_URl);

        LayerView[] layerViews=new LayerView[2];
        layerViews[0] = layerView1;
        layerViews[1] = layerView2;

        mapview.addLayers(layerViews);
        //设置地图缩放
        mapview.setBuiltInZoomControls(true);
        //设置地图中心点
        //mapview.getController().setCenter(centerPoint2D);
        mapview.getController().setCenter(centerPoint2D);
        //设置地图的显示比例，数值越大显示的越细
        mapview.getController().setZoom(3);


        //地图点击时，标的点的图标
        Drawable drawableBlue = getResources().getDrawable(R.mipmap.icon_overlay_nearby_center);
        overlay = new DefaultItemizedOverlay(drawableBlue);

        //为地图提供touch事件，主要是为了解决点击，移动等问题
        TouchOverlay touchOverlay = new TouchOverlay();
        mapview.getOverlays().add(touchOverlay);

        initGeoCoder();
    }

    /**
     * 触屏Overlay
     */
    class TouchOverlay extends Overlay {
        @Override
        public boolean onTouchEvent(MotionEvent event, final MapView mapView) {
            touchX =  Math.round(event.getX());
            touchY = Math.round(event.getY());

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    isSelectPoint = true;
                    lasttouchX = touchX;
                    lasttouchY = touchY;
                    break;
                case MotionEvent.ACTION_MOVE:
                    //当为点击事件时，这里要做判断，用户是在刷动地图，还是点选点
                    //当左右移动 或是 上下移动 距离大于20的时候，就算是移动了，不做点选点的处理
                    if((Math.abs(lasttouchX - touchX) > 20) || (Math.abs(lasttouchY - touchY) > 20)) {
                        isSelectPoint = false;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    // 记录点击位置
                    if(isSelectPoint) {

                        longTouchGeoPoint = mapView.getProjection().fromPixels(touchX, touchY);
                        lng = String.valueOf(longTouchGeoPoint.x);
                        lat = String.valueOf(longTouchGeoPoint.y);
                        //Log.e("MapSelectActivity", "行数: 240  lng:" + lng + " lat:" + lat);
                        overlay.clear();
                        OverlayItem overlayItem1 = new OverlayItem(longTouchGeoPoint, "绥化", "绥化");
                        overlay.addItem(overlayItem1);
                        mapview.getOverlays().add(overlay);
                        mapview.invalidate();

                        //distanceQuery();
                        geometryQuery();
                        return true;
                    }

            }

            return false;
        }
    }

    /**
     * 距离查询
     */
    private void distanceQuery()
    {
        Log.e("ThreeActivity", "行数: 154  distanceQuery");
        QueryByDistanceParameters p = new QueryByDistanceParameters();

        p.distance = 500; //必设，查询距离，单位为地理单位
        Geometry g = new Geometry();
        // 构建点地物，必设
        g.type = GeometryType.POINT;

        Log.e("OneActivity", "行数: 156  X:" + longTouchGeoPoint.getX() + " X2:" + longTouchGeoPoint.x);

        g.points = new com.supermap.services.components.commontypes.Point2D[]{
                new com.supermap.services.components.commontypes.Point2D(longTouchGeoPoint.x,longTouchGeoPoint.y)
        };
        g.parts = new int[]{1};
        p.geometry = g;

        FilterParameter fp = new FilterParameter();

        fp.name = "BuildRegion_habin@china";
        p.filterParameters = new FilterParameter[]{fp};
        QueryByDistanceService qs = new QueryByDistanceService(DataMap_URl);
        Log.e("ThreeActivity", "行数: 171  qs取值了");
        qs.process(p, new DistanceQueryEventListener());
    }

    /**
     * 范围查询
     */
    private void geometryQuery()
    {
        QueryByGeometryParameters p = new QueryByGeometryParameters();
        p.spatialQueryMode = SpatialQueryMode.WITHIN;// 必设，空间查询模式，默认相交
        // 构建查询几何对象
        Geometry g = new Geometry();
        g.type = GeometryType.POINT;
        g.points = new com.supermap.services.components.commontypes.Point2D[] { new com.supermap.services.components.commontypes.Point2D(longTouchGeoPoint.x,longTouchGeoPoint.y) };
        g.parts = new int[] { 1 };
        p.geometry = g;
        FilterParameter fp = new FilterParameter();
        fp.name = "BuildRegion_habin@china";// 必设参数，图层名称格式：数据集名称@数据源别名

        p.filterParameters = new FilterParameter[] { fp };
        QueryByGeometryService qs = new QueryByGeometryService(DataMap_URl);
        qs.process(p, new MyQueryEventListener());
    }

    public class MyQueryEventListener extends QueryEventListener {

        /**
         * <p>
         * 查询完成回调该接口，用户根据需要处理结果sourceObject
         * </p>
         * @param sourceObject 查询结果
         * @param status 查询结果状态
         */
        @Override
        public void onQueryStatusChanged(Object sourceObject, EventStatus status) {
            Message msg = new Message();
            if (sourceObject instanceof QueryResult && status.equals(EventStatus.PROCESS_COMPLETE)) {
                QueryResult qr = (QueryResult) sourceObject;
                if(qr != null && qr.quertyResultInfo != null && qr.quertyResultInfo.recordsets != null)
                {
                    for(int i=0; i<qr.quertyResultInfo.recordsets.length;i++)
                    {
                        Feature[] features = qr.quertyResultInfo.recordsets[i].features;
                        if(features != null)
                        {
                            for(int j=0;j<features.length;j++)
                            {
                                Feature feature= features[j];
                                for(int k=0;k<feature.fieldNames.length;k++)
                                {
                                    //Log.e("MapSelect", "行数: 355  网格数据：" + feature.fieldNames[k] + "  value:" + feature.fieldValues[k]);
                                    if("GridName".equals(feature.fieldNames[k]))
                                    {
                                        //Log.e("MapSelectActivity.java(onQueryStatusChanged)", "行数: 309  没走吗");
                                        gridName = feature.fieldValues[k];

                                    }
                                    if("GridCode".equals(feature.fieldNames[k]))
                                    {
                                        gridId = feature.fieldValues[k];
                                    }
                                }
                            }
                        }
                    }

                    if(TextUtils.isEmpty(gridName) || TextUtils.isEmpty(gridId))
                    {
                        msg.what = 1;
                    }else{
                        msg.what = 0;
                    }

                }else{
                    myUntils.showToast(mContext,"点击不在范围内，请确认！");
                    msg.what = 1;
                }
            } else {
                msg.what = 1;
                Log.e("MapSelect", "行数: 338  geometryQuery错误");
                myUntils.showToast(mContext,"点击不在范围内，请确认！");
            }
            // 子线程不能直接调用UI相关控件，所以只能通过把结果以消息的方式告知UI主线程展示结果
            handler.sendMessage(msg);
        }
    }


    Handler handler = new Handler(){

        public void handleMessage(Message msg) {

            if(msg.what == 0)
            {
                progress_Dialog.show();
                initWebView();
                rl_2.setVisibility(View.VISIBLE);
            }else{
                myUntils.showToast(mContext,"点击不在范围内，请确认！");
            }
        }
    };


    class DistanceQueryEventListener extends QueryEventListener
    {

        @Override
        public void onQueryStatusChanged(Object sourceObject, EventStatus eventStatus) {
            if (sourceObject instanceof QueryResult && eventStatus.equals(EventStatus.PROCESS_COMPLETE)) {

                Log.e("ThreeActivity", "行数: 180  这里走了吗，取出来的数据是什么");

            }else{
                Log.e("ThreeActivity", "行数: 186  这里是错误吗");
            }
        }
    }



    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView()
    {
        final double[] location = new double[]{Double.valueOf(lng), Double.valueOf(lat)};   //575215.42774951, 5167223.4071475   //
        locationWebview = new WebView(this);
        locationWebview.getSettings().setJavaScriptEnabled(true);

        locationWebview.loadUrl("file:///android_asset/gauss-kruger.html");
        locationWebview.addJavascriptInterface(this,"androidTransform");

        locationWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    //progress到一百后就是加载完成，在里边写你要调用的方法就行了
                    locationWebview.loadUrl("javascript:mMercatorToLonLat('" + "EPSG:4326" + "'," + location[0] + "," + location[1] + ")");
                }
            }
        });
    }

    @JavascriptInterface
    public void LonLatToMercatorCallback(double lon,double lat)
    {
        double[] lnglat = CoordinateConversion.wgs84tobd09(lon,lat);
        LatLng latLng = new LatLng(lnglat[1],lnglat[0]);
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng).pageNum(0).pageSize(100));
    }

    private void initGeoCoder() {
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

                progress_Dialog.dismiss();

                if (reverseGeoCodeResult == null
                        || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                    return;
                }



                String myAddress = reverseGeoCodeResult.getAddress();
                String BusinessCircle = reverseGeoCodeResult.getBusinessCircle();

                ReverseGeoCodeResult.AddressComponent addressDetail = reverseGeoCodeResult.getAddressDetail();

                address = myAddress;
                siteValue = addressDetail.province + addressDetail.city;

                Log.e("MapSelectActivity.java(onGetReverseGeoCodeResult)", "行数: 474  address:" + address);

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
            //取得点选的网格
            case R.id.title_right_btn:
                Intent intent = new Intent();
                if("report".equals(mapclass)) {
                    intent.setClass(mContext, EventReportActivity.class);
                }else if("over".equals(mapclass))
                {
                    intent.setClass(mContext,EventOverviewActivity.class);
                }else if("partsearch".equals(mapclass))
                {
                    intent.setClass(mContext,PartStatisticsSearchActivity.class);
                }else if("partadd".equals(mapclass))
                {
                    intent.setClass(mContext,PartsAddActivity.class);
                }
                intent.putExtra("lng",lng);
                intent.putExtra("lat",lat);
                intent.putExtra("siteValue", siteValue);
                intent.putExtra("gridId",gridId);
                intent.putExtra("gridName", gridName);
                intent.putExtra("address",address);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
