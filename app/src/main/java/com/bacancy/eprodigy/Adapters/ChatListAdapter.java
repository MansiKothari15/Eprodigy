package com.bacancy.eprodigy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.Activity.SingleChatActivity;
import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;
import com.bacancy.eprodigy.callback.ActorDiffCallbackChat;
import com.bacancy.eprodigy.db.DataManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> implements Filterable {

    private List<ChatPojo> mList = new ArrayList<>();
    List<ChatPojo> mListFiltered = new ArrayList<>();
    Context mContext;


    public ChatListAdapter(ArrayList<ChatPojo> chatUserList, Context mContext) {
        this.mList = chatUserList;
        this.mListFiltered = chatUserList;
        this.mContext = mContext;
    }

    @Override
    public ChatListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatlist_row, parent, false);

        return new ChatListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListAdapter.MyViewHolder holder, final int position) {

        final ChatPojo bean = mList.get(position);


        if (bean != null && bean.getMsgMode() != null && bean.getMsgMode().equalsIgnoreCase(AppConfing.GROUP_CHAT_MSG_MODE)
                && bean.getChatText().equals(AppConfing.GROUP_GREETINGS)) {

            if (!TextUtils.isEmpty(bean.getGroupId()))
                holder.rv_main.setTag(bean.getGroupId());

            holder.tv_id.setText(bean.getGroupName());
            holder.tv_text.setText("");
            Glide.with(mContext).load(bean.getGroupImage())
                    .apply(RequestOptions.circleCropTransform()).into(holder.img_pic);

            holder.rv_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(mContext, SingleChatActivity.class);
                    Bundle b = new Bundle();
                    b.putString("name", holder.tv_id.getText().toString());
                    b.putString("receiverJid", view.getTag().toString());
                    b.putString("isGroup", "true");
                    i.putExtras(b);
                    mContext.startActivity(i);
                }
            });

        } else {
            final ContactListResponse.ResponseDataBean singleUser = DataManager.getInstance().getUser(bean.getChatId());

            if (singleUser != null) {
                if (!TextUtils.isEmpty(singleUser.getUsername()))
                    holder.rv_main.setTag(singleUser.getUsername());
                if (singleUser != null) {
                    holder.tv_id.setText(singleUser.getName());
                    holder.tv_text.setText(singleUser.getPhone());
                    Glide.with(mContext).load(singleUser.getProfilepicture())
                            .apply(RequestOptions.circleCropTransform()).into(holder.img_pic);
                }
            }

            holder.rv_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (TextUtils.isEmpty(view.getTag().toString())) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent i = new Intent(mContext, SingleChatActivity.class);
                    Bundle b = new Bundle();
                    b.putString("name", holder.tv_id.getText().toString());

                    b.putString("receiverJid", view.getTag().toString());
                    b.putString("isGroup", "false");
                    i.putExtras(b);
                    mContext.startActivity(i);
                }
            });

        }

    }

    public void swapItems(List<ChatPojo> actors) {
        final ActorDiffCallbackChat diffCallback = new ActorDiffCallbackChat(this.mList, actors);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mList.clear();
        this.mList.addAll(actors);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return mListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mListFiltered = mList;
                } else {
                    List<ChatPojo> filteredList = new ArrayList<>();
                    for (ChatPojo str : mList) {

                        if (str.getChatText().toLowerCase().contains(charString.toLowerCase().trim())) {
                            filteredList.add(str);
                        }
                    }

                    mListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                mListFiltered = (List<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_id, tv_text, tv_time;
        RelativeLayout rv_main;
        ImageView img_pic;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_id = (TextView) itemView.findViewById(R.id.tv_id);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            rv_main = (RelativeLayout) itemView.findViewById(R.id.rv_main);
            img_pic = (ImageView) itemView.findViewById(R.id.img_pic);
        }
    }
}
