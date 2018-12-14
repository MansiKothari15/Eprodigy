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

public class CreateGroupActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_label,tv_right,tv_left,tv_next,tv_back;
    RecyclerView rv_group, rv_groupContacts;
    CreateGroupAdapter createGroupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategroup);
        init();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_label = (TextView)findViewById(R.id.tv_label);
        tv_right = (TextView)findViewById(R.id.tv_right);
        tv_left = (TextView)findViewById(R.id.tv_left);
        tv_next = (TextView)findViewById(R.id.tv_next);
        tv_back = (TextView)findViewById(R.id.tv_back);
        tv_next.setOnClickListener(this);
        tv_back.setOnClickListener(this);

        tv_label.setText("Add Users");
        hideCustomToolbar();

        rv_group = (RecyclerView)findViewById(R.id.rv_group);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        rv_group.setLayoutManager(mLayoutManager);
        rv_group.setItemAnimator(new DefaultItemAnimator());
        createGroupAdapter = new CreateGroupAdapter();
        rv_group.setAdapter(createGroupAdapter);

        rv_groupContacts = (RecyclerView)findViewById(R.id.rv_groupContacts);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        rv_groupContacts.setLayoutManager(mLayoutManager1);
        rv_groupContacts.setItemAnimator(new DefaultItemAnimator());
    }

    public void hideCustomToolbar(){
        tv_right.setVisibility(View.INVISIBLE);
        tv_left.setVisibility(View.INVISIBLE);
        tv_next.setVisibility(View.VISIBLE);
        tv_back.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_next:
                Intent i = new Intent(CreateGroupActivity.this,GroupSubjectActivity.class);
                startActivity(i);
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
