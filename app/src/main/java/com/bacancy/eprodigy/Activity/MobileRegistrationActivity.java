package com.bacancy.eprodigy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.CountryResponse;
import com.bacancy.eprodigy.ResponseModel.RegisterResponse;
import com.bacancy.eprodigy.utils.AlertUtils;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.utils.Pref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileRegistrationActivity extends BaseActivity implements View.OnClickListener {

    Button btn_continue;
    EditText edt_phoneNo;
    Spinner sp_countrycode;
    ProgressBar pgb_country;
    ArrayList<String> CountryCodeArrayList = new ArrayList<>();
    ArrayList<String> CountryNameArrayList = new ArrayList<>();
    ArrayList<String> CountryCodeNameArrayList = new ArrayList<>();

    int indiaCodePos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
    }

    private void init() {
        pgb_country = findViewById(R.id.pgb_country);
        edt_phoneNo = (EditText) findViewById(R.id.edt_phoneNo);
        edt_phoneNo.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        sp_countrycode = (Spinner) findViewById(R.id.sp_countrycode);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);

        getCountryList();

        sp_countrycode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = sp_countrycode.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void directLogin() {
//        9601564269

    /* {"status":200,"message":"your verification process successfully.","userdata":{"username":"eprodigylxcfvvdltk1546324952","password":"eprodigylxcfvvdltk1546324952","email":"eprodigy@eprodigy.com","serverkey":"","salt":"","iterationcount":"0","created_at":"2019-01-01 06:42:32","displayname":"mansi","userstatus":"Available","phone_number":"9601564269","country_code":"91","withcountrycode":"919601564269","authy_id":"110564521","verified":"1","updated_at":"2019-01-01 01:42:32","device_type":"0","profilepicture":"http://158.69.205.234/eprodigy/assets/admin/images/user.png","device_token":"eb129763-53be-45f9-971f-9896c38a7f95","country_name":"India","deleteaccountstatus":"0","readreceiptstatus":"1","privacystatus":"0","userlastseen":"2019-01-01 01:42:32","IsDisable":"0","contactcategory":"Home","login_token":"PQdq2h4T7367284581bb07597c61361a62b5f23e","role_id":"0","privacylastseenstatus":"0",
    "privacyprofilephotostatus":"0","old_device_token":"eb129763-53be-45f9-971f-9896c38a7f95","notification_enabled":"1"}}*/

        Pref.setValue(MobileRegistrationActivity.this, "auth_id", "110564521");
        Pref.setValue(MobileRegistrationActivity.this, "device_token", UUID.randomUUID().toString());
        Pref.setValue(MobileRegistrationActivity.this, "phone_number", "9601564269");
        Pref.setValue(MobileRegistrationActivity.this, "country_code", CountryCodeArrayList.get(sp_countrycode.getSelectedItemPosition()));


        Pref.setValue(activity,"login_token","Zxg8PuH47367284581bb07597c61361a62b5f23e");
        Pref.setValue(activity,"username","eprodigylxcfvvdltk1546324952");
        Pref.setValue(activity,"verified","1");
        Pref.setValue(activity,"password","eprodigylxcfvvdltk1546324952");
        Pref.setValueBoolean(activity, AppConfing.IS_LOGGED_IN_FIRST_TIME,true);

        Intent i = new Intent(activity,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }
    private void directLogin2() {
        //9879792298

    /* {"status":200,"message":"your verification process successfully.","userdata":{"username":"eprodigyr6i6y2qd9o1545809636","password":"eprodigyr6i6y2qd9o1545809636","email":"eprodigy@eprodigy.com","serverkey":"","salt":"","iterationcount":"0",
    "created_at":"2018-12-26 07:33:56","displayname":"Adil","userstatus":"Available","phone_number":"9879792298","country_code":"91","withcountrycode":"919879792298","authy_id":"112952212","verified":"1","updated_at":"2018-12-26 02:33:56","device_type":"0"
    ,"profilepicture":"http://158.69.205.234/eprodigy/assets/admin/images/user.png","device_token":"7e6a517a-0ea5-4ddd-8afe-5ffce2c9069a","country_name":"India","deleteaccountstatus":"0","readreceiptstatus":"1","privacystatus":"0","userlastseen":"2018-12-26 02:33:56",
    "IsDisable":"0","contactcategory":"Home","login_token":"PvRhq6rE11dbba6b3832659720fd2d43a257a470","role_id":"0","privacylastseenstatus":"0","privacyprofilephotostatus":"0","old_device_token":"7e6a517a-0ea5-4ddd-8afe-5ffce2c9069a","notification_enabled":"1"}}*/

        Pref.setValue(MobileRegistrationActivity.this, "auth_id", "112952212");
        Pref.setValue(MobileRegistrationActivity.this, "device_token", UUID.randomUUID().toString());
        Pref.setValue(MobileRegistrationActivity.this, "phone_number", "9879792298");
        Pref.setValue(MobileRegistrationActivity.this, "country_code", CountryCodeArrayList.get(sp_countrycode.getSelectedItemPosition()));


        Pref.setValue(activity,"login_token","PvRhq6rE11dbba6b3832659720fd2d43a257a470");
        Pref.setValue(activity,"username","eprodigyr6i6y2qd9o1545809636");
        Pref.setValue(activity,"verified","1");
        Pref.setValue(activity,"password","eprodigyr6i6y2qd9o1545809636");
        Pref.setValueBoolean(activity, AppConfing.IS_LOGGED_IN_FIRST_TIME,true);

        Intent i = new Intent(activity,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }
    private void directLogin3() {
        //9879792298

    /* {"status":200,"message":"your verification process successfully.","userdata":
    {"username":"eprodigyl1m3iq6tnv1545809249","password":"eprodigyl1m3iq6tnv1545809249",
    "email":"eprodigy@eprodigy.com","serverkey":"","salt":"","iterationcount":"0","created_at":"2018-12-26 07:27:29",
    "displayname":"ad jio","userstatus":"Available","phone_number":"6354698146","country_code":"91","withcountrycode":"916354698146",
    "authy_id":"113364642","verified":"1","updated_at":"2018-12-26 02:27:29","device_type":"0",
    "profilepicture":"http://158.69.205.234/eprodigy/assets/admin/images/user.png","device_token":"d7e04caf-a929-46f8-8263-a88dda0286cf",
    "country_name":"India","deleteaccountstatus":"0","readreceiptstatus":"1","privacystatus":"0","userlastseen":"2018-12-26 02:27:29",
    "IsDisable":"0","contactcategory":"Home","login_token":"DEH9xdEg470d88a617aea639c9abdc9178545a0b","role_id":"0",
    "privacylastseenstatus":"0","privacyprofilephotostatus":"0","old_device_token":"d7e04caf-a929-46f8-8263-a88dda0286cf",
    "notification_enabled":"1"}}*/

        Pref.setValue(MobileRegistrationActivity.this, "auth_id", "113364642");
        Pref.setValue(MobileRegistrationActivity.this, "device_token", "d7e04caf-a929-46f8-8263-a88dda0286cf");
        Pref.setValue(MobileRegistrationActivity.this, "phone_number", "6354698146");
        Pref.setValue(MobileRegistrationActivity.this, "country_code", CountryCodeArrayList.get(sp_countrycode.getSelectedItemPosition()));


        Pref.setValue(activity,"login_token","DEH9xdEg470d88a617aea639c9abdc9178545a0b");
        Pref.setValue(activity,"username","eprodigyl1m3iq6tnv1545809249");
        Pref.setValue(activity,"verified","1");
        Pref.setValue(activity,"password","eprodigyl1m3iq6tnv1545809249");
        Pref.setValueBoolean(activity, AppConfing.IS_LOGGED_IN_FIRST_TIME,true);

        Intent i = new Intent(activity,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_continue:
                if (edt_phoneNo.getText().length() != 0) {
                   // directLogin();//9601564269
                   // directLogin2();//9879792298
                   // directLogin3();//6354698146
                     getRegister();
                } else {
                    Toast.makeText(this, "Phone number is required.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void getCountryList() {
        pgb_country.setVisibility(View.VISIBLE);
        HashMap<String, String> data = new HashMap<>();
        Call<CountryResponse> call = ApiClient.getClient().country(data);

        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {

                Log.d("CountryResponse", response.toString());
                dismissLoadingDialog();

                CountryCodeNameArrayList.clear();
                CountryCodeArrayList.clear();
                CountryNameArrayList.clear();

//                CountryCodeArrayList.add("+1");
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().getResponseData().size(); i++) {
                        if (!TextUtils.isEmpty(response.body().getResponseData().get(i).getPhonecode()) && !TextUtils.isEmpty(response.body().getResponseData().get(i).getNicename())) {

                            CountryNameArrayList.add(response.body().getResponseData().get(i).getNicename());
                            CountryCodeArrayList.add(response.body().getResponseData().get(i).getPhonecode());
                            CountryCodeNameArrayList.add("(" + response.body().getResponseData().get(i).getPhonecode() + ") "+response.body().getResponseData().get(i).getNicename());

                        }

                        //default
                        if (!TextUtils.isEmpty(response.body().getResponseData().get(i).getPhonecode())
                                && response.body().getResponseData().get(i).getPhonecode().equalsIgnoreCase("91")) {
                            indiaCodePos = i;
                        }
                    }

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, CountryCodeNameArrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    sp_countrycode.setAdapter(adapter);
                    sp_countrycode.setSelection(indiaCodePos);
                    pgb_country.setVisibility(View.GONE);
                } else {
                    pgb_country.setVisibility(View.GONE);
                    dismissLoadingDialog();
                    AlertUtils.showSimpleAlert(MobileRegistrationActivity.this, getString(R.string.server_error));

                }

            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                pgb_country.setVisibility(View.GONE);
                LogM.e("errrrrrr" + t.toString());
                dismissLoadingDialog();
            }
        });
    }

    public void getRegister() {
        showLoadingDialog(this);

        final String uniqueID = UUID.randomUUID().toString();
        final String phoneNo = edt_phoneNo.getText().toString().trim().replaceAll("[^0-9]", "");
        //  final String phoneNo = edt_phoneNo.getText().toString();
        //final String countryCode = sp_countrycode.getSelectedItem().toString();
        final String countryCode = CountryCodeArrayList.get(sp_countrycode.getSelectedItemPosition());
        String countryName = CountryNameArrayList.get(sp_countrycode.getSelectedItemPosition());

        Log.d("params---", uniqueID + " " + phoneNo + " " + countryCode + " " + countryName);

        HashMap<String, String> data = new HashMap<>();
        data.put("username", "xxx");
        data.put("email_id", "xxx@gmail.com");
        data.put("password", "12345678");
        data.put("phone_number", phoneNo);
        data.put("device_token", uniqueID);
        data.put("device_type", "0");
        data.put("country_name", countryName);
        data.put("country_code", countryCode);

        Call<RegisterResponse> call = ApiClient.getClient().register(data);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                Log.d("RegisterResponse", response.toString());
                dismissLoadingDialog();
                if (response.isSuccessful()) {
                    Pref.setValue(MobileRegistrationActivity.this, "auth_id", String.valueOf(response.body().getAuthyId()));
                    Pref.setValue(MobileRegistrationActivity.this, "device_token", uniqueID);
                    Pref.setValue(MobileRegistrationActivity.this, "phone_number", phoneNo);
                    Pref.setValue(MobileRegistrationActivity.this, "country_code", countryCode);
                    Intent i = new Intent(MobileRegistrationActivity.this, MobileVerificationActivity.class);
                    startActivity(i);
                } else {
                    dismissLoadingDialog();
                    AlertUtils.showSimpleAlert(MobileRegistrationActivity.this, getString(R.string.server_error));
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                dismissLoadingDialog();
            }
        });
    }

}
