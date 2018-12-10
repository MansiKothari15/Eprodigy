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
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {



    List<ContactListResponse.ResponseDataBean> UserNameList;
    Activity activity;

    public UsersAdapter(Activity activity, List<ContactListResponse.ResponseDataBean> UserNameList) {
        this.activity = activity;
        this.UserNameList = UserNameList;

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
    public UsersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userslist_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.MyViewHolder holder, final int position) {

        final ContactListResponse.ResponseDataBean dataBean=UserNameList.get(position);
        holder.tv_name.setText(dataBean.getName());
         holder.tv_country.setText(dataBean.getPhone());
        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(activity,SingleChatActivity.class);
                Bundle b = new Bundle();
                b.putString("name",dataBean.getName());
                b.putString("receiverJid",dataBean.getUsername());
                i.putExtras(b);
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return UserNameList.size();
    }

    public void filterList(List<ContactListResponse.ResponseDataBean> filterdNames) {
        this.UserNameList = filterdNames;
        notifyDataSetChanged();
    }
}
