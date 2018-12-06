package com.bacancy.eprodigy.Adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bacancy.eprodigy.R;


public class RecvContactHolder extends RecyclerView.ViewHolder {

    public ImageView img_contact_incoming;
    public TextView tv_contact_name_incoming, tv_phone_incoming, tv_time_incoming;
    public RelativeLayout rl_contact_incoming;

    public RecvContactHolder(View itemView) {
        super(itemView);
        rl_contact_incoming = (RelativeLayout) itemView.findViewById(R.id.rl_contact_incoming);
        img_contact_incoming = (ImageView) itemView.findViewById(R.id.img_contact_incoming);
        tv_contact_name_incoming = (TextView) itemView.findViewById(R.id.tv_contact_name_incoming);
        tv_time_incoming = (TextView) itemView.findViewById(R.id.tv_time_incoming);
        tv_phone_incoming = (TextView) itemView.findViewById(R.id.tv_phone_incoming);
    }
}
