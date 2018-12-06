package com.bacancy.eprodigy.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.StatusUpdateResponse;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.utils.Pref;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusActivity extends BaseActivity implements View.OnClickListener{

    TextView tv_label,tv_back,tv_right,tv_status;
    ListView lv_status;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        init();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_label = (TextView)findViewById(R.id.tv_label);
        tv_label.setText("Status");
        tv_back = (TextView)findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        tv_right = (TextView)findViewById(R.id.tv_right);
        tv_status = (TextView)findViewById(R.id.tv_status);

        lv_status = (ListView)findViewById(R.id.lv_status);
        lv_status.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv_status.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, getResources().getStringArray(R.array.statuslist)));

//        listAdapter= new com.bacancy.eprodigy.Adapters.ListAdapter(StatusActivity.this,getResources().getStringArray(R.array.statuslist));
//        lv_status.setAdapter(listAdapter);

        lv_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = lv_status.getItemAtPosition(i).toString();
                tv_status.setText(text);
                statusUpdate();
            }
        });

        hideCustomToolbar();
    }

    private void statusUpdate(){
        progressUtils.showProgressDialog("Please wait...");

        String username = Pref.getValue(this, AppConfing.USERNAME, "");
        String login_token = Pref.getValue(this, AppConfing.LOGIN_TOKEN, "");

        HashMap<String, String> data = new HashMap<>();
        data.put("username",username);
        data.put("login_token",login_token);
        data.put("userstatus",tv_status.getText().toString());

        Call<StatusUpdateResponse> call = ApiClient.getClient().statusUpdate(data);
        call.enqueue(new Callback<StatusUpdateResponse>() {
            @Override
            public void onResponse(Call<StatusUpdateResponse> call, Response<StatusUpdateResponse> response) {
                progressUtils.dismissProgressDialog();
                Log.d("StatusUpdateResponse", response.toString());

            }

            @Override
            public void onFailure(Call<StatusUpdateResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                progressUtils.dismissProgressDialog();
            }
        });
    }

    public void hideCustomToolbar(){
        tv_right.setVisibility(View.INVISIBLE);
        tv_back.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
