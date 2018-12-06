package com.bacancy.eprodigy.Adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bacancy.eprodigy.R;

public class SendContactHolder extends RecyclerView.ViewHolder {

    public ImageView img_contact_outgoing;
    public TextView tv_contactName,tv_phoneNo;
    public LinearLayout ll_contact;

    public SendContactHolder(View itemView) {
        super(itemView);
        ll_contact = (LinearLayout)itemView.findViewById(R.id.ll_contact);
        img_contact_outgoing = (ImageView) itemView.findViewById(R.id.img_contact_outgoing);
        tv_contactName = (TextView) itemView.findViewById(R.id.tv_contactName);
        tv_phoneNo = (TextView) itemView.findViewById(R.id.tv_phoneNo);
    }
}
