package com.bacancy.eprodigy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.Adapters.CreateGroupAdapter;
import com.bacancy.eprodigy.Adapters.GroupContactAdapter;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;
import com.bacancy.eprodigy.interfaces.MyContactListener;
import com.bacancy.eprodigy.tasks.GetMyContactTask;
import com.bacancy.eprodigy.utils.AlertUtils;
import com.bacancy.eprodigy.utils.Constants;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.utils.Pref;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroupActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_label,tv_right,tv_left,tv_next,tv_back;
    public static RecyclerView rv_group;
    RecyclerView rv_groupContacts;
    CreateGroupAdapter createGroupAdapter;
    GroupContactAdapter groupContactAdapter;
    List<ContactListResponse.ResponseDataBean> responseDataBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategroup);
        init();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_label = (TextView)findViewById(R.id.tv_label);
        tv_right = (TextView)findViewById(R.id.tv_right);
        tv_left = (TextView)findViewById(R.id.tv_left);
        tv_next = (TextView)findViewById(R.id.tv_next);
        tv_back = (TextView)findViewById(R.id.tv_back);
        tv_next.setOnClickListener(this);
        tv_back.setOnClickListener(this);

        tv_label.setText("Add Users");
        hideCustomToolbar();

        rv_group = (RecyclerView)findViewById(R.id.rv_group);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        rv_group.setLayoutManager(mLayoutManager);
        rv_group.setItemAnimator(new DefaultItemAnimator());

        rv_groupContacts = (RecyclerView)findViewById(R.id.rv_groupContacts);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        rv_groupContacts.setLayoutManager(mLayoutManager1);
        rv_groupContacts.setItemAnimator(new DefaultItemAnimator());

        ((BaseActivity) CreateGroupActivity.this).showLoadingDialog(CreateGroupActivity.this);
        new GetMyContactTask(CreateGroupActivity.this, new MyContactListener() {
            @Override
            public void onResponseGetContact(JSONArray jsonArray) {

                Log.d("JSON---", jsonArray.toString());
                getContactList(jsonArray.toString());
            }
        }).execute();
    }

    public void hideCustomToolbar(){
        tv_right.setVisibility(View.INVISIBLE);
        tv_left.setVisibility(View.INVISIBLE);
        tv_next.setVisibility(View.VISIBLE);
        tv_back.setVisibility(View.VISIBLE);
    }

    private void getContactList(String contact_list) {
        if (((BaseActivity) CreateGroupActivity.this).validateInternetConn(CreateGroupActivity.this)) {

            String username = Pref.getValue(CreateGroupActivity.this, AppConfing.USERNAME, "");
            String login_token = Pref.getValue(CreateGroupActivity.this, AppConfing.LOGIN_TOKEN, "");

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
                        if (((BaseActivity) CreateGroupActivity.this).validateUser(CreateGroupActivity.this,
                                response.body().getStatus(),
                                response.body().getMessage())) {
                            return;
                        }
                        Log.d("ContactListResponse", response.toString());
                        List<ContactListResponse.ResponseDataBean> mList = response.body().getResponse_data();

                        if (mList != null && mList.size() > 0) {
                            for (ContactListResponse.ResponseDataBean bean : mList) {
                                if (bean != null && bean.getStatus()==Constants.OUR_USERS_STATUS) {
                                    responseDataBeanList.add(bean);
                                }
                            }
                        }

                        if (mList != null && mList.size() > 0) {
                            groupContactAdapter = new GroupContactAdapter(CreateGroupActivity.this, responseDataBeanList);
                            rv_groupContacts.setAdapter(groupContactAdapter);
                        }

                    } else {
                        ((BaseActivity) CreateGroupActivity.this).dismissLoadingDialog();
                        AlertUtils.showSimpleAlert(CreateGroupActivity.this, CreateGroupActivity.this.getString(R.string.server_error));
                    }

                    ((BaseActivity) CreateGroupActivity.this).dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<ContactListResponse> call, Throwable t) {
                    ((BaseActivity) CreateGroupActivity.this).dismissLoadingDialog();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_next:
                LogM.e("  uname>>"+groupContactAdapter.getCheckSetList().size());
                LogM.e("  name>>"+groupContactAdapter.getNameList().size());

                Intent i = new Intent(CreateGroupActivity.this,GroupSubjectActivity.class);
                i.putExtra("checked_array_list", groupContactAdapter.getCheckSetList());
                i.putExtra("name_array_list", groupContactAdapter.getNameList());
                startActivity(i);
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
