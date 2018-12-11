package com.bacancy.eprodigy.Adapters.viewholder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bacancy.eprodigy.R;

public class ChatImageRecvHolder extends RecyclerView.ViewHolder{
    public ImageView img_incoming;
    public TextView tvTime;

    public ChatImageRecvHolder(View itemView) {
        super(itemView);
        img_incoming = (ImageView) itemView.findViewById(R.id.img_incoming);
        tvTime = itemView.findViewById(R.id.tv_time);
    }
}
