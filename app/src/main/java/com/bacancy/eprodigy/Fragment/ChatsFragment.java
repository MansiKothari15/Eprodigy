package com.bacancy.eprodigy.Fragment;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.Activity.BaseActivity;
import com.bacancy.eprodigy.Activity.NewMessageActivity;
import com.bacancy.eprodigy.Adapters.ChatListAdapter;
import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.Models.ChatStateModel;
import com.bacancy.eprodigy.Models.GroupPojo;
import com.bacancy.eprodigy.Models.PresenceModel;
import com.bacancy.eprodigy.MyApplication;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.db.DataManager;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.xmpp.XmppCustomEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends BaseFragment {

    TextView tv_noChat;
    RecyclerView rv_chat;
    ChatListAdapter chatListAdapter;
    List<ChatPojo> recentChatlist = new ArrayList<ChatPojo>();
    List<String> chatUserList = new ArrayList<>();

    private List<ChatPojo> conversation_ArrayList = new ArrayList<>();

    EditText edt_search;
    ImageView img_clear;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setAction(view);

    }


    private void setAction(View view) {
        img_clear = (ImageView) view.findViewById(R.id.img_clear);
        edt_search = (EditText) view.findViewById(R.id.edt_search);
        tv_noChat = (TextView)view.findViewById(R.id.tv_noChat);

        rv_chat = (RecyclerView)view.findViewById(R.id.rv_chat);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_chat.setLayoutManager(mLayoutManager);
        rv_chat.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_chat.getContext(),
                new LinearLayoutManager(getActivity()).getOrientation());
        rv_chat.addItemDecoration(dividerItemDecoration);

//        userChatAdapter = new UserChatAdapter(getActivity(), new ArrayList<ContactListResponse.ResponseDataBean>());
        chatListAdapter = new ChatListAdapter(new ArrayList<ChatPojo>(),getActivity());
        rv_chat.setAdapter(chatListAdapter);
        chatListAdapter.notifyDataSetChanged();

        tv_noChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),NewMessageActivity.class);
                startActivity(i);
            }
        });
//adding a TextChangedListener
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                img_clear.setVisibility(!TextUtils.isEmpty(charSequence.toString()) ? View.VISIBLE : View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input

                if (chatListAdapter!=null) {
                    chatListAdapter.getFilter().filter(editable);
                }
            }
        });

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_search.setText("");
            }
        });

        LoadData();
    }

    private void LoadData() {

        List<String> chatList = DataManager.getInstance().getChatUserList();
        Log.d("chatList",chatList.toString());

        if (chatList != null && chatList.size() > 0)
            tv_noChat.setVisibility(View.INVISIBLE);

            DataManager.getInstance()
                    .getRecentChatUserListById(chatList)
                    .observe(getActivity(), new Observer<List<ChatPojo>>() {
                        @Override
                        public void onChanged(@Nullable List<ChatPojo> userList) {
                            Log.d("userList",userList.toString());

                            for (ChatPojo chatPojo:userList) {

                                Log.e("Sender=",">"+chatPojo.getChatSender());
                                Log.e("Recv=",">"+chatPojo.getChatRecv());
                                Log.e("grp id=",">"+chatPojo.getGroupId());
                                Log.e("grp name=",">"+chatPojo.getGroupName());
                                Log.e("grp image=",">"+chatPojo.getGroupImage());
                            }
//select grouptable where grpid=gid
                            if (chatListAdapter != null) {
                                chatListAdapter.swapItems(userList);
                                chatListAdapter.notifyDataSetChanged();
                            }

                        }
                    });


        List<String> groupIdList = DataManager.getInstance().getGroupIdList();

       /* DataManager.getInstance()
                .getRecentGroupUserListById(groupIdList)
                .observe(getActivity(), new Observer<List<ChatPojo>>() {
                    @Override
                    public void onChanged(@Nullable List<ChatPojo> groupList) {
                        Log.d("grpList",groupList.toString());

                    }
                });*/


        /*DataManager.getInstance()
                .getAllGroup()
                .observe(getActivity(), new Observer<List<GroupPojo>>() {
                    @Override
                    public void onChanged(@Nullable List<GroupPojo> groupPojoList) {
                        if (groupPojoList != null)
                            Log.d("groupPojoList", "" + groupPojoList.size());
                               *//* if (chatListAdapter != null) {
                                    chatListAdapter.swapItems(userList);
                                    chatListAdapter.notifyDataSetChanged();
                                }
*//*
                    }
                });*/

    }




}
