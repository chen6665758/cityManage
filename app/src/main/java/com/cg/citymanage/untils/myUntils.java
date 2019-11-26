package com.cg.citymanage.untils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.cg.citymanage.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

* 功能：通用方法类
* 作者：cg
* 时间：2019/3/11 9:15
*/
public class myUntils {

    /**
     * 获取本地软件版本号
     */
    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
            localVersion = Integer.parseInt(String.valueOf(localVersion).replace("999",""));
            System.out.println("本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 转换日期格式
     *
     * @param date       　　　　　　　　　　　　　　　　想要格式化的日期
     * @param oldPattern 想要格式化的日期的现有格式
     * @param newPattern 想要格式化成什么格式
     * @return 转化后的日期
     */
    public static String StringPattern(String date, String oldPattern, String newPattern) {
        if (date == null || oldPattern == null || newPattern == null)
            return "";
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern);        // 实例化模板对象
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern);        // 实例化模板对象
        Date d = null;
        try {
            d = sdf1.parse(date);   // 将给定的字符串中的日期提取出来
        } catch (Exception e) {            // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace();       // 打印异常信息
            return "0000-00-00";
        }
        return sdf2.format(d);
    }

    public static String getTime(Date date) {//可根据需要自行截取数据显示

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 获取系统时间戳 * @return
     */
    public static long getCurTimeLong() {
        long time = System.currentTimeMillis();
        return time;
    }

    /**
     * 获取当前时间 * @param pattern * @return
     */
    public static String getCurDate(String pattern) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date());
    }

    /**
     * 时间戳转换成字符窜 * @param milSecond * @param pattern * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 将字符串转为时间戳 * @param dateString * @param pattern * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 是否是电话码号
     * @param input     输入文字
     * @return          是，否
     */
    public static boolean isPhoneNumber(String input) {
        String regex = "(1[0-9][0-9]|15[0-9]|18[0-9])\\d{8}";
        Pattern p = Pattern.compile(regex);
        return p.matches(regex, input);
    }

    /**
     * 将用户的证件号，中间部分转化成星号
     * @param Card                          证件号
     * @return                              转化后的证件号
     */
    public static String ReplaceUserCard(String Card)
    {
        String temp = "";
        if(TextUtils.isEmpty(Card))
        {
            temp = "******************";
        }
        else if(Card.length()==1)
        {
            temp = Card + "*****************";
        }else{
            temp = Card.substring(0,1) + "****************" + Card.substring(Card.length()-1,Card.length());
        }

        return temp;
    }

    /**
     * 将给出的钱数，截成，正数与小数部分
     * @param money                     传入的钱数
     * @param type                      是取整数部分，还是小数部分
     * @return                          返回值
     */
    public static String getMoney(String money,int type)
    {
        String temp = "";
        Pattern pattern = Pattern.compile("(\\d*.)(\\d*)");
        Matcher matcher = pattern.matcher(money);
        if(matcher.find()) {
            if(type==0) {
                temp = matcher.group(1);
            }else {
                temp = matcher.group(2);
            }
        }

        return temp;
    }

    /**
     * 验证输入的身份证号是否合法
     */
    public static boolean isLegalId(String id){
        if (id.toUpperCase().matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)")){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 显示小数点后两位
     *
     * @param num 传入值
     * @return 返回值
     */
    public static String FormatterNum(float num) {
        try {
            DecimalFormat mFormat = new DecimalFormat("#0.00");
            return mFormat.format(num);
        } catch (Exception ex) {
            return "0.00";
        }

    }





    private static Toast toast = null;
    /**
     * 显示Toast,添加了多次点击只显示一次的功能
     * @param ctx             上下文
     * @param msg             内容
     */
    public static void showToast(Context ctx, String msg) {

        if (toast == null) {
            toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();

    }


    private static final int CLICK_DELAY_TIME = 500;
    private static long lastClickTime;
    /**
     * 防止button快速连点
     *
     * @return            true连点   false不连点
     */
    public static boolean isNotFastClick() {
        boolean flag = false;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    /**
     * 判断权限
     * @param mActivity               所在acivity
     * @param mContext                上下文
     * @param warnText                提示语
     * @param permission              权限
     */
    public static void JudgePermission(Activity mActivity, final Context mContext, final String warnText, String... permission)
    {
        AndPermission.with(mActivity)
                .runtime()
                .permission(permission)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        String dirName = Environment.getExternalStorageDirectory().getPath() + "/sdcard/download/";
                        File file = new File(dirName);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {

                        //授权失败的提示
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setIcon(R.mipmap.ico_warning);
                        builder.setTitle("权限申请失败");
                        builder.setMessage(warnText);
                        builder.setPositiveButton("确定", null);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }).start();
    }

    /**
     * 判断权限是否开通
     * @param mContext              上下文
     * @param mActivity             调用的页面
     * @param permission            权限
     * @return                      true:开通      false:未开通
     */
    public static boolean checkGalleryPermission(Context mContext, Activity mActivity, String permission) {
        int ret = ActivityCompat.checkSelfPermission(mContext, permission);
        if (ret != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[] {permission},
                    1000);
            return false;
        }
        return true;
    }


    /**
     * 判断2个时间大小
     * yyyy-MM-dd  格式（自己可以修改成想要的时间格式）
     * @param startTime             开始时间
     * @param endTime               结束时间
     * @return
     */
    public static int timeCompare(String startTime, String endTime) {
        int i = 0;
        //注意：传过来的时间格式必须要和这里填入的时间格式相同
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = dateFormat.parse(startTime);//开始时间
            Date date2 = dateFormat.parse(endTime);//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime() < date1.getTime()) {
                //结束时间小于开始时间

                i = 1;
            } else if (date2.getTime() == date1.getTime()) {

                //开始时间与结束时间相同

                i = 2;
            } else if (date2.getTime() > date1.getTime()) {
                //结束时间大于开始时间
                i = 3;
            }
        } catch (Exception e) {

        }
        return i;
    }


    //bitmap转为base64
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //base64转为bitmap
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName=null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;


        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }


}
