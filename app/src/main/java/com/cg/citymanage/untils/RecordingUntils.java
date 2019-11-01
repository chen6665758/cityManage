package com.cg.citymanage.untils;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.Toast;

import java.io.File;

public class RecordingUntils {

    public static void startSound(Context mContext, MediaRecorder mMediaRecorder, File mRecAudioFile, String strTempFile, File mRecAudioPath, long rectime, Chronometer tv_play)
    {
        // 录音
        try {
            /* ①Initial：实例化MediaRecorder对象 */
            //mMediaRecorder = new MediaRecorder();

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

            mMediaRecorder.setOutputFile(mRecAudioFile
                    .getAbsolutePath());
            /* ③准备 */
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
            /* 按钮状态 */

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            Toast.makeText(mContext, "录音权限暂未开通，请先开通再使用。", Toast.LENGTH_LONG).show();
            e1.printStackTrace();
        }
    }
}
