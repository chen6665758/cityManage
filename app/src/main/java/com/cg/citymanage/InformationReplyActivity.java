package com.cg.citymanage;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cg.citymanage.untils.myUntils;

import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.io.File;
import java.util.ArrayList;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

import static com.cg.citymanage.untils.CameraUntils.getImageAbsolutePath;

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

* 功能：信息回复页面
* 作者：cg
* 时间：2019/11/4 15:56
*/
public class InformationReplyActivity extends BaseActivity implements View.OnClickListener,BGASortableNinePhotoLayout.Delegate {

    /**
     * 标题栏
     */
    private ImageButton title_left_btn;
    private TextView title_textview;

    /**
     * 信息填写
     */
    private EditText txt_infoTitle;
    private TextView txt_receiverName;
    private EditText txt_infoContent;

    /**
     * 添加图片
     */
    private LinearLayout linear_pic;
    private BGASortableNinePhotoLayout mPhotosSnpl;
    private int RC_CHOOSE_PHOTO = 101;
    private int RC_PHOTO_PREVIEW = 102;

    /**
     * 添加视频
     */
    private LinearLayout linear_video;
    private String videoPath;
    private int VIDEO_CAPTURE = 103;
    private VideoPlayer video_player;
    private VideoPlayerController mController;
    private ImageView img_videoDel;
    private RelativeLayout rela_video;

    /**
     * 添加音频
     */
    private LinearLayout linear_voice;
    private int VOICE_CODE = 104;
    private String voicePath;
    private RelativeLayout rela_voice;
    private RelativeLayout rela_voiceplay;
    private ImageView img_play;
    private AnimationDrawable animationDrawable;
    private MediaPlayer mediaPlayer;
    private ImageView img_voiceDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        //权限的设置
        myUntils.JudgePermission(this,mContext,"您拒绝了相机功能，拍照等功能将无法使用！",Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);

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
        if(mController!=null)
        {
            mController.unRegisterNetChangedReceiver();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //从后台切换到前台，当视频暂停时或者缓冲暂停时，调用该方法重新开启视频播放
        VideoPlayerManager.instance().resumeVideoPlayer();
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
    public void setContentView() {
        setContentView(R.layout.activity_information_reply);
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
        title_textview.setText("信息");

        //信息填写
        txt_infoTitle = (EditText) findViewById(R.id.txt_infoTitle);
        txt_receiverName = (TextView)findViewById(R.id.txt_receiverName);
        txt_receiverName.setOnClickListener(this);
        txt_infoContent = (EditText)findViewById(R.id.txt_infoContent);

        //添加图片
        linear_pic = (LinearLayout)findViewById(R.id.linear_pic);
        linear_pic.setOnClickListener(this);
        mPhotosSnpl = (BGASortableNinePhotoLayout)findViewById(R.id.snpl_moment_add_photos);
        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);

        //添加视频
        linear_video = (LinearLayout)findViewById(R.id.linear_video);
        linear_video.setOnClickListener(this);
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

        img_videoDel = (ImageView)findViewById(R.id.img_videoDel);
        img_videoDel.setOnClickListener(this);
        rela_video = (RelativeLayout)findViewById(R.id.rela_video);

        //音频
        linear_voice = (LinearLayout)findViewById(R.id.linear_voice);
        linear_voice.setOnClickListener(this);
        rela_voice = (RelativeLayout)findViewById(R.id.rela_voice);
        rela_voiceplay = (RelativeLayout)findViewById(R.id.rela_voiceplay);
        rela_voiceplay.setOnClickListener(this);
        img_play = (ImageView)findViewById(R.id.img_play);
        img_voiceDel = (ImageView)findViewById(R.id.img_voiceDel);
        img_voiceDel.setOnClickListener(this);
    }

