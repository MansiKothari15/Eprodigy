package com.bacancy.eprodigy.Adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bacancy.eprodigy.R;

public class SendVideoHolder extends RecyclerView.ViewHolder{

    public ImageView img_outgoing_video;
    public TextView tv_time;

    public SendVideoHolder(View itemView) {
        super(itemView);
        img_outgoing_video = (ImageView) itemView.findViewById(R.id.img_outgoing_video);
        tv_time = (TextView) itemView.findViewById(R.id.tv_time);
    }
}
