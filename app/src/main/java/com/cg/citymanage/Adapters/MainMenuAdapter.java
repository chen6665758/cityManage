package com.cg.citymanage.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cg.citymanage.R;
import com.cg.citymanage.models.MainMenuModel;

import java.util.List;

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

* 功能：首页菜单的Adaptre
* 作者：cg
* 时间：2019/10/22 10:52
*/
public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.myViewHolder> {

    private Context mContext;
    private List<MainMenuModel> list_data;
    private LayoutInflater inflater;

    public MainMenuAdapter(Context mContext, List<MainMenuModel> list_data) {
        this.mContext = mContext;
        this.list_data = list_data;
        this.inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.activity_main_menu_item,viewGroup,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i) {
        Glide.with(mContext).load(getMenuPic(list_data.get(i).getMenuId())).into(myViewHolder.img_menuImg);
        myViewHolder.txt_menuName.setText(list_data.get(i).getMenuName());

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = myViewHolder.getLayoutPosition();
                    mOnItemClickLitener.OnItemClick(myViewHolder.itemView, pos);
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        ImageView img_menuImg;
        TextView txt_menuName;

        myViewHolder(View itemView) {
            super(itemView);

            img_menuImg = (ImageView) itemView.findViewById(R.id.img_menuImg);
            txt_menuName = (TextView)itemView.findViewById(R.id.txt_menuName);
        }
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

    private int getMenuPic(String menuId)
    {
        int picId = R.mipmap.ico_loginusertype;
        switch (menuId)
        {
            case "1":
                picId = R.mipmap.ico_maineventreport;
                break;
            case "13":
                picId = R.mipmap.ico_maineventall;
                break;
            case "14":
                picId = R.mipmap.ico_maineventtransmit;
                break;
            case "19":
                picId = R.mipmap.ico_maineventimpatient;
                break;
            case "2":
                picId = R.mipmap.ico_mainwillwork;
                break;
            case "3":
                picId = R.mipmap.ico_maineventwork;
                break;
            case "9":
                picId = R.mipmap.ico_maininformation;
                break;
        }

        return picId;
    }
}
