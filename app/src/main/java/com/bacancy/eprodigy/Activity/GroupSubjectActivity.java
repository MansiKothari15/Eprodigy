package com.bacancy.eprodigy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bacancy.eprodigy.Adapters.CreateGroupAdapter;
import com.bacancy.eprodigy.R;

public class GroupSubjectActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_label,tv_right,tv_left,tv_back,tv_create;
    RecyclerView rv_createdGroup;
    CreateGroupAdapter createGroupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupsubject);
        init();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_label = (TextView)findViewById(R.id.tv_label);
        tv_right = (TextView)findViewById(R.id.tv_right);
        tv_left = (TextView)findViewById(R.id.tv_left);
        tv_back = (TextView)findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        tv_create = (TextView)findViewById(R.id.tv_create);
        tv_create.setOnClickListener(this);

        tv_label.setText("Add Users");
        hideCustomToolbar();

        rv_createdGroup = (RecyclerView)findViewById(R.id.rv_createdGroup);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        rv_createdGroup.setLayoutManager(mLayoutManager);
        rv_createdGroup.setItemAnimator(new DefaultItemAnimator());
        createGroupAdapter = new CreateGroupAdapter();
        rv_createdGroup.setAdapter(createGroupAdapter);

    }

    public void hideCustomToolbar(){
        tv_right.setVisibility(View.INVISIBLE);
        tv_left.setVisibility(View.INVISIBLE);
        tv_back.setVisibility(View.VISIBLE);
        tv_create.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_create:
                Intent i = new Intent(GroupSubjectActivity.this,MessagingActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }
}
