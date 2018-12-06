package com.bacancy.eprodigy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bacancy.eprodigy.R;

public class CreateGroupActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_label,tv_right,tv_left,tv_next;
    RecyclerView rv_group;

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
        tv_next.setOnClickListener(this);

        tv_label.setText("Add Users");
        hideCustomToolbar();

        rv_group = (RecyclerView)findViewById(R.id.rv_group);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_group.setLayoutManager(mLayoutManager);
        rv_group.setItemAnimator(new DefaultItemAnimator());
    }

    public void hideCustomToolbar(){
        tv_right.setVisibility(View.INVISIBLE);
        tv_left.setVisibility(View.INVISIBLE);
        tv_next.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_next:
                Intent i = new Intent(CreateGroupActivity.this,GroupSubjectActivity.class);
                startActivity(i);
                break;
        }
    }
}
