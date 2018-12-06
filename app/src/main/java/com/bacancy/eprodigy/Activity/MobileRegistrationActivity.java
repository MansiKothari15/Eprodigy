package com.bacancy.eprodigy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.CountryResponse;
import com.bacancy.eprodigy.ResponseModel.RegisterResponse;
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
    ArrayList<String> CountryCodeArrayList = new ArrayList<>();
    ArrayList<String> CountryNameArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
    }

    private void init() {
        edt_phoneNo = (EditText)findViewById(R.id.edt_phoneNo);
        sp_countrycode = (Spinner)findViewById(R.id.sp_countrycode);
        btn_continue = (Button)findViewById(R.id.btn_continue);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_continue:
                if(edt_phoneNo.getText().length() != 0){
                    getRegister();
                }else {
                    Toast.makeText(this, "Phone number is required.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void getCountryList(){

        HashMap<String, String> data = new HashMap<>();
        Call<CountryResponse> call = ApiClient.getClient().country(data);
        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {

                Log.d("CountryResponse", response.toString());
                progressUtils.dismissProgressDialog();
                CountryCodeArrayList.clear();
//                CountryCodeArrayList.add("+1");
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().getResponseData().size(); i++) {
                        CountryCodeArrayList.add(response.body().getResponseData().get(i).getPhonecode());
                        CountryNameArrayList.add(response.body().getResponseData().get(i).getNicename());
                    }

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, CountryCodeArrayList);
                    adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                    sp_countrycode.setAdapter(adapter);

                }

            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                progressUtils.dismissProgressDialog();
            }
        });
    }

    public void getRegister() {

        progressUtils.showProgressDialog("Please wait...");

        final String uniqueID = UUID.randomUUID().toString();
        final String phoneNo = edt_phoneNo.getText().toString();
        final String countryCode = sp_countrycode.getSelectedItem().toString();
        String countryName = CountryNameArrayList.get(sp_countrycode.getSelectedItemPosition());
        Log.d("params---",uniqueID +" "+ phoneNo +" "+ countryCode +" "+ countryName);

        HashMap<String, String> data = new HashMap<>();
        data.put("username","xxx");
        data.put("email_id","xxx@gmail.com");
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
                progressUtils.dismissProgressDialog();
                if (response.isSuccessful()) {
                    Pref.setValue(MobileRegistrationActivity.this,"auth_id",String.valueOf(response.body().getAuthyId()));
                    Pref.setValue(MobileRegistrationActivity.this,"device_token",uniqueID);
                    Pref.setValue(MobileRegistrationActivity.this,"phone_number",phoneNo);
                    Pref.setValue(MobileRegistrationActivity.this,"country_code",countryCode);
                    Intent i = new Intent(MobileRegistrationActivity.this,MobileVerificationActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                progressUtils.dismissProgressDialog();
            }
        });
    }

}
