package com.bacancy.eprodigy.Adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bacancy.eprodigy.R;

public class SendAudioHolder extends RecyclerView.ViewHolder{

    public ImageView img_play;

    public SendAudioHolder(View itemView) {
        super(itemView);
        img_play = (ImageView)itemView.findViewById(R.id.img_play);
    }
}
