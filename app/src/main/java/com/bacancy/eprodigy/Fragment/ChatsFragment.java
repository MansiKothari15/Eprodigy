package com.bacancy.eprodigy.Fragment;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bacancy.eprodigy.Activity.NewMessageActivity;
import com.bacancy.eprodigy.Adapters.ChatListAdapter;
import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.db.DataManager;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    TextView tv_noChat;
    RecyclerView rv_chat;
    ChatListAdapter chatListAdapter;
    ArrayList<ChatPojo> chatPojoArrayList = new ArrayList<ChatPojo>();
    List<String> chatUserList = new ArrayList<>();
    String ChatUserId;
    private List<ChatPojo> conversation_ArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_noChat = (TextView)view.findViewById(R.id.tv_noChat);
        tv_noChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),NewMessageActivity.class);
                startActivity(i);
            }
        });

        rv_chat = (RecyclerView)view.findViewById(R.id.rv_chat);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_chat.setLayoutManager(mLayoutManager);
        rv_chat.setItemAnimator(new DefaultItemAnimator());
        LoadData();
    }

    private void LoadData() {
        chatUserList = DataManager.getInstance().getChatUserList();
        Log.d("chatUserList--",chatUserList.toString());
        if(chatUserList.size() != 0){
            tv_noChat.setVisibility(View.INVISIBLE);
        }

        DataManager.getInstance().getAll(ChatUserId).observe(getActivity(), new Observer<List<ChatPojo>>() {
            @Override
            public void onChanged(@Nullable List<ChatPojo> chatPojos) {
                conversation_ArrayList = chatPojos;
            }
        });

        chatListAdapter = new ChatListAdapter(chatUserList,getActivity());
        rv_chat.setAdapter(chatListAdapter);

    }

}
