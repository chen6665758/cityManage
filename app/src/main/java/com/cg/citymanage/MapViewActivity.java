package com.cg.citymanage;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cg.citymanage.untils.myUntils;
import com.supermap.imobilelite.maps.DefaultItemizedOverlay;
import com.supermap.imobilelite.maps.LayerView;
import com.supermap.imobilelite.maps.MapView;
import com.supermap.imobilelite.maps.OverlayItem;
import com.supermap.imobilelite.maps.Point2D;

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

* 功能：在地图上标出相应该点
* 作者：cg
* 时间：2019/11/7 8:32
*/
public class MapViewActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 地图
     */
    private MapView mapview;
    //地图图层
    private LayerView layerView1;
    private LayerView layerView2;
    private LayerView[] layerViews;
    private Point2D centerPoint2D;//中心点坐标
    private double lng;
    private double lat;

    private DefaultItemizedOverlay overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        lng = getIntent().getDoubleExtra("lng",0);
        lat = getIntent().getDoubleExtra("lat",0);
        if(lng==0 || lat==0 )
        {
            myUntils.showToast(mContext,"对不起，地点信息不正确！地图无法显示");
            finish();
        }

        centerPoint2D = new Point2D(lng, lat);
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
        setContentView(R.layout.activity_map_view);
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
        title_textview.setText("发生位置");

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

        overlay.clear();
        OverlayItem overlayItem1 = new OverlayItem(centerPoint2D, "绥化", "绥化");
        overlay.addItem(overlayItem1);
        mapview.getOverlays().add(overlay);
        mapview.invalidate();
    }

    @Override
    public void Jump_intent(Class<?> cla, Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            //返回
            case R.id.title_left_btn:
                finish();
                break;

        }
    }
}
