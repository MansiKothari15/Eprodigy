package com.bacancy.eprodigy.tasks;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.bacancy.eprodigy.interfaces.MyContactListener;
import com.bacancy.eprodigy.interfaces.MyContactListenerTwo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetMyContactTask extends AsyncTask<Void, Void, JSONArray> {
    String TAG = "GetMyContact";
    Context mContext;
    MyContactListener contactListener = null;
    MyContactListenerTwo contactListenerTwo = null;

    ArrayList<String> phoneNumberList;
    ArrayList<String> UserNameList;

    ArrayList<String> CountryList;
    ArrayList<String> EmailList;

    public GetMyContactTask(Context mContext, MyContactListener contactListener) {

        phoneNumberList = new ArrayList<>();
        CountryList = new ArrayList<>();
        UserNameList = new ArrayList<>();
        EmailList = new ArrayList<>();

        this.mContext = mContext;
        this.contactListener = contactListener;
        this.contactListenerTwo = null;

    }

    public GetMyContactTask(Context mContext, MyContactListenerTwo contactListenerTwo) {

        phoneNumberList = new ArrayList<>();
        CountryList = new ArrayList<>();
        UserNameList = new ArrayList<>();
        EmailList = new ArrayList<>();

        this.mContext = mContext;
        this.contactListenerTwo = contactListenerTwo;
        this.contactListener = null;
    }

    @Override
    protected JSONArray doInBackground(Void... voids) {

        ContentResolver cr = mContext.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                /*Bitmap bp = BitmapFactory.decodeResource(mContext.getResources(),
                        R.mipmap.profile_pic);*/

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
                        /*String image_uri = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.PHOTO_URI));*/
                        String country = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                        String email = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        Log.e("Email", email);

                       /* if (image_uri != null) {

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
                        }*/
                        Log.i(TAG, "Name: " + name);


                            Log.i(TAG, "Phone Number: " + phoneNo);

                            String ph=phoneNo.replace("(","");
                            String ph2=ph.replace(")","");
                            String ph3=ph2.replace("-","");
                            String ph4=ph3.replace(" ","");
                            phoneNumberList.add(ph4);

                        CountryList.add(country);
                        UserNameList.add(name);
                        EmailList.add(email);
                        Log.d("params---", phoneNumberList.size() + " " + UserNameList.size() + " " + EmailList.size());
                    }
                    pCur.close();
                }
            }


        }
        if (cur != null) {
            cur.close();
        }

        JSONArray jsonArrayContactList = new JSONArray();
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
            jsonArrayContactList.put(student1);
        }

        if (jsonArrayContactList != null) {
            return jsonArrayContactList;
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArrayContactList) {

        if (contactListener != null) {
            contactListener.onResponseGetContact(jsonArrayContactList);
        } else if (contactListenerTwo != null) {
            contactListenerTwo.onResponseGetContact(UserNameList, phoneNumberList);
        }

    }
}
