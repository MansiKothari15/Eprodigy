package com.bacancy.eprodigy.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bacancy.eprodigy.R;


/**
 * CommonUtils class
 * <p/>
 * <p>
 * This is internet utils class which contains all methods regarding check and others internet access
 * </p>
 *
 * @author Sumeet Bhut
 * @version 1.0
 * @since 2016-10-15
 */
public class InternetUtils {
    /***
     * Check and return device internet status
     *
     * @param context Context
     * @return Internet connected status
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    /***
     * Show no internet alert
     *
     * @param context      Activity class
     * @param isForceClose Pass true, If want to force close app when user click on ok
     */
    public static void showInternetAlert(final Context context, final boolean isForceClose) {
        if (isForceClose) {
            AlertDialog dialog = new AlertDialog.Builder(context).setIcon(0).setTitle(context.getString(R.string.e_no_internet_title)).setMessage(context.getString(R.string.e_no_internet)).setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (isForceClose) {
                        ((Activity) context).finish();
                    }
                }
            }).show();
            AlertUtils.changeDefaultColor(dialog);

        } else {
            AlertUtils.showSimpleAlert(context, context.getString(R.string.e_no_internet));
        }
    }

    public static boolean checkAndShowAlert(final Context context){
        boolean isInternet=isNetworkConnected(context);
        if(!isInternet){
            showInternetAlert(context,false);
        }
        return isInternet;
    }
}
