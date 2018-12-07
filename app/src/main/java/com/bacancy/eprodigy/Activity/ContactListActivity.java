package com.bacancy.eprodigy.Activity;

import android.Manifest;
import android.app.Activity;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bacancy.eprodigy.Adapters.ContactListAdapter;
import com.bacancy.eprodigy.Adapters.UsersAdapter;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.interfaces.MyContactListener;
import com.bacancy.eprodigy.interfaces.MyContactListenerTwo;
import com.bacancy.eprodigy.permission.PermissionListener;
import com.bacancy.eprodigy.tasks.GetMyContactTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ContactListActivity extends BaseActivity implements View.OnClickListener,PermissionListener{
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    PermissionListener permissionListenerIntr;
    Activity mActivity;
    TextView tv_label,tv_right,tv_left,tv_back;
    RecyclerView rv_contacts;


    private String TAG = "ContactListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactlist);
        mActivity=this;
        permissionListenerIntr=(PermissionListener)this;
        init();
        initPermission(mActivity,  permissionListenerIntr, true, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_label = (TextView)findViewById(R.id.tv_label);
        tv_right = (TextView)findViewById(R.id.tv_right);
        tv_left = (TextView)findViewById(R.id.tv_left);
        tv_back = (TextView)findViewById(R.id.tv_next);
        tv_back.setOnClickListener(this);

        tv_label.setText("Contacts");
        hideCustomToolbar();

        rv_contacts = (RecyclerView)findViewById(R.id.rv_contacts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_contacts.setLayoutManager(mLayoutManager);
        rv_contacts.setItemAnimator(new DefaultItemAnimator());


    }


    public void hideCustomToolbar(){
        tv_right.setVisibility(View.INVISIBLE);
        tv_left.setVisibility(View.INVISIBLE);
        tv_back.setVisibility(View.VISIBLE);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_next:

                finish();
                break;
        }
    }

    @Override
    public void onPermissionGranted() {
        showLoadingDialog(mActivity);
        new GetMyContactTask(mActivity, new MyContactListenerTwo() {
            @Override
            public void onResponseGetContact(ArrayList<String> contactNameList, ArrayList<String> phoneNumberList) {
                dismissLoadingDialog();
                ContactListAdapter usersAdapter = new ContactListAdapter(mActivity,contactNameList,phoneNumberList);
                rv_contacts.setAdapter(usersAdapter);
            }
        }).execute();

    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        initPermission(mActivity,  permissionListenerIntr, true, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);
    }
}