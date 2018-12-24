package com.bacancy.eprodigy.callback;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;

import java.util.List;

public class ActorDiffCallbackUser extends DiffUtil.Callback {

    private final List<ContactListResponse.ResponseDataBean> oldList;
    private final List<ContactListResponse.ResponseDataBean> newList;

    public ActorDiffCallbackUser(List<ContactListResponse.ResponseDataBean> oldList, List<ContactListResponse.ResponseDataBean> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getUsername() == newList.get(newItemPosition).getUsername();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final ContactListResponse.ResponseDataBean oldItem = oldList.get(oldItemPosition);
        final ContactListResponse.ResponseDataBean newItem = newList.get(newItemPosition);

        return oldItem.getUsername().equals(newItem.getUsername());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}