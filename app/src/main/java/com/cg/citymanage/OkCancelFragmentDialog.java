package com.cg.citymanage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

 * 功能：通用的确定，取消dialog
 * 作者：cg
 * 时间：2019/4/12 16:37
 */
public class OkCancelFragmentDialog extends DialogFragment {

    private Context mContext;
    private String info;                  //显示的提示语

    public static OkCancelFragmentDialog newInstance(String info) {

        OkCancelFragmentDialog b = new OkCancelFragmentDialog();
        Bundle args = new Bundle();
        args.putString("info",info);
        b.setArguments(args);
        return b;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        info = getArguments().getString("info");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_okcancel_dialog,container,false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView txt_info = (TextView)view.findViewById(R.id.txt_info);
        txt_info.setText(info);

        Button btn_patroldialog_cancel = (Button)view.findViewById(R.id.btn_patroldialog_cancel);
        btn_patroldialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button btn_patroldialog_ok = (Button)view.findViewById(R.id.btn_patroldialog_ok);
        btn_patroldialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.OnItemClick(v,0);
                dismiss();
            }
        });

    }

    /**
     * 定义点击每项的接口
     */
    public interface OnItemClickLitener
    {
        void OnItemClick(View view, int positon);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
