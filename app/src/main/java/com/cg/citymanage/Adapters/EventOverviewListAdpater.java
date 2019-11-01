package com.cg.citymanage.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cg.citymanage.R;
import com.cg.citymanage.models.EventListModel;

import java.util.List;


public class EventOverviewListAdpater extends RecyclerView.Adapter<EventOverviewListAdpater.myViewHolder> {

    private List<EventListModel> list_data;
    private Context mContext;
    private LayoutInflater inflater;

    public EventOverviewListAdpater(List<EventListModel> list_data, Context mContext) {
        this.list_data = list_data;
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.activity_eventoverview_listitem,viewGroup,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i) {

        myViewHolder.txt_eventName.setText(list_data.get(i).getEventName());
        myViewHolder.txt_eventTime.setText(list_data.get(i).getEventTime());
        myViewHolder.txt_eventLink.setText(list_data.get(i).getEventLink());
        myViewHolder.txt_eventInfo.setText(list_data.get(i).getEventInfo());

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

        TextView txt_eventName;
        TextView txt_eventTime;
        TextView txt_eventLink;
        TextView txt_eventInfo;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_eventName = (TextView)itemView.findViewById(R.id.txt_eventName);
            txt_eventTime = (TextView)itemView.findViewById(R.id.txt_eventTime);
            txt_eventLink = (TextView)itemView.findViewById(R.id.txt_eventLink);
            txt_eventInfo = (TextView)itemView.findViewById(R.id.txt_eventInfo);
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
