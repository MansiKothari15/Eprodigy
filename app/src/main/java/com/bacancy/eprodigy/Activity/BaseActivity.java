package com.bacancy.eprodigy.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.custom_loader.CustomProgressDialog;
import com.bacancy.eprodigy.permission.MyPermission;
import com.bacancy.eprodigy.permission.PermissionActivity;
import com.bacancy.eprodigy.permission.PermissionListener;
import com.bacancy.eprodigy.utils.ImageSelectUtils;
import com.bacancy.eprodigy.utils.InternetUtils;
import com.bacancy.eprodigy.utils.LogM;

import com.bacancy.eprodigy.utils.ProgressUtils;


/**
 * Created by sumeet on 5/10/17.
 */

public class BaseActivity extends AppCompatActivity {

    public ImageSelectUtils imageSelectUtils;
    PermissionActivity permissionActivity;
    public ProgressUtils progressUtils;
    public Activity activity;
    CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageSelectUtils = new ImageSelectUtils(BaseActivity.this);
        permissionActivity = new PermissionActivity(BaseActivity.this);
        progressUtils = new ProgressUtils(BaseActivity.this);

        customProgressDialog = new CustomProgressDialog();
        this.activity = this;
    }

    /**
     * @param mActivity
     * @param permissionlistener
     * @param isDenied           : for denied message ,if true then user must allow permission
     * @param permissions
     */
    public void initPermission(Activity mActivity, PermissionListener permissionlistener, boolean isDenied, String... permissions) {
        if (!isDenied) {
            MyPermission.with(mActivity)
                    .setPermissionListener(permissionlistener)
                    .setPermissions(permissions)
                    .check();
        } else {
            MyPermission.with(mActivity)
                    .setPermissionListener(permissionlistener)
                    // .setRationaleTitle(R.string.rationale_title) //confirmation dialog
                    //.setRationaleMessage(R.string.rationale_message)//confirmation dialog
                    .setDeniedTitle(mActivity.getResources().getString(R.string.denied_message))
                    .setDeniedMessage(
                            mActivity.getResources().getString(R.string.reject_message))
                    .setGotoSettingButtonText(mActivity.getResources().getString(R.string.btn_go_title))
                    .setPermissions(permissions)
                    .check();
        }

    }

    /**
     * @return true or false (if false show no internet dialog)
     */
    public boolean validateInternetConn(Activity mActivity) {
        if (InternetUtils.isNetworkConnected((mActivity))) {

            return true;
        } else {

            showNoInternetDialog(mActivity, false);
        }
        return false;
    }



    /**
     * no internet dialog
     */
    public void showNoInternetDialog(final Activity mActivity, final boolean needToFinishonOk) {
        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mActivity, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(mActivity);
        }

        builder.setTitle(mActivity.getResources().getString(R.string.no_internet_title))
                .setMessage(mActivity.getResources().getString(R.string.no_internet_msg))
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        if (needToFinishonOk) {
                            mActivity.finish();
                        }
                        //validateInternetConn();


                    }
                })
                /*  .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int which) {
                          // do nothing
                      }
                  })*/
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionActivity.onPermissionsResult(requestCode, permissions, grantResults);
        LogM.v("OnResult Base");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void showLoadingDialog(Activity mActivity) {
        if (customProgressDialog != null) {

            customProgressDialog.showCustomDialog(mActivity);
        }
    }

    public void dismissLoadingDialog() {
        if (customProgressDialog != null) {
            customProgressDialog.dismissCustomDialog();
        }
    }
}
