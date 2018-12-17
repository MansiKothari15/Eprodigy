package com.bacancy.eprodigy.Adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bacancy.eprodigy.R;

public class RecvAudioHolder extends RecyclerView.ViewHolder{

    public ImageView img_play,img_pause;

    public RecvAudioHolder(View itemView) {
        super(itemView);
        img_play = (ImageView)itemView.findViewById(R.id.img_play);
        img_pause = (ImageView)itemView.findViewById(R.id.img_pause);
    }
}
