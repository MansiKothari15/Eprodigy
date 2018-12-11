package com.bacancy.eprodigy.Adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bacancy.eprodigy.R;


public class LocationFromHolder extends RecyclerView.ViewHolder {
    public RelativeLayout rl_location_outgoing;
    public ImageView img_contact_outgoing;
    public TextView tv_title_location_outgoing, tv_desc_location_outgoing, tv_time_location_outgoing;

    public LocationFromHolder(View itemView) {
        super(itemView);
        rl_location_outgoing = itemView.findViewById(R.id.rl_location_outgoing);
        img_contact_outgoing = itemView.findViewById(R.id.img_contact_outgoing);
        tv_title_location_outgoing = itemView.findViewById(R.id.tv_title_location_outgoing);
        tv_desc_location_outgoing = itemView.findViewById(R.id.tv_desc_location_outgoing);
        tv_time_location_outgoing = itemView.findViewById(R.id.tv_time_location_outgoing);
    }
}