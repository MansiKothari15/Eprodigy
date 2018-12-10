package com.bacancy.eprodigy.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.UserDisplayNameResponse;
import com.bacancy.eprodigy.utils.AlertUtils;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.utils.Pref;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUsernameActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_label,tv_back,tv_right,tv_done;
    EditText edt_username;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituser);
        mActivity=this;
        init();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_label = (TextView)findViewById(R.id.tv_label);
        tv_label.setText("UserName");
        tv_back = (TextView)findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        tv_right = (TextView)findViewById(R.id.tv_right);
        tv_done = (TextView)findViewById(R.id.tv_done);
        tv_done.setOnClickListener(this);
        edt_username = (EditText)findViewById(R.id.edt_username);
        String username = Pref.getValue(EditUsernameActivity.this,"DisplayName","");
        edt_username.setText(username);
        hideCustomToolbar();
    }

    public void hideCustomToolbar(){
        tv_right.setVisibility(View.INVISIBLE);
        tv_back.setVisibility(View.VISIBLE);
        tv_done.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_done:
                updateUserDisplayName();
                break;
        }
    }

    private void updateUserDisplayName() {
        showLoadingDialog(this);

        String username = Pref.getValue(this, AppConfing.USERNAME, "");
        String login_token = Pref.getValue(this, AppConfing.LOGIN_TOKEN, "");

        HashMap<String, String> data = new HashMap<>();
        data.put("username",username);
        data.put("login_token",login_token);
        data.put("displayname",edt_username.getText().toString());

        Call<UserDisplayNameResponse> call = ApiClient.getClient().userDisplayName(data);
        call.enqueue(new Callback<UserDisplayNameResponse>() {
            @Override
            public void onResponse(Call<UserDisplayNameResponse> call, Response<UserDisplayNameResponse> response) {

                if (response.isSuccessful()) {
                    dismissLoadingDialog();

                    if (validateUser(mActivity,
                            response.body().getStatus(),
                            response.body().getMessage())) {
                        return;
                    }

                    Log.d("UsernameResponse", response.toString());
                    Pref.setValue(EditUsernameActivity.this,"DisplayName",edt_username.getText().toString());
                    Toast.makeText(EditUsernameActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();



                }
                else {
                    dismissLoadingDialog();
                    AlertUtils.showSimpleAlert(mActivity, mActivity.getString(R.string.server_error));
                }


            }

            @Override
            public void onFailure(Call<UserDisplayNameResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                dismissLoadingDialog();
            }
        });

    }
}
