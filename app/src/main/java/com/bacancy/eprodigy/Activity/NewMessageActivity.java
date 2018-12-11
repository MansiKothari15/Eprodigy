package com.bacancy.eprodigy.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.Adapters.UsersAdapter;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;
import com.bacancy.eprodigy.interfaces.MyContactListener;
import com.bacancy.eprodigy.permission.PermissionListener;
import com.bacancy.eprodigy.tasks.GetMyContactTask;
import com.bacancy.eprodigy.utils.AlertUtils;
import com.bacancy.eprodigy.utils.Pref;

import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMessageActivity extends BaseActivity implements PermissionListener {
    Activity mActivity;
    private String TAG = "NewMessageActivity";
    TextView tv_label, tv_right, tv_left, tv_cancel;

    RecyclerView rv_newChat;
    UsersAdapter usersAdapter;
    //    ArrayList<String> phoneNumberList = new ArrayList<>();
//    ArrayList<String> UserNameList = new ArrayList<>();
    LinearLayout ll_newGroup;
    EditText edt_search;
    ImageView img_clear;

    PermissionListener permissionListenerIntr;
    List<ContactListResponse.ResponseDataBean> responseDataBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newchat);

        mActivity = this;
        permissionListenerIntr = this;

        init();
        initPermission(mActivity, permissionListenerIntr, true, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rv_newChat = (RecyclerView) findViewById(R.id.rv_newChat);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_newChat.setLayoutManager(mLayoutManager);
        rv_newChat.setItemAnimator(new DefaultItemAnimator());

        ll_newGroup = (LinearLayout) findViewById(R.id.ll_newGroup);
        ll_newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(NewMessageActivity.this, CreateGroupActivity.class);
                startActivity(i);
            }
        });

        tv_label = (TextView) findViewById(R.id.tv_label);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_label.setText("New Chat");
        hideCustomToolbar();


        img_clear = (ImageView) findViewById(R.id.img_clear);
        edt_search = (EditText) findViewById(R.id.edt_search);
        //adding a TextChangedListener
        //to call a method whenever there is some change on the EditText
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
                if (usersAdapter!=null) {
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

    }



    private void getContactList(String contact_list) {
        if (validateInternetConn(mActivity)) {

            String username = Pref.getValue(mActivity, AppConfing.USERNAME, "");
            String login_token = Pref.getValue(mActivity, AppConfing.LOGIN_TOKEN, "");

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
                        if (validateUser(NewMessageActivity.this,
                                response.body().getStatus(),
                                response.body().getMessage())) {
                            return;
                        }
                        dismissLoadingDialog();
                        Log.d("ContactListResponse", response.toString());
                        List<ContactListResponse.ResponseDataBean> mList = response.body().getResponse_data();

                       /* if (mList != null && mList.size() > 0) {
                            for (ContactListResponse.ResponseDataBean bean : mList) {
                                if (bean != null && !TextUtils.isEmpty(bean.getUserstatus()) && bean.getUserstatus().equalsIgnoreCase(Constants.OUR_USERS_STATUS)) {
                                    responseDataBeanList.add(bean);
                                }
                            }
                        }*/


                        usersAdapter = new UsersAdapter(mActivity, mList);
                        rv_newChat.setAdapter(usersAdapter);

                    }
                    else {
                        dismissLoadingDialog();
                        AlertUtils.showSimpleAlert(NewMessageActivity.this, getString(R.string.server_error));
                    }
                    dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<ContactListResponse> call, Throwable t) {
                    dismissLoadingDialog();
                }
            });
        }
    }

    public void hideCustomToolbar() {
        tv_right.setVisibility(View.INVISIBLE);
        tv_left.setVisibility(View.INVISIBLE);
        tv_cancel.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPermissionGranted() {
        showLoadingDialog(mActivity);
        new GetMyContactTask(mActivity, new MyContactListener() {
            @Override
            public void onResponseGetContact(JSONArray jsonArray) {

                Log.d("JSON---", jsonArray.toString());
                getContactList(jsonArray.toString());
            }
        }).execute();
    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        initPermission(mActivity, permissionListenerIntr, true, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);
    }
}
