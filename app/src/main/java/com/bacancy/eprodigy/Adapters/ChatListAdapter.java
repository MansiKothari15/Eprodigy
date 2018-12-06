package com.bacancy.eprodigy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bacancy.eprodigy.Activity.SingleChatActivity;
import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.R;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder>{

    private List<String> chatUserList = new ArrayList<>();
    Context mContext;
    private List<ChatPojo> conversation_ArrayList = new ArrayList<>();

    public ChatListAdapter(List<String> chatUserList, Context mContext) {
        this.chatUserList = chatUserList;
        this.mContext = mContext;
    }

    @Override
    public ChatListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatlist_row, parent, false);

        return new ChatListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.MyViewHolder holder, final int position) {

        holder.tv_id.setText(chatUserList.get(position));
        holder.rv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext,SingleChatActivity.class);
                Bundle b = new Bundle();
                b.putString("name",chatUserList.get(position));
                i.putExtras(b);
                mContext.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return chatUserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_id,tv_text,tv_time;
        RelativeLayout rv_main;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_id = (TextView)itemView.findViewById(R.id.tv_id);
            tv_text = (TextView)itemView.findViewById(R.id.tv_text);
            tv_time = (TextView)itemView.findViewById(R.id.tv_time);
            rv_main = (RelativeLayout)itemView.findViewById(R.id.rv_main);
        }
    }
}
