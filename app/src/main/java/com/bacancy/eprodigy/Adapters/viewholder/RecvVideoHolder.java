package com.bacancy.eprodigy.Adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bacancy.eprodigy.R;

public class RecvVideoHolder extends RecyclerView.ViewHolder{

    public ImageView img_incoming_video;
    public TextView tv_time;

    public RecvVideoHolder(View itemView) {
        super(itemView);
        img_incoming_video = (ImageView) itemView.findViewById(R.id.img_incoming_video);
        tv_time = (TextView) itemView.findViewById(R.id.tv_time);
    }
}
