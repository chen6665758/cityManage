package com.cg.citymanage;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.citymanage.untils.PermissionUntil;
import com.cg.citymanage.untils.RecordingUntils;
import com.cg.citymanage.untils.myUntils;

import java.io.File;

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

* 功能：录音功能
* 作者：cg
* 时间：2019/10/17 9:08
*/
public class SoundRecordingActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    private Button btn_soundstart;
    private Button btn_backSound;

    private File mRecAudioFile; // 录制的音频文件
    private File mRecAudioPath; // 录制的音频文件路徑
    private String mRecAudioNmae; // 录制的音频文件名
    private MediaRecorder mMediaRecorder;// MediaRecorder对象
    private String strTempFile = "recaudio_";// 临时文件的前缀

    private Chronometer tv_play;
    private long rectime = 0;

    MediaPlayer mediaPlayer;

    private int playstatus;   //录音状态  0:停止，1：开始

    private ImageView iv_play;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PermissionUntil.JudgePermission(this,mContext,"您拒绝了录音功能的开启，无法进行录音",
                Manifest.permission.RECORD_AUDIO);

        playstatus = 0;
        mRecAudioPath = Environment.getExternalStorageDirectory();// 得到SD卡得路径

        initControls();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_sound_recording);
    }


    private void initControls()
    {

        //标题栏
        title_left_btn = (ImageButton)findViewById(R.id.title_left_btn);
        title_left_btn.setOnClickListener(this);
        title_textview = (TextView) findViewById(R.id.title_textview);
        title_textview.setText("录制音频");

        btn_soundstart = (Button)findViewById(R.id.btn_soundstart);
        btn_soundstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(myUntils.checkGalleryPermission(mContext, SoundRecordingActivity.this, Manifest.permission.RECORD_AUDIO)) {
                    if (playstatus == 0) {
                        btn_soundstart.setText("停止录音");
                        btn_backSound.setVisibility(View.GONE);
                        playstatus = 1;
                        //startSound();
                        if (mMediaRecorder == null) {
                            mMediaRecorder = new MediaRecorder();
                            //mMediaRecorder.setOnErrorListener(this);
                        } else {
                            mMediaRecorder.reset();
                        }
                        try {
                            mRecAudioFile = File.createTempFile(strTempFile,
                                    ".wav", mRecAudioPath);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //计时器
                        if (tv_play != null) {
                            rectime = SystemClock.elapsedRealtime();
                            tv_play.setBase(rectime);
                            tv_play.start();
                        }
                        RecordingUntils.startSound(mContext, mMediaRecorder, mRecAudioFile, strTempFile, mRecAudioPath, rectime, tv_play);
                        animationDrawable = (AnimationDrawable) iv_play.getBackground();
                        animationDrawable.stop();
                        animationDrawable.start();
                    } else {
                        btn_soundstart.setText("开始录音");
                        btn_backSound.setVisibility(View.VISIBLE);
                        stopSound();
                    }
                }else{
                    myUntils.showToast(mContext,"对不起，请开通录音功能！");
                    PermissionUntil.JudgePermission(SoundRecordingActivity.this,mContext,"您拒绝了录音功能的开启，无法进行录音",
                            Manifest.permission.RECORD_AUDIO);
                }
            }
        });



        btn_backSound = (Button)findViewById(R.id.btn_backSound);
        btn_backSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String backURL = mRecAudioPath.getAbsolutePath()+ File.separator + mRecAudioNmae;
                Intent intent = new Intent(SoundRecordingActivity.this, EventReportActivity.class);
                intent.putExtra("sPath", backURL);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        tv_play = (Chronometer)findViewById(R.id.tv_play);

        iv_play = (ImageView)findViewById(R.id.iv_play);
        iv_play.setBackgroundResource(R.drawable.animation_speak);
    }

    /**
     * 开始录音
     */
    private void startSound()
    {
        // 录音
        try {
            /* ①Initial：实例化MediaRecorder对象 */
            //mMediaRecorder = new MediaRecorder();
            if (mMediaRecorder == null) {
                mMediaRecorder = new MediaRecorder();
                //mMediaRecorder.setOnErrorListener(this);
            } else {
                mMediaRecorder.reset();
            }
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder
                    .setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default
             * THREE_GPP(
             * 3gp格式，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB
             * )
             */
            mMediaRecorder
                    .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default */
            mMediaRecorder
                    .setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            /* ②设置输出文件的路径 */
            try {
                mRecAudioFile = File.createTempFile(strTempFile,
                        ".wav", mRecAudioPath);

            } catch (Exception e) {
                e.printStackTrace();
            }
            mMediaRecorder.setOutputFile(mRecAudioFile
                    .getAbsolutePath());
            /* ③准备 */
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
            /* 按钮状态 */

            System.out.println("长按。。。。。。。。。。。");

            //计时器
            rectime = SystemClock.elapsedRealtime();
            tv_play.setBase(rectime);
            tv_play.start();


        } catch (Exception e1) {
            // TODO Auto-generated catch block
            Toast.makeText(mContext, "录音权限暂未开通，请先开通再使用。", Toast.LENGTH_LONG).show();
            e1.printStackTrace();
        }
    }

    /**
     * 停止录音
     */
    private void stopSound()
    {
        if (mRecAudioFile != null) {
            //设置后不会崩
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setOnInfoListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            /* ⑤停止录音 */
            mMediaRecorder.reset();
            /* 录音文件 */
            mRecAudioNmae = mRecAudioFile.getName();
            /* ⑥释放MediaRecorder */
            mMediaRecorder.release();
            mMediaRecorder = null;
            /* 按钮状态 */
            playstatus = 0;
            System.out.println("抬起。。。。。。。。。。。。。。。。OnTouch");

            //计时器
            String time_temp = ""+(SystemClock.elapsedRealtime()- tv_play.getBase());
            tv_play.setText(time_temp.substring(0,2) + "'");
            //tv_play.setText(tv_play.getText().toString().split(":")[1] + "'");
            //tv_play.setBase(SystemClock.elapsedRealtime());
            tv_play.stop();

            if(animationDrawable!=null)
            {
                animationDrawable.stop();
            }

        }
    }


    private void play(String path) throws Exception {
        if ("".equals(path)) {
            Toast.makeText(mContext, "路径不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(path);
        if (file.exists()) {

            animationDrawable = (AnimationDrawable) iv_play.getBackground();
            animationDrawable.stop();
            animationDrawable.start();

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(path);
            // mediaPlayer.prepare(); // c/c++ 播放器引擎的初始化
            // 同步方法
            // 采用异步的方式
            mediaPlayer.prepareAsync();
            // 为播放器注册
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mp.start();
//                    iv_play.setBackgroundResource(R.drawable.animation_speak);
//                    iv_call_del.setVisibility(View.INVISIBLE);
//                    animationDrawable = (AnimationDrawable) iv_play.getBackground();
//                    animationDrawable.stop();
//                    animationDrawable.start();
//                    type = 3;

                }
            });

            // 注册播放完毕后的监听事件
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    if(mp!=null) {
                        if(mp.isPlaying())
                            mp.stop();
                        mp.reset();
                        mp.release();
                        mp=null;

                        if(animationDrawable!=null)
                        {
                            animationDrawable.stop();
                        }
                    }
//                    if(animationDrawable!=null)
//                    {
//                        animationDrawable.stop();
//                    }
//
//                    iv_play.setBackgroundResource(R.mipmap.ico_playsound3);
//                    iv_call_del.setVisibility(View.VISIBLE);
//                    type = 2;

                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    mediaPlayer.reset();
                    return false;
                }
            });

        } else {
            Toast.makeText(mContext, "文件不存在", Toast.LENGTH_LONG).show();
            return;
        }

    }

    @Override
    public void Jump_intent(Class<?> cla, Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            //返回按钮
            case R.id.title_left_btn:
                finish();
                break;
        }
    }
}
