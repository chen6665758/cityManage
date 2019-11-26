package com.cg.citymanage.untils;

import android.app.Activity;
import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.cg.citymanage.services.InitIntentService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.jessyan.autosize.AutoSizeConfig;
import okhttp3.OkHttpClient;

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

* 功能：自定义的Application,用于初始化一些控件与方法
* 作者：cg
* 时间：2019/9/20 8:41
*/
public class myApplication extends Application {

    private static myApplication mInstance;
    private static List<Activity> activityList = new LinkedList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        /**
         * 设置今日头条的一些设置
         */
        //initAutoSize();

        //OKGO网络加载框架初始化
        //initOKGO();
        initBaiDu();

        InitIntentService.start(this);
    }

    public static myApplication getInstance() {
        return mInstance;
    }

    public void addActivity(Activity activity)  {
        activityList.add(activity);
    }
    public void removeActivity(Activity activity){
        activityList.remove(activity);
    }
    public void exitAllActivity(){
        for(Activity activity:activityList) {
            activity.finish();
        }
    }


    /**
     * 今日头条适配框架的一些设置
     */
    private void initAutoSize() {


        /*当某个 Activity 的设计图尺寸与在 AndroidManifest 中填写的全局设计图尺寸不同时，可以实现 CustomAdapt 接口扩展适配参数

        public class CustomAdaptActivity extends AppCompatActivity implements CustomAdapt {
            @Override
            public boolean isBaseOnWidth() {
                return false;
            }
            @Override
            public float getSizeInDp() {
                return 667;
            }
        }


        当某个 Activity 想放弃适配，请实现 CancelAdapt 接口

        public class CancelAdaptActivity extends AppCompatActivity implements CancelAdapt {

        }

        Fragment

        首先开启支持 Fragment 自定义参数的功能

        AutoSizeConfig.getInstance().setCustomFragment(true);

        当某个 Fragment 的设计图尺寸与在 AndroidManifest 中填写的全局设计图尺寸不同时，可以实现 CustomAdapt 接口扩展适配参数

        public class CustomAdaptFragment extends Fragment implements CustomAdapt {

            @Override
            public boolean isBaseOnWidth() {
                return false;
            }

            @Override
            public float getSizeInDp() {
                return 667;
            }
        }

        当某个 Fragment 想放弃适配，请实现 CancelAdapt 接口

        public class CancelAdaptFragment extends Fragment implements CancelAdapt {

        }


        */



        AutoSizeConfig.getInstance()
                //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
                .setCustomFragment(true)
                //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变
                //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
                .setExcludeFontScale(true);
    }

    /**
     * OKGO网络加载框架初始化
     */
    private void initOKGO()
    {
        OkGo.getInstance().init(this);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));

        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();

        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");
//        HttpParams params = new HttpParams();
//        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
//        params.put("commonParamsKey2", "这里支持中文参数");


        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers);                      //全局公共头
                //.addCommonParams(params);                       //全局公共参数
    }

    /**
     * 百度地图初始化
     */
    private void initBaiDu()
    {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
