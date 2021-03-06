package com.bacancy.eprodigy.Adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bacancy.eprodigy.R;

public class SendAudioHolder extends RecyclerView.ViewHolder{

    public ImageView img_play,img_pause;
    public TextView tv_time_outgoing;

    public SendAudioHolder(View itemView) {
        super(itemView);
        img_play = (ImageView)itemView.findViewById(R.id.img_play);
        img_pause = (ImageView)itemView.findViewById(R.id.img_pause);
        tv_time_outgoing = (TextView)itemView.findViewById(R.id.tv_time_outgoing);
    }
}
