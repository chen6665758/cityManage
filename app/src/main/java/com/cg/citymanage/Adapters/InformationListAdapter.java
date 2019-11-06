package com.cg.citymanage.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cg.citymanage.R;
import com.cg.citymanage.models.InformationListModel;

import java.util.List;

public class InformationListAdapter extends RecyclerView.Adapter<InformationListAdapter.myViewHolder> {

    private Context mContext;
    private List<InformationListModel> list_data;
    private LayoutInflater inflater;

    public InformationListAdapter(Context mContext, List<InformationListModel> list_data) {
        this.mContext = mContext;
        this.list_data = list_data;
        this.inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.activity_information_listitem,viewGroup,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i) {

        myViewHolder.txt_infoTitle.setText(list_data.get(i).getInfoTitle());
        myViewHolder.txt_infoTime.setText(list_data.get(i).getInfoTime());

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

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView txt_infoTitle;
        TextView txt_infoTime;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_infoTitle = (TextView)itemView.findViewById(R.id.txt_infoTitle);
            txt_infoTime = (TextView)itemView.findViewById(R.id.txt_infoTime);
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
}
