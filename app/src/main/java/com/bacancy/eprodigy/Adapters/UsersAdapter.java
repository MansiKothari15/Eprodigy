package com.bacancy.eprodigy.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bacancy.eprodigy.Activity.BaseActivity;
import com.bacancy.eprodigy.Activity.SingleChatActivity;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;
import com.bacancy.eprodigy.callback.ActorDiffCallbackUser;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> implements Filterable{



    List<ContactListResponse.ResponseDataBean> mList;
    List<ContactListResponse.ResponseDataBean> mListFiltered;
    Activity activity;

    public UsersAdapter(Activity activity, List<ContactListResponse.ResponseDataBean> mList) {
        this.activity = activity;
        this.mList = mList;
        this.mListFiltered = mList;

    }

    public void swapItems(List<ContactListResponse.ResponseDataBean> actors) {
        final ActorDiffCallbackUser diffCallback = new ActorDiffCallbackUser(this.mListFiltered, actors);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mListFiltered.clear();
        this.mListFiltered.addAll(actors);
        diffResult.dispatchUpdatesTo(this);
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
                    List<ContactListResponse.ResponseDataBean> filteredList = new ArrayList<>();
                    for (ContactListResponse.ResponseDataBean row : mList) {

                        if (row.getName().toLowerCase().contains(charString.toLowerCase().trim())
                                || row.getPhone().contains(charString.toLowerCase().trim())) {
                            filteredList.add(row);
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
                mListFiltered = (List<ContactListResponse.ResponseDataBean>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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


    private long mLastClickTime = 0;

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.MyViewHolder holder, final int position) {

        final ContactListResponse.ResponseDataBean dataBean=mListFiltered.get(position);
        holder.tv_name.setText(dataBean.getName());
         holder.tv_country.setText(dataBean.getPhone());
        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // mis-clicking prevention, using threshold of 2000 ms
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();


                Intent i = new Intent(activity,SingleChatActivity.class);
                Bundle b = new Bundle();
                b.putString("name",dataBean.getName());
                b.putString("receiverJid",dataBean.getUsername());
                b.putString("isGroup", "false");
                i.putExtras(b);
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListFiltered.size();
    }


}