    @Override
    public void Jump_intent(Class<?> cla, Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按钮
            case R.id.title_left_btn:
                finish();
                break;
            //选择接收人
            case R.id.txt_receiverName:

                break;
            //图片添加
            case R.id.linear_pic:
                if(myUntils.checkGalleryPermission(mContext,InformationReplyActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                        myUntils.checkGalleryPermission(mContext,InformationReplyActivity.this,Manifest.permission.CAMERA) )
                {
                    if(mPhotosSnpl.getData().size() >= 3)
                    {
                        myUntils.showToast(mContext,"图片最多只能上传三张！");
                        return;
                    }else {
                        choicePhotoWrapper();
                    }
                }else{
                    //权限的设置
                    myUntils.JudgePermission(this,mContext,"您拒绝了相机功能，拍照等功能将无法使用！",Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
                }
                break;
            //视频添加
            case R.id.linear_video:
                if(myUntils.checkGalleryPermission(mContext,InformationReplyActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                        myUntils.checkGalleryPermission(mContext,InformationReplyActivity.this,Manifest.permission.CAMERA))
                {
                    Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,0);
                    //好使
                    intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,10485760L);
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10);
                    startActivityForResult(intent,VIDEO_CAPTURE);
                }else{
                    //权限的设置
                    myUntils.JudgePermission(this,mContext,"您拒绝了相机功能，拍照等功能将无法使用！",Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
                }
                break;
            //视频关闭按钮
            case R.id.img_videoDel:
                OkCancelFragmentDialog okcancel = OkCancelFragmentDialog.newInstance("您确定要删除这个视频吗！");
                okcancel.show(getSupportFragmentManager(),"视频删除");
                okcancel.setOnItemClickLitener(new OkCancelFragmentDialog.OnItemClickLitener() {
                    @Override
                    public void OnItemClick(View view, int positon) {
                        videoPath = "";
                        rela_video.setVisibility(View.GONE);
                    }
                });
                break;
            //添加音频
            case R.id.linear_voice:
                Intent intent = new Intent();
                intent.setClass(InformationReplyActivity.this,SoundRecordingActivity.class);
                startActivityForResult(intent,VOICE_CODE,bundle);
                break;
            //播放音频
            case R.id.rela_voiceplay:
                try {
                    playVoice(voicePath);
                }catch (Exception ex)
                {
                    Log.e("EventReport", "行数: 278  ex:" + ex.getMessage());
                }
                break;
            //删除音频
            case R.id.img_voiceDel:
                OkCancelFragmentDialog okcancel_v = OkCancelFragmentDialog.newInstance("您确定要删除这个音频吗！");
                okcancel_v.show(getSupportFragmentManager(),"音频删除");
                okcancel_v.setOnItemClickLitener(new OkCancelFragmentDialog.OnItemClickLitener() {
                    @Override
                    public void OnItemClick(View view, int positon) {
                        rela_voice.setVisibility(View.GONE);
                        voicePath = "";
                    }
                });

                break;
        }
    }

    /**
     * 点击添加图片
     */
    private void choicePhotoWrapper() {
        //String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        //if (EasyPermissions.hasPermissions(this, perms)) {
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");

        Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                .maxChooseCount(mPhotosSnpl.getMaxItemCount() - mPhotosSnpl.getItemCount()) // 图片选择张数的最大值
                .selectedPhotos(null) // 当前已选中的图片路径集合
                .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                .build();
        startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);

    }

    /**
     * 下方四个方法，是图片控件的方法，
     */
    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {

    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(this)
                .previewPhotos(models) // 当前预览的图片路径集合
                .selectedPhotos(models) // 当前已选中的图片路径集合
                .maxChooseCount(mPhotosSnpl.getMaxItemCount()) // 图片选择张数的最大值
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data));

            Log.e("EventReportActivity", "行数: 185  data:" + mPhotosSnpl.getData().toString());

        } else if (requestCode == RC_PHOTO_PREVIEW) {
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
        } else if (resultCode==RESULT_OK && requestCode==VIDEO_CAPTURE){
            Uri videoUri=data.getData();

            String mVideoPath = getImageAbsolutePath(InformationReplyActivity.this,videoUri);
            videoPath = mVideoPath;
            rela_video.setVisibility(View.VISIBLE);
            video_player.setUp(mVideoPath,null);
        } else if(resultCode==RESULT_OK && requestCode == VOICE_CODE)
        {
            voicePath = data.getStringExtra("sPath");
            if(!"".equals(voicePath))
            {
                rela_voice.setVisibility(View.VISIBLE);

            }else{
                myUntils.showToast(mContext,"没有采集到音频文件，请重新采集！");
                return;
            }
            Log.e("EventReportActivity", "行数: 332  voicePath:" + voicePath);
        }
    }
}
