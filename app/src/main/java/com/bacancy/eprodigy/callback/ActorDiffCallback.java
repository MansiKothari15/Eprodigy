package com.bacancy.eprodigy.callback;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;


import com.bacancy.eprodigy.Models.ChatPojo;

import java.util.List;

public class ActorDiffCallback extends DiffUtil.Callback {

    private final List<ChatPojo> oldList;
    private final List<ChatPojo> newList;

    public ActorDiffCallback(List<ChatPojo> oldList, List<ChatPojo> newList) {
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
        return oldList.get(oldItemPosition).getMsgId() == newList.get(newItemPosition).getMsgId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final ChatPojo oldItem = oldList.get(oldItemPosition);
        final ChatPojo newItem = newList.get(newItemPosition);

        return oldItem.getChatSender().equals(newItem.getChatSender());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
