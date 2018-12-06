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
import com.bacancy.eprodigy.Adapters.UsersAdapter;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;
import com.bacancy.eprodigy.utils.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersFragment extends Fragment {

    private String TAG = "UsersFragment";
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    RecyclerView rv_users;
    UsersAdapter usersAdapter;
    ArrayList<String> phoneNumberList = new ArrayList<>();
    ArrayList<String> CountryList = new ArrayList<>();
    ArrayList<String> UserNameList = new ArrayList<>();
    ArrayList<String> EmailList = new ArrayList<>();
    EditText edt_search;
    ImageView img_clear;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_users = (RecyclerView)view.findViewById(R.id.rv_users);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_users.setLayoutManager(mLayoutManager);
        rv_users.setItemAnimator(new DefaultItemAnimator());

        img_clear = (ImageView) view.findViewById(R.id.img_clear);
        edt_search = (EditText)view.findViewById(R.id.edt_search);
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

        showContacts();
    }


    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<String> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (String s : UserNameList) {
            //if the existing elements contains the search input
            if (s.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        usersAdapter.filterList(filterdNames);
    }

    /**
     * Show the contacts in the ListView.
     */
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            getDeviceContactList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getDeviceContactList() {
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                Bitmap bp = BitmapFactory.decodeResource(getActivity().getResources(),
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
                                        .getBitmap(getActivity().getContentResolver(),
                                                Uri.parse(image_uri));

                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        Log.i(TAG, "Name: " + name);
                        Log.i(TAG, "Phone Number: " + phoneNo);
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
            getContactList(jsonArray.toString());

            usersAdapter = new UsersAdapter(getActivity(),UserNameList,CountryList);
            rv_users.setAdapter(usersAdapter);
        }
        if(cur!=null){
            cur.close();
        }
    }

    private void getContactList(String contact_list) {

        String username = Pref.getValue(getActivity(), AppConfing.USERNAME, "");
        String login_token = Pref.getValue(getActivity(), AppConfing.LOGIN_TOKEN, "");

        HashMap<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("login_token", login_token);
        data.put("contacts_list", contact_list);
        Log.d("Params---",username + " " + login_token);


        Call<ContactListResponse> phone_contact_list_call = ApiClient.getClient().contactList(data);
        phone_contact_list_call.enqueue(new Callback<ContactListResponse>() {
            @Override
            public void onResponse(Call<ContactListResponse> call, Response<ContactListResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("ContactListResponse",response.toString());

                }
            }

            @Override
            public void onFailure(Call<ContactListResponse> call, Throwable t) {


            }
        });
    }


}
