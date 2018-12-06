package com.bacancy.eprodigy.Adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bacancy.eprodigy.R;


/**
 * Created by samir on 22/2/18.
 */

public class ChatHolderTo extends RecyclerView.ViewHolder {
    public TextView tvMessage,tvTime;

    public ChatHolderTo(View itemView) {
        super(itemView);
        tvMessage = itemView.findViewById(R.id.tv_message);
        tvTime = itemView.findViewById(R.id.tv_time);

    }
}
