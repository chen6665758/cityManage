package com.cg.citymanage.customs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cg.citymanage.R;

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

* 功能：等待显示框
* 作者：cg
* 时间：2019/9/20 9:31
*/
public class MyLoading extends Dialog {

	private Context mCcontext = null;
    private static MyLoading myProgressDialog = null;
    //private static Typeface youyuan;
    boolean isShowing = false;
    boolean isTimeout = false;
    public MyLoading(Context context){
        super(context);
        this.mCcontext = context;
    }

    public MyLoading(Context context, int theme) {
        super(context, theme);
        this.mCcontext = context;
    }
     
    public static MyLoading createDialog(Context context){
        myProgressDialog = new MyLoading(context, R.style.ChooseGenderCustomDialog);
        myProgressDialog.setContentView(R.layout.my_loading_dialog);
    	//透明
    	Window window = myProgressDialog.getWindow();
    	WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = 300; // 宽度
        lp.height = 300; // 高度
    	lp.alpha = 0.9f;
    	window.setAttributes(lp);
    	//myProgressDialog.show();
        /*youyuan = Typeface.createFromAsset(context.getAssets(),
                "fonts/youyuan.TTF");*/
        myProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        myProgressDialog.setCanceledOnTouchOutside(false);
		//myProgressDialog.setIcon(R.drawable.b01);
    	//myProgressDialog.setIndeterminate(false);
        myProgressDialog.setCancelable(true);
        return myProgressDialog;
    }
  
    public void onWindowFocusChanged(boolean hasFocus){
         
        if (myProgressDialog == null){
            return;
        }
        ImageView imageView = (ImageView) myProgressDialog.findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    @Override
    public void show() {
        super.show();
        isShowing = true;
        mHandler.postDelayed(timeOutTask,30000);//30秒超时
    }

    @Override
    public void dismiss() {

        try {
        super.dismiss();
        isShowing = false;
        if (mHandler != null && timeOutTask != null ){
            mHandler.removeCallbacks(timeOutTask);
        }
        }catch (Exception e){}
    }
    public Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == 1){

                try {
                    dismiss();
                }catch (Exception e){}

            }
        }
    };
    Runnable timeOutTask = new Runnable() {
        public void run() {
            isTimeout = true;
            Message msg = Message.obtain();
            msg.what = 1;
            mHandler.sendMessage(msg);
        }
    };
    /**
     *
     * [Summary]
     *       setTitile ����
     * @param strTitle
     * @return
     *
     */
    public MyLoading setTitile(String strTitle){
        return myProgressDialog;
    }


}
