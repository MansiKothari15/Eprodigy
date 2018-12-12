package com.bacancy.eprodigy.Adapters.viewholder;

 
 
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.customMapView.GoogleMapView;

public class LocationToHolder extends RecyclerView.ViewHolder {
    public RelativeLayout rl_location_incoming;
    public ImageView img_contact_incoming;
    public TextView tv_title_location_incoming, tv_desc_location_incoming, tv_time_location_incoming;

    public LocationToHolder(View itemView) {
        super(itemView);
        rl_location_incoming = itemView.findViewById(R.id.rl_location_incoming);
        img_contact_incoming = itemView.findViewById(R.id.img_contact_incoming);
        tv_title_location_incoming = itemView.findViewById(R.id.tv_title_location_incoming);
        tv_desc_location_incoming = itemView.findViewById(R.id.tv_desc_location_incoming);
        tv_time_location_incoming = itemView.findViewById(R.id.tv_time_location_incoming);
    }
}
