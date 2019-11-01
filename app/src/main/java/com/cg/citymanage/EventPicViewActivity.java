package com.cg.citymanage;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.io.File;
import java.util.ArrayList;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

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

* 功能：事件详情中显示图片，视频、音频的页面
* 作者：cg
* 时间：2019/10/24 15:00
*/
public class EventPicViewActivity extends BaseActivity implements View.OnClickListener,BGASortableNinePhotoLayout.Delegate {

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 图片
     */
    private BGASortableNinePhotoLayout snpl_moment_add_photos;
    private int RC_PHOTO_PREVIEW = 102;

    /**
     * 视频
     */
    private VideoPlayer video_player;
    private VideoPlayerController mController;

    /**
     * 音频
     */
    private RelativeLayout rela_voiceplay;
    private ImageView img_play;
    private AnimationDrawable animationDrawable;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        initControls();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //从前台切到后台，当视频正在播放或者正在缓冲时，调用该方法暂停视频
        VideoPlayerManager.instance().suspendVideoPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁页面，释放，内部的播放器被释放掉，同时如果在全屏、小窗口模式下都会退出
        VideoPlayerManager.instance().releaseVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        //处理返回键逻辑；如果是全屏，则退出全屏；如果是小窗口，则退出小窗口
        if (VideoPlayerManager.instance().onBackPressed()){
            return;
        }else {
            //销毁页面
            VideoPlayerManager.instance().releaseVideoPlayer();
        }
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //从后台切换到前台，当视频暂停时或者缓冲暂停时，调用该方法重新开启视频播放
        VideoPlayerManager.instance().resumeVideoPlayer();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_event_pic_view);
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
        title_textview.setText("事件显示");

        //图片
        snpl_moment_add_photos = (BGASortableNinePhotoLayout)findViewById(R.id.snpl_moment_add_photos);
        snpl_moment_add_photos.setDelegate(this);
        snpl_moment_add_photos.addLastItem("http://img.taopic.com/uploads/allimg/140326/235113-1403260QA519.jpg");
        snpl_moment_add_photos.addLastItem("http://img.taopic.com/uploads/allimg/140326/235113-1403260I33562.jpg");

        //视频
        video_player = (VideoPlayer)findViewById(R.id.video_player);
        //设置播放类型
        // IjkPlayer or MediaPlayer
        video_player.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_IJK);
        video_player.continueFromLastPosition(false);
        video_player.setBackgroundColor("#000000");
        video_player.setUp("/storage/emulated/0/DCIM/Camera/VID_20191023_103406.3gp",null);
        //创建视频控制器
        mController = new VideoPlayerController(this);
        //设置视频控制器
        video_player.setController(mController);

        //音频
        rela_voiceplay = (RelativeLayout)findViewById(R.id.rela_voiceplay);
        rela_voiceplay.setOnClickListener(this);
        img_play = (ImageView)findViewById(R.id.img_play);

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
            //音频播放
            case R.id.rela_voiceplay:
                try {
                    playVoice("/storage/emulated/0/recaudio_166718318.wav");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {

    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {

    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(this)
                .previewPhotos(models) // 当前预览的图片路径集合
                .selectedPhotos(models) // 当前已选中的图片路径集合
                .maxChooseCount(snpl_moment_add_photos.getMaxItemCount()) // 图片选择张数的最大值
                .currentPosition(position) // 当前预览图片的索引
                .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                .build();
        startActivityForResult(photoPickerPreviewIntent, RC_PHOTO_PREVIEW);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {

    }


    private void playVoice(String path) throws Exception {
        if ("".equals(path)) {
            Toast.makeText(mContext, "路径不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(path);
        if (file.exists()) {


            animationDrawable = (AnimationDrawable) img_play.getBackground();
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
}
