package com.bacancy.eprodigy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bacancy.eprodigy.R;

public class MainActivity extends AppCompatActivity {

    TextView tv_label,tv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_label = (TextView)findViewById(R.id.tv_label);
        tv_right = (TextView)findViewById(R.id.tv_right);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,MessagingActivity.class);
                startActivity(i);
            }
        });
    }
}
