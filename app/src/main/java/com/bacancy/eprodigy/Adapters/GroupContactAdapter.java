package com.bacancy.eprodigy.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bacancy.eprodigy.Activity.CreateGroupActivity;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;

import java.util.ArrayList;
import java.util.List;

public class GroupContactAdapter extends RecyclerView.Adapter<GroupContactAdapter.MyViewHolder>{

    Context mContext;
    List<ContactListResponse.ResponseDataBean> responseDataBeanList;
    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;
    CreateGroupAdapter createGroupAdapter;
    ArrayList<String> NameArrayList = new ArrayList<>();
      ArrayList<String> mCheckset = new ArrayList<>();



    public ArrayList<String> getCheckSetList()
    {
        return mCheckset;
    }public ArrayList<String> getNameList()
    {
        return NameArrayList;
    }
    public GroupContactAdapter(Context mcContext,List<ContactListResponse.ResponseDataBean> responseDataBeanList) {
        this.mContext = mcContext;
        this.responseDataBeanList = responseDataBeanList;
    }

    @Override
    public GroupContactAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_groupcontact, parent, false);

        return new GroupContactAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupContactAdapter.MyViewHolder holder, final int position) {

        holder.tv_name.setText(responseDataBeanList.get(position).getName());
        holder.tv_country.setText(responseDataBeanList.get(position).getPhone());
        holder.checkbox.setChecked(false);
        holder.checkbox.setTag(position);

        holder.checkbox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NameArrayList.add(holder.tv_name.getText().toString());
                mCheckset.add(responseDataBeanList.get(position).getUsername());
                createGroupAdapter = new CreateGroupAdapter(mContext,NameArrayList);

                if (CreateGroupActivity.rv_group!=null)
                CreateGroupActivity.rv_group.setAdapter(createGroupAdapter);
//                CheckBox cb = (CheckBox)v;
//                int clickedPos = (Integer) cb.getTag();
//
//                if(cb.isChecked())
//                {
//                    if(lastChecked != null)
//                    {
//                        lastChecked.setChecked(false);
//                    }
//
//                    lastChecked = cb;
//                    lastCheckedPos = clickedPos;
//
//                }
//                else
//                    lastChecked = null;
            }
        });

    }

    @Override
    public int getItemCount() {
        return responseDataBeanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_country;
        ImageView img_profile;
        private CheckBox checkbox;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            tv_country = (TextView)itemView.findViewById(R.id.tv_country);
            img_profile = (ImageView)itemView.findViewById(R.id.img_profile);
            checkbox = (CheckBox)itemView.findViewById(R.id.chk1);
        }
    }
}
