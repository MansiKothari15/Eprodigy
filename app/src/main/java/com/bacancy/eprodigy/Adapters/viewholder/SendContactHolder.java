package com.bacancy.eprodigy.Adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bacancy.eprodigy.R;

public class SendContactHolder extends RecyclerView.ViewHolder {

    public ImageView img_contact_outgoing;
    public TextView tv_contact_name_outgoing,tv_phone_outgoing,tv_time_outgoing;
    public RelativeLayout rl_contact_outgoing;

    public SendContactHolder(View itemView) {
        super(itemView);
        rl_contact_outgoing = (RelativeLayout)itemView.findViewById(R.id.rl_contact_outgoing);
        img_contact_outgoing = (ImageView) itemView.findViewById(R.id.img_contact_outgoing);
        tv_contact_name_outgoing = (TextView) itemView.findViewById(R.id.tv_contact_name_outgoing);
        tv_time_outgoing = (TextView) itemView.findViewById(R.id.tv_time_outgoing);
        tv_phone_outgoing = (TextView) itemView.findViewById(R.id.tv_phone_outgoing);
    }
}
