package com.bacancy.eprodigy.custom_loader;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.bacancy.eprodigy.R;

public class CustomProgressDialog {
    AlertDialog dialog;


    public void showCustomDialog(Context context) {

        if (dialog!=null && dialog.isShowing())
        {
            return;
        }

        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(context);
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_loading_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    public void dismissCustomDialog()
    {
        if (dialog!=null && dialog.isShowing())
        {
            dialog.dismiss();
        }
    }
}
