package com.bacancy.eprodigy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.API.AppConfing;
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
    EditText et_code_verification;

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
        et_code_verification = (EditText)findViewById(R.id.et_code_verification);

        tv_resend = (TextView)findViewById(R.id.tv_resend);
        btn_confirm = (Button)findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        tv_resend.setOnClickListener(this);
        img_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_confirm:

                /*for reduce OTP call ********************************** */
                directLogin();//9601564269
                //directLogin2();//9879792298


                /*if(!TextUtils.isEmpty(et_code_verification.getText().toString().trim())){
                    getVerifyUser();
                }else {
                    Toast.makeText(this, "Please enter proper security code.", Toast.LENGTH_SHORT).show();
                }*/

                break;
            case R.id.tv_resend:
                et_code_verification.setText("");
                resendToken();
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void directLogin() {
//        9601564269

    /* {"status":200,"message":"your verification process successfully.","userdata":{"username":"eprodigylxcfvvdltk1546324952","password":"eprodigylxcfvvdltk1546324952","email":"eprodigy@eprodigy.com","serverkey":"","salt":"","iterationcount":"0","created_at":"2019-01-01 06:42:32","displayname":"mansi","userstatus":"Available","phone_number":"9601564269","country_code":"91","withcountrycode":"919601564269","authy_id":"110564521","verified":"1","updated_at":"2019-01-01 01:42:32","device_type":"0","profilepicture":"http://158.69.205.234/eprodigy/assets/admin/images/user.png","device_token":"eb129763-53be-45f9-971f-9896c38a7f95","country_name":"India","deleteaccountstatus":"0","readreceiptstatus":"1","privacystatus":"0","userlastseen":"2019-01-01 01:42:32","IsDisable":"0","contactcategory":"Home","login_token":"PQdq2h4T7367284581bb07597c61361a62b5f23e","role_id":"0","privacylastseenstatus":"0",
    "privacyprofilephotostatus":"0","old_device_token":"eb129763-53be-45f9-971f-9896c38a7f95","notification_enabled":"1"}}*/

        Pref.setValue(MobileVerificationActivity.this,"login_token","PQdq2h4T7367284581bb07597c61361a62b5f23e");
        Pref.setValue(MobileVerificationActivity.this,"username","eprodigylxcfvvdltk1546324952");
        Pref.setValue(MobileVerificationActivity.this,"verified","1");
        Pref.setValue(MobileVerificationActivity.this,"password","eprodigylxcfvvdltk1546324952");
        Pref.setValueBoolean(MobileVerificationActivity.this, AppConfing.IS_LOGGED_IN_FIRST_TIME,true);

        Intent i = new Intent(MobileVerificationActivity.this,UserDetailsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }
    private void directLogin2() {
        //9879792298

    /* {"status":200,"message":"your verification process successfully.","userdata":{"username":"eprodigyr6i6y2qd9o1545809636","password":"eprodigyr6i6y2qd9o1545809636","email":"eprodigy@eprodigy.com","serverkey":"","salt":"","iterationcount":"0",
    "created_at":"2018-12-26 07:33:56","displayname":"Adil","userstatus":"Available","phone_number":"9879792298","country_code":"91","withcountrycode":"919879792298","authy_id":"112952212","verified":"1","updated_at":"2018-12-26 02:33:56","device_type":"0"
    ,"profilepicture":"http://158.69.205.234/eprodigy/assets/admin/images/user.png","device_token":"7e6a517a-0ea5-4ddd-8afe-5ffce2c9069a","country_name":"India","deleteaccountstatus":"0","readreceiptstatus":"1","privacystatus":"0","userlastseen":"2018-12-26 02:33:56",
    "IsDisable":"0","contactcategory":"Home","login_token":"wc6OehBF11dbba6b3832659720fd2d43a257a470","role_id":"0","privacylastseenstatus":"0","privacyprofilephotostatus":"0","old_device_token":"7e6a517a-0ea5-4ddd-8afe-5ffce2c9069a","notification_enabled":"1"}}*/

        Pref.setValue(MobileVerificationActivity.this,"login_token","wc6OehBF11dbba6b3832659720fd2d43a257a470");
        Pref.setValue(MobileVerificationActivity.this,"username","eprodigyr6i6y2qd9o1545809636");
        Pref.setValue(MobileVerificationActivity.this,"verified","1");
        Pref.setValue(MobileVerificationActivity.this,"password","eprodigyr6i6y2qd9o1545809636");
        Pref.setValueBoolean(MobileVerificationActivity.this, AppConfing.IS_LOGGED_IN_FIRST_TIME,true);

        Intent i = new Intent(MobileVerificationActivity.this,UserDetailsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

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

        String verifyToken = et_code_verification.getText().toString().trim();
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
                        Pref.setValueBoolean(MobileVerificationActivity.this, AppConfing.IS_LOGGED_IN_FIRST_TIME,true);

                        Intent i = new Intent(MobileVerificationActivity.this,UserDetailsActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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




}
