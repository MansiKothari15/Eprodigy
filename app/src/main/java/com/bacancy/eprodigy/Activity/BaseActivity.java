package com.bacancy.eprodigy.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bacancy.eprodigy.permission.PermissionActivity;
import com.bacancy.eprodigy.utils.ImageSelectUtils;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.utils.ProgressUtils;


/**
 * Created by sumeet on 5/10/17.
 */

public class BaseActivity extends AppCompatActivity {

    public ImageSelectUtils imageSelectUtils;
    PermissionActivity permissionActivity ;
    public ProgressUtils progressUtils ;
    public Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageSelectUtils = new ImageSelectUtils(BaseActivity.this);
        permissionActivity = new PermissionActivity(BaseActivity.this);
        progressUtils = new ProgressUtils(BaseActivity.this);

        this.activity = this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionActivity.onPermissionsResult(requestCode, permissions, grantResults);
        LogM.v("OnResult Base");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
