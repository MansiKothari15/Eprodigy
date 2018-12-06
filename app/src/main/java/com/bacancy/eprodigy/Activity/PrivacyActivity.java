package com.bacancy.eprodigy.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bacancy.eprodigy.R;

public class PrivacyActivity extends BaseActivity implements View.OnClickListener{

    TextView tv_label,tv_back,tv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        init();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_label = (TextView)findViewById(R.id.tv_label);
        tv_label.setText("Privacy");
        tv_back = (TextView)findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        tv_right = (TextView)findViewById(R.id.tv_right);
        hideCustomToolbar();
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
