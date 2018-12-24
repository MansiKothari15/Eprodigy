package com.bacancy.eprodigy.Fragment;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.Toast;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.Activity.BaseActivity;
import com.bacancy.eprodigy.Activity.MobileVerificationActivity;
import com.bacancy.eprodigy.Adapters.ChatAdapter;
import com.bacancy.eprodigy.Adapters.UsersAdapter;
import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.Models.ChatStateModel;
import com.bacancy.eprodigy.Models.PresenceModel;
import com.bacancy.eprodigy.MyApplication;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;
import com.bacancy.eprodigy.db.DataManager;
import com.bacancy.eprodigy.interfaces.MyContactListener;
import com.bacancy.eprodigy.permission.PermissionListener;
import com.bacancy.eprodigy.tasks.GetMyContactTask;
import com.bacancy.eprodigy.utils.AlertUtils;
import com.bacancy.eprodigy.utils.Constants;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.utils.Pref;
import com.bacancy.eprodigy.xmpp.XMPPHandler;
import com.bacancy.eprodigy.xmpp.XmppCustomEventListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersFragment extends Fragment implements MyContactListener, PermissionListener {

    private String TAG = "UsersFragment";
    PermissionListener permissionListenerIntr;

    UsersAdapter usersAdapter;

    RecyclerView rv_users;
    SwipeRefreshLayout swipe_refresh_data;
    EditText edt_search;
    ImageView img_clear;


    List<ContactListResponse.ResponseDataBean> responseDataBeanList = new ArrayList<>();



    public XmppCustomEventListener xmppCustomEventListener = new XmppCustomEventListener() {

        //Event Listeners
        public void onNewMessageReceived(ChatPojo chatPojo) {

            Log.e("ad", "onNewMessageReceived>>" + chatPojo.toString());

            chatPojo.setShowing(true);
            chatPojo.setMine(false);
            DataManager.getInstance().AddChat(chatPojo);

            LogM.e("onNewMessageReceived ChatActivity");

            // if (ifshow) BaseActivity.SendNotification(SingleChatActivity.this, chatPojo);

        }

        @Override
        public void onPresenceChanged(PresenceModel presenceModel) {
//            final String presence = com.coinasonchatapp.app.utils.Utils.getStatusMode(presenceModel.getUserStatus());
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    txtUserStatus.setText(presence);
//                    LogM.e("onPresenceChanged" + presence);
//                }
//            });
        }


        //On Chat Status Changed
        public void onChatStateChanged(ChatStateModel chatStateModel) {

//            String chatStatus = com.coinasonchatapp.app.utils.Utils.getChatMode(chatStateModel.getChatState());
            LogM.e("chatStatus --- onChatStateChanged");
            if (MyApplication.getmService().xmpp.checkSender(((BaseActivity)getActivity()).username, chatStateModel.getUser())) {
                //  chatStatusTv.setText(chatStatus);
                LogM.e("onChatStateChanged");
            }
        }

        @Override
        public void onConnected() {


            ((BaseActivity)getActivity()).xmppHandler = MyApplication.getmService().xmpp;
            ((BaseActivity)getActivity()).xmppHandler.setUserPassword(((BaseActivity)getActivity()).username, ((BaseActivity)getActivity()).password);
            //((BaseActivity)getActivity()).xmppHandler.login();
            new XMPPHandler.LoginTask(getActivity(),((BaseActivity)getActivity()).password,((BaseActivity)getActivity()).username);
        }

        public void onLoginFailed() {
            ((BaseActivity)getActivity()).xmppHandler.disconnect();
            Toast.makeText(getActivity(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionListenerIntr = this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users, container, false);
    }
    @Override
    public void onResume() {
        super.onResume();

        //Here we bind our event listener (XmppCustomEventListener)
        ((BaseActivity)getActivity()).xmppEventReceiver.setListener(xmppCustomEventListener);



    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        swipe_refresh_data = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_data);
        rv_users = (RecyclerView) view.findViewById(R.id.rv_users);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_users.setLayoutManager(mLayoutManager);
        rv_users.setItemAnimator(new DefaultItemAnimator());

        img_clear = (ImageView) view.findViewById(R.id.img_clear);
        edt_search = (EditText) view.findViewById(R.id.edt_search);

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

                if (usersAdapter != null) {
                    usersAdapter.getFilter().filter(editable);
                }
            }
        });


        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_search.setText("");
            }
        });

        swipe_refresh_data.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_refresh_data.setRefreshing(false);
                ((BaseActivity) getActivity()).initPermission(getActivity(), permissionListenerIntr, true, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);
            }
        });


        usersAdapter = new UsersAdapter(getActivity(), new ArrayList<ContactListResponse.ResponseDataBean>());
        rv_users.setAdapter(usersAdapter);




       if(Pref.getValueBoolean(getActivity(), AppConfing.IS_LOGGED_IN_FIRST_TIME, false))
       {
           Log.e("ad10","---getValueBoolean");
           Pref.setValueBoolean(getActivity(), AppConfing.IS_LOGGED_IN_FIRST_TIME,false);

           ((BaseActivity) getActivity()).initPermission(getActivity(), permissionListenerIntr, true, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);
       }
       else
       {
           Log.e("ad10","---load data");
           LoadData();
       }

    }


    private void LoadData() {
        ((BaseActivity) getActivity()).dismissLoadingDialog();

            DataManager.getInstance()
                    .getAllUser()
                    .observe(getActivity(), new Observer<List<ContactListResponse.ResponseDataBean>>() {
                        @Override
                        public void onChanged(@Nullable List<ContactListResponse.ResponseDataBean> userList) {

                            if (userList != null)
                                LogM.e("" + userList.size());
                            Log.e("ad10","--- data size"+userList.size());
                            if (usersAdapter != null && userList != null) {
                                usersAdapter.swapItems(userList);

                            }

                        }
                    });
    }


    private void getContactList(String contact_list) {
        if (((BaseActivity) getActivity()).validateInternetConn(getActivity())) {

            String username = Pref.getValue(getActivity(), AppConfing.USERNAME, "");
            String login_token = Pref.getValue(getActivity(), AppConfing.LOGIN_TOKEN, "");

            HashMap<String, String> data = new HashMap<>();
            data.put("username", username);
            data.put("login_token", login_token);
            data.put("contacts_list", contact_list);

            Log.d("Params---", username + " " + login_token);

            Call<ContactListResponse> phone_contact_list_call = ApiClient.getClient().contactList(data);
            phone_contact_list_call.enqueue(new Callback<ContactListResponse>() {
                @Override
                public void onResponse(Call<ContactListResponse> call, Response<ContactListResponse> response) {
                    if (response.isSuccessful()) {
                        if (((BaseActivity) getActivity()).validateUser(getActivity(),
                                response.body().getStatus(),
                                response.body().getMessage())) {
                            return;
                        }
                        Log.d("ContactListResponse", response.toString());
                        List<ContactListResponse.ResponseDataBean> mList = response.body().getResponse_data();

                        if (mList != null && mList.size() > 0) {
                            for (ContactListResponse.ResponseDataBean bean : mList) {
                                if (bean != null && bean.getStatus() == Constants.OUR_USERS_STATUS) {
                                    responseDataBeanList.add(bean);

                                    DataManager.getInstance().AddUser(bean);


                                }
                            }
                        }


                        if (mList != null && mList.size() > 0) {
                            usersAdapter = new UsersAdapter(getActivity(), responseDataBeanList);
                            rv_users.setAdapter(usersAdapter);
                        }

                    } else {
                        ((BaseActivity) getActivity()).dismissLoadingDialog();
                        AlertUtils.showSimpleAlert(getActivity(), getActivity().getString(R.string.server_error));
                    }

                    ((BaseActivity) getActivity()).dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<ContactListResponse> call, Throwable t) {
                    ((BaseActivity) getActivity()).dismissLoadingDialog();
                }
            });
        }
    }

    @Override
    public void onPermissionGranted() {

        ((BaseActivity) getActivity()).showLoadingDialog(getActivity());
        new GetMyContactTask(getActivity(), new MyContactListener() {
            @Override
            public void onResponseGetContact(JSONArray jsonArray) {

                Log.d("JSON---", jsonArray.toString());
                getContactList(jsonArray.toString());
            }
        }).execute();
    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        ((BaseActivity) getActivity()).initPermission(getActivity(), permissionListenerIntr, true, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);
    }

    @Override
    public void onResponseGetContact(JSONArray jsonArray) {
        Log.d("JSON---", jsonArray.toString());
        getContactList(jsonArray.toString());
    }
}
