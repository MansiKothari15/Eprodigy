package com.bacancy.eprodigy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.ResendCodeResponse;
import com.bacancy.eprodigy.ResponseModel.VerifyUserResponse;
import com.bacancy.eprodigy.utils.AlertUtils;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.utils.Pref;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileVerificationActivity extends BaseActivity implements View.OnClickListener {

    Button btn_confirm;
    EditText edt_1,edt_2,edt_3,edt_4,edt_5,edt_6,edt_7;
    TextView tv_resend;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        init();
    }

    private void init() {
        img_back = (ImageView)findViewById(R.id.img_back);
        edt_1 = (EditText)findViewById(R.id.edt_1);
        edt_2 = (EditText)findViewById(R.id.edt_2);
        edt_3 = (EditText)findViewById(R.id.edt_3);
        edt_4 = (EditText)findViewById(R.id.edt_4);
        edt_5 = (EditText)findViewById(R.id.edt_5);
        edt_6 = (EditText)findViewById(R.id.edt_6);
        edt_7 = (EditText)findViewById(R.id.edt_7);
        tv_resend = (TextView)findViewById(R.id.tv_resend);
        btn_confirm = (Button)findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        tv_resend.setOnClickListener(this);
        img_back.setOnClickListener(this);

        changeFocus();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_confirm:
                if(edt_1.getText().length()!=0 && edt_2.getText().length()!=0 && edt_3.getText().length()!=0
                        && edt_4.getText().length()!=0 && edt_5.getText().length()!=0 && edt_6.getText().length()!=0 && edt_7.getText().length()!=0){
                    getVerifyUser();
                }else {
                    Toast.makeText(this, "Please enter proper security code.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_resend:
                edt_1.setText("");
                edt_2.setText("");
                edt_3.setText("");
                edt_4.setText("");
                edt_5.setText("");
                edt_6.setText("");
                edt_7.setText("");
                resendToken();
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    public void resendToken(){
        showLoadingDialog(this);
        String auth_id = Pref.getValue(MobileVerificationActivity.this,"auth_id","");

        HashMap<String, String> data = new HashMap<>();
        data.put("authy_id",auth_id);

        Call<ResendCodeResponse> call = ApiClient.getClient().resendToken(data);
        call.enqueue(new Callback<ResendCodeResponse>() {
            @Override
            public void onResponse(Call<ResendCodeResponse> call, Response<ResendCodeResponse> response) {

                Log.d("ResendTokenResponse", response.toString());
                dismissLoadingDialog();

                if (response.isSuccessful()) {
                    Toast.makeText(MobileVerificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResendCodeResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                dismissLoadingDialog();
            }
        });
    }


    public void getVerifyUser() {

        showLoadingDialog(this);

        String verifyToken = edt_1.getText().toString()+edt_2.getText().toString()+edt_3.getText().toString()
                +edt_4.getText().toString()+edt_5.getText().toString()+edt_6.getText().toString()+edt_7.getText().toString();
        String auth_id = Pref.getValue(MobileVerificationActivity.this,"auth_id","");
        String device_token = Pref.getValue(MobileVerificationActivity.this,"device_token","");
        String phone_number = Pref.getValue(MobileVerificationActivity.this,"phone_number","");
        String country_code = Pref.getValue(MobileVerificationActivity.this,"country_code","");
        Log.d("Params--",verifyToken+" "+auth_id+" "+device_token+" "+phone_number+" "+country_code);

        String new_auth_id = Pref.getValue(MobileVerificationActivity.this,"new_auth_id","");
        String old_auth_id = Pref.getValue(MobileVerificationActivity.this,"old_auth_id","");
        Log.d("New Params--",new_auth_id +" "+ old_auth_id);
        if(new_auth_id.equals("") && old_auth_id.equals("")){
            new_auth_id = auth_id;
            old_auth_id = auth_id;
        }

        HashMap<String, String> data = new HashMap<>();
        data.put("verify_token",verifyToken);
        data.put("oldauthy_id",old_auth_id);
        data.put("phonenumber", phone_number);
        data.put("newauthy_id", new_auth_id);
        data.put("countrycode", country_code);
        data.put("device_token", device_token);

        Call<VerifyUserResponse> call = ApiClient.getClient().verifyUser(data);
        call.enqueue(new Callback<VerifyUserResponse>() {
            @Override
            public void onResponse(Call<VerifyUserResponse> call, Response<VerifyUserResponse> response) {

                if (response.isSuccessful()) {
                    dismissLoadingDialog();
                    Log.d("VerifyUserResponse", response.toString());
                    if (validateUser(MobileVerificationActivity.this,
                            response.body().getStatus(),
                            response.body().getMessage())) {
                        return;
                    }


                    if (response.body().getStatus() == 200) {

                        Pref.setValue(MobileVerificationActivity.this,"login_token",response.body().getUserdata().getLoginToken());
                        Pref.setValue(MobileVerificationActivity.this,"username",response.body().getUserdata().getUsername());
                        Pref.setValue(MobileVerificationActivity.this,"verified",response.body().getUserdata().getVerified());
                        Pref.setValue(MobileVerificationActivity.this,"password",response.body().getUserdata().getPassword());

                        Intent i = new Intent(MobileVerificationActivity.this,UserDetailsActivity.class);
                        startActivity(i);
                        finish();
                    }else {
                        Toast.makeText(MobileVerificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                     dismissLoadingDialog();
                    AlertUtils.showSimpleAlert(MobileVerificationActivity.this,getString(R.string.server_error));
                }

            }

            @Override
            public void onFailure(Call<VerifyUserResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                dismissLoadingDialog();
            }
        });
    }


    private void changeFocus() {
        edt_1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(edt_1.getText().toString().length()==1)     //size as per your requirement
                {
                    edt_2.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        edt_2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(edt_2.getText().toString().length()==1)     //size as per your requirement
                {
                    edt_3.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        edt_3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(edt_3.getText().toString().length()==1)     //size as per your requirement
                {
                    edt_4.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        edt_4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(edt_4.getText().toString().length()==1)     //size as per your requirement
                {
                    edt_5.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        edt_5.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(edt_5.getText().toString().length()==1)     //size as per your requirement
                {
                    edt_6.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        edt_6.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(edt_6.getText().toString().length()==1)     //size as per your requirement
                {
                    edt_7.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
    }

}
