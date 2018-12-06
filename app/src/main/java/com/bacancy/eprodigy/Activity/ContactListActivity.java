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
import com.bacancy.eprodigy.permission.PermissionListener;

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
    ArrayList<String> phoneNumberList = new ArrayList<>();
    ArrayList<String> CountryList = new ArrayList<>();
    ArrayList<String> UserNameList = new ArrayList<>();
    ArrayList<String> EmailList = new ArrayList<>();

    private String TAG = "ContactListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactlist);
        mActivity=this;
        permissionListenerIntr=(PermissionListener)this;
        initPermission(mActivity,  permissionListenerIntr, true, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);
        init();
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

    private void getDeviceContactList() {
        ContentResolver cr = this.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                Bitmap bp = BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.profile_pic);

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String image_uri = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                        String country = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                        String email = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        Log.e("Email", email);

                        if (image_uri != null) {

                            try {
                                bp = MediaStore.Images.Media
                                        .getBitmap(this.getContentResolver(),
                                                Uri.parse(image_uri));

                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
//                        Log.i(TAG, "Name: " + name);
//                        Log.i(TAG, "Phone Number: " + phoneNo);
                        phoneNumberList.add(phoneNo);
                        CountryList.add(country);
                        UserNameList.add(name);
                        EmailList.add(email);
                        Log.d("params---",phoneNumberList.size()+" "+UserNameList.size()+ " " +EmailList.size());
                    }
                    pCur.close();
                }
            }

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < UserNameList.size(); i++) {
                JSONObject student1 = new JSONObject();
                try {
                    student1.put("name", UserNameList.get(i));
                    student1.put("email", EmailList.get(i));
                    student1.put("phone", phoneNumberList.get(i));

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                jsonArray.put(student1);
            }
            Log.d("JSON---",jsonArray.toString());

            ContactListAdapter usersAdapter = new ContactListAdapter(this,UserNameList,phoneNumberList);
            rv_contacts.setAdapter(usersAdapter);
        }

        if(cur!=null){
            cur.close();
        }
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
        getDeviceContactList();
    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        initPermission(mActivity,  permissionListenerIntr, true, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);
    }
}