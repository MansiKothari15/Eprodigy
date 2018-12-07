package com.bacancy.eprodigy.Fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.Activity.BaseActivity;
import com.bacancy.eprodigy.Adapters.UsersAdapter;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;
import com.bacancy.eprodigy.interfaces.MyContactListener;
import com.bacancy.eprodigy.permission.PermissionListener;
import com.bacancy.eprodigy.tasks.GetMyContactTask;
import com.bacancy.eprodigy.utils.Constants;
import com.bacancy.eprodigy.utils.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
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
        //to call a method whenever there is some change on the EditText
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
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

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<String> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (ContactListResponse.ResponseDataBean sDataBean : responseDataBeanList) {
            //if the existing elements contains the search input
            if (sDataBean.getName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                responseDataBeanList.add(sDataBean);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        usersAdapter.filterList(responseDataBeanList);
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
                        Log.d("ContactListResponse", response.toString());
                        List<ContactListResponse.ResponseDataBean> mList = response.body().getResponseData();

                       /* if (mList != null && mList.size() > 0) {
                            for (ContactListResponse.ResponseDataBean bean : mList) {
                                if (bean != null && !TextUtils.isEmpty(bean.getUserstatus()) && bean.getUserstatus().equalsIgnoreCase(Constants.OUR_USERS_STATUS)) {
                                    responseDataBeanList.add(bean);
                                }
                            }
                        }*/


                        usersAdapter = new UsersAdapter(getActivity(), mList);
                        rv_users.setAdapter(usersAdapter);

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
