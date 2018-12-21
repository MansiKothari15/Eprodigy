package com.bacancy.eprodigy.Fragment;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.Activity.BaseActivity;
import com.bacancy.eprodigy.Adapters.UsersAdapter;
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
    RecyclerView rv_users;
    UsersAdapter usersAdapter;

    EditText edt_search;
    ImageView img_clear;

    List<ContactListResponse.ResponseDataBean> responseDataBeanList = new ArrayList<>();

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((BaseActivity) getActivity()).showLoadingDialog(getActivity());

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

        ((BaseActivity) getActivity()).initPermission(getActivity(), permissionListenerIntr, true, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);
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
                                    DataManager.getInstance().getAllUser();

                                    DataManager.getInstance()
                                            .getAllUser()
                                            .observe(getActivity(), new Observer<List<ContactListResponse.ResponseDataBean>>() {
                                                @Override
                                                public void onChanged(@Nullable List<ContactListResponse.ResponseDataBean> userList) {

                                                    if (userList != null)
                                                        LogM.e("" + userList.size());


                                                }
                                            });
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
