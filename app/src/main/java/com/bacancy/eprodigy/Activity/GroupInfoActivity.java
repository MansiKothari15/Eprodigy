package com.bacancy.eprodigy.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bacancy.eprodigy.R;

public class GroupInfoActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_label, tv_right, tv_left, tv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupinfo);
        init();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_label = (TextView) findViewById(R.id.tv_label);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);

        tv_label.setText("Group Info");
        hideCustomToolbar();

    }

    public void hideCustomToolbar() {
        tv_right.setVisibility(View.INVISIBLE);
        tv_left.setVisibility(View.INVISIBLE);
        tv_back.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
