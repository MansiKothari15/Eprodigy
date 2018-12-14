package com.bacancy.eprodigy.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bacancy.eprodigy.R;

public class CreateGroupAdapter extends RecyclerView.Adapter<CreateGroupAdapter.MyViewHolder>{


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_creategroup, parent, false);

        return new CreateGroupAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tv_name.setText("XYZ");
        holder.img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_close,img_profile;
        TextView tv_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_close = (ImageView)itemView.findViewById(R.id.img_close);
            img_profile = (ImageView)itemView.findViewById(R.id.img_profile);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
        }
    }
}
