package com.bacancy.eprodigy.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bacancy.eprodigy.Activity.SingleChatActivity;
import com.bacancy.eprodigy.R;

import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {

    ArrayList<String> phoneNumberList;
    ArrayList<String> UserNameList;
    Activity activity;

    public ContactListAdapter(Activity activity,ArrayList<String> UserNameList,ArrayList<String> phoneNumberList) {
        this.activity = activity;
        this.UserNameList = UserNameList;
        this.phoneNumberList = phoneNumberList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_country;
        ImageView img_profile;
        LinearLayout ll_main;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tv_country = (TextView)view.findViewById(R.id.tv_country);
            img_profile = (ImageView)view.findViewById(R.id.img_profile);
            ll_main = (LinearLayout)view.findViewById(R.id.ll_main);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userslist_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.tv_name.setText(UserNameList.get(position));
        holder.tv_country.setText(phoneNumberList.get(position));
        holder.ll_main.setId(position);
        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
// TODO Add extras or a data URI to this intent as appropriate.
                resultIntent.putExtra("name",UserNameList.get(view.getId()));
                resultIntent.putExtra("phone",phoneNumberList.get(view.getId()));

                activity.setResult(Activity.RESULT_OK, resultIntent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return UserNameList.size();
    }

    public void filterList(ArrayList<String> filterdNames) {
        this.UserNameList = filterdNames;
        notifyDataSetChanged();
    }
}
