package com.bacancy.eprodigy.Adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bacancy.eprodigy.R;


/**
 * Created by samir on 22/2/18.
 */

public class HeaderHolder extends RecyclerView.ViewHolder {
    public TextView headerDate;

    public HeaderHolder(View itemView) {
        super(itemView);

        headerDate = itemView.findViewById(R.id.txtHeader);
    }
}
