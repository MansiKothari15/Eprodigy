package com.bacancy.eprodigy.Adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bacancy.eprodigy.R;

public class ChatImageHolder extends RecyclerView.ViewHolder{

    public ImageView img_outgoing;

    public ChatImageHolder(View itemView) {
        super(itemView);
        img_outgoing = (ImageView) itemView.findViewById(R.id.img_outgoing);
    }
}
