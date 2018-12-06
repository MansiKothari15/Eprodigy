package com.bacancy.eprodigy.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import com.bacancy.eprodigy.R;


/**
 * CommonUtils class
 * <p/>
 * <p>
 * This is progress dialog utils class which manage show/hide progress dialog
 * </p>
 *
 * @author Sumeet Bhut
 * @version 1.0
 * @since 2016-11-30
 */
public class ProgressUtils {

    private ProgressDialog progressDialog;
    private int processCount;
    private Context context;

    public ProgressUtils(Context context)
    {
        this.context=context;
    }

    public void showProgressDialog(String message)
    {
        showProgressDialog(message,1);
    }
    /***
     * Show progress dialog
     * @param message Message
     * @param processCount Count of total processes.
     *                     Like if you want to do 2 tasks at a time then just call this showProgressDialog with processCount=2 and then call dismissProgressDialog() method at every task finish
     */
    public void showProgressDialog(String message, int processCount)
    {
        this.processCount =processCount;
        if(progressDialog==null)
        {
            progressDialog=new ProgressDialog(context);
            progressDialog.setTitle("");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(true);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                Drawable drawable = new ProgressBar(context).getIndeterminateDrawable().mutate();
                drawable.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent),
                        PorterDuff.Mode.SRC_IN);
                progressDialog.setIndeterminateDrawable(drawable);
            }

        }
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    /***
     * Update progress dialog message
     * @param message Message
     */
    public void setMessage(String message)
    {
        if(progressDialog!=null)
        {
            progressDialog.setMessage(message);
        }
    }

    /***
     * If you forcefully want dismiss progress dialog
     * @param isForce
     */
    public void dismissProgressDialog(boolean isForce)
    {
        if(isForce)
        {
            try
            {
                progressDialog.dismiss();
            }catch (Exception e)
            {

            }
        }
        else
        {
            dismissProgressDialog();
        }
    }

    /***
     * For dismiss progress dialog
     */
    public void dismissProgressDialog()
    {
        if(processCount >1)
        {
            processCount--;
        }
        else
        {
            try
            {
                progressDialog.dismiss();
            }catch (Exception e)
            {

            }
        }
    }
}
