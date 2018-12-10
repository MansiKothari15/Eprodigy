package com.bacancy.eprodigy.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.CountryResponse;
import com.bacancy.eprodigy.ResponseModel.MobileNumberChangeResponse;
import com.bacancy.eprodigy.utils.AlertUtils;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.utils.Pref;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeNumberActivity extends BaseActivity implements View.OnClickListener {
    Activity mActivity;
    TextView tv_label, tv_back, tv_right, tv_phoneNo, tv_countrycode, tv_done;
    Spinner sp_countrycode;
    EditText edt_phoneNo;
    ArrayList<String> CountryCodeArrayList = new ArrayList<>();
    ArrayList<String> CountryNameArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changenumber);
        mActivity = this;
        init();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_label = (TextView) findViewById(R.id.tv_label);
        tv_label.setText("Change Number");
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_done = (TextView) findViewById(R.id.tv_done);
        tv_done.setOnClickListener(this);

        tv_phoneNo = (TextView) findViewById(R.id.tv_phoneNo);
        tv_countrycode = (TextView) findViewById(R.id.tv_countrycode);
        sp_countrycode = (Spinner) findViewById(R.id.sp_countrycode);
        edt_phoneNo = (EditText) findViewById(R.id.edt_phoneNo);

        String phone = Pref.getValue(ChangeNumberActivity.this, "phone_number", "");
        tv_phoneNo.setText(phone);
        String country = Pref.getValue(ChangeNumberActivity.this, "country_code", "");
        tv_countrycode.setText("+" + country);

        hideCustomToolbar();
        getCountryList();
    }

    public void hideCustomToolbar() {
        tv_right.setVisibility(View.INVISIBLE);
        tv_back.setVisibility(View.VISIBLE);
        tv_done.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_done:
                changeMobileNo();
                break;
        }
    }

    public void getCountryList() {

        HashMap<String, String> data = new HashMap<>();
        Call<CountryResponse> call = ApiClient.getClient().country(data);
        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {

                Log.d("CountryResponse", response.toString());
                dismissLoadingDialog();
                CountryCodeArrayList.clear();

                if (response.isSuccessful()) {
                    if (validateUser(mActivity,
                            response.body().getStatus(),
                            response.body().getMessage())) {
                        return;
                    }

                    for (int i = 0; i < response.body().getResponseData().size(); i++) {
                        CountryCodeArrayList.add(response.body().getResponseData().get(i).getPhonecode());
                        CountryNameArrayList.add(response.body().getResponseData().get(i).getNicename());
                    }

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, CountryCodeArrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    sp_countrycode.setAdapter(adapter);

                }
                else {
                     dismissLoadingDialog();
                    AlertUtils.showSimpleAlert(mActivity, getString(R.string.server_error));
                }
                /* if (response.isSuccessful()) {
                    ((BaseActivity) getActivity()).dismissLoadingDialog();

                    if (((BaseActivity) getActivity()).validateUser(getActivity(),
                            response.body().getStatus(),
                            response.body().getMessage())) {
                        return;
                    }




                }
                else {
                    ((BaseActivity) getActivity()).dismissLoadingDialog();
                    AlertUtils.showSimpleAlert(getActivity(), getActivity().getString(R.string.server_error));
                }*/

            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                dismissLoadingDialog();
            }
        });
    }

    private void changeMobileNo() {
        showLoadingDialog(this);

        String login_token = Pref.getValue(this, "login_token", "");
        String username = Pref.getValue(this, "username", "");
        String countryCode = sp_countrycode.getSelectedItem().toString();

        HashMap<String, String> data = new HashMap<>();
        data.put("login_token", login_token);
        data.put("username", username);
        data.put("oldnumber", tv_phoneNo.getText().toString());
        data.put("country_code", countryCode);
        data.put("newnumber", edt_phoneNo.getText().toString());

        Call<MobileNumberChangeResponse> call = ApiClient.getClient().mobileNoChange(data);
        call.enqueue(new Callback<MobileNumberChangeResponse>() {
            @Override
            public void onResponse(Call<MobileNumberChangeResponse> call, Response<MobileNumberChangeResponse> response) {

                if (response.isSuccessful()) {
                    if (validateUser(mActivity,
                            response.body().getStatus(),
                            response.body().getMessage())) {
                        return;
                    }
                    Log.d("MobileNoResponse", response.toString());
                    dismissLoadingDialog();
                    String new_auth_id = String.valueOf(response.body().getUserdata().getNewauthyId());
                    Pref.setValue(ChangeNumberActivity.this, "new_auth_id", new_auth_id);
                    String old_auth_id = String.valueOf(response.body().getUserdata().getOldauthyId());
                    Pref.setValue(ChangeNumberActivity.this, "old_auth_id", old_auth_id);
                    Pref.setValue(ChangeNumberActivity.this, "phone_number", edt_phoneNo.getText().toString());
                    Intent i = new Intent(ChangeNumberActivity.this, MobileVerificationActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    dismissLoadingDialog();
                    AlertUtils.showSimpleAlert(mActivity, mActivity.getString(R.string.server_error));
                }


            }

            @Override
            public void onFailure(Call<MobileNumberChangeResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                dismissLoadingDialog();
            }
        });
    }
}
