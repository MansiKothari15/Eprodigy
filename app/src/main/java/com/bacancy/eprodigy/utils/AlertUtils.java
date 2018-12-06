package com.bacancy.eprodigy.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bacancy.eprodigy.R;


/**
 * CommonUtils class
 * <p/>
 * <p>
 * This is util class for Alert dialog. All customization regarding alert are managed in this class
 * </p>
 *
 * @author Sumeet Bhut
 * @version 1.0
 * @since 2015-10-15
 */
public class AlertUtils {

	/***
	 * Show simple alert dialog with title 'Alert'
	 * @param context Activity context
	 * @param message Message
	 * @return AlertDialog
	 */
	public static AlertDialog showSimpleAlert(Context context, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(0);
        builder.setTitle("");
//		builder.setTitle(context.getString(R.string.alert));
		builder.setMessage(message);
		builder.setNeutralButton(context.getString(R.string.ok), null);

		AlertDialog dialog = builder.show();
		changeDefaultColor(dialog);
		return dialog;
	}


    /***
     * Show simple alert dialog
     * @param context Activity context
     * @param title Title
     * @param message Message
     * @param listener DialogInterface.OnClickListener for ok button click
     * @return AlertDialog
     */
    public static AlertDialog showSimpleAlert(Context context, String title, String message, final OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(0);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton(context.getString(R.string.ok), listener);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (listener != null) {
					listener.onClick(dialog, 0);
				}
			}
		});
        AlertDialog dialog = builder.show();
        changeDefaultColor(dialog);
        return dialog;
    }

    public static void showToast(Context context, String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }

    /***
     * Show alert dialog for confirm some action
     * @param context Activity context
     * @param title Title
     * @param message Message
     * @param onYesClick DialogInterface.OnClickListener for positive button click
     * @return AlertDialog
     */
	public static AlertDialog showConfirmAlert(Context context, String title,
                                               String message, OnClickListener onYesClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(0);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(context.getString(R.string.yes), onYesClick);
		builder.setNegativeButton(context.getString(R.string.no), null);

		AlertDialog dialog = builder.show();
		changeDefaultColor(dialog);
		return dialog;
	}

    public static AlertDialog showUpdateAppDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(0);
        builder.setTitle("Update App");
        builder.setMessage("A new version is available on Play Store. Do you want to update it now?");
        builder.setPositiveButton("Update Now", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        builder.setNegativeButton("Later", null);

        AlertDialog dialog = builder.show();
        changeDefaultColor(dialog);
        return dialog;
    }

    /***
     * Show alert dialog with list of options
     * @param context Activity context
     * @param title Title
     * @param items String array of all list options
     * @param onItemClick DialogInterface.OnClickListener for detect every item click
     * @return AlertDialog
     */
    public static AlertDialog showSingleChoiceListAlert(Context context, String title,
                                                        String[] items, int selected, final OnClickListener onItemClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(0);
        builder.setTitle(title);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,items);
        builder.setSingleChoiceItems(items, selected,new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if(dialog!=null){
                    dialog.dismiss();
                }
                if (onItemClick != null) {
                    onItemClick.onClick(dialog, which);
                }
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), null);

        AlertDialog dialog = builder.show();
        changeDefaultColor(dialog);
        return dialog;
    }

	/***
	 * Show alert dialog with list of options
	 * @param context Activity context
	 * @param title Title
	 * @param items String array of all list options
	 * @param onOkClickListener DialogInterface.OnClickListener for detect ok item click
	 * @return AlertDialog
	 */
	public static AlertDialog showMultiChoiceListAlert(Context context, String title,
                                                       String[] items, final boolean[] checkeItems,
                                                       final OnClickListener onOkClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(0);
		builder.setTitle(title);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,items);
		builder.setMultiChoiceItems(items, checkeItems, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				checkeItems[which]=isChecked;
			}
		});
		builder.setNegativeButton(context.getString(R.string.cancel), null);
		builder.setPositiveButton(context.getString(R.string.ok), onOkClickListener);
		AlertDialog dialog = builder.show();
		changeDefaultColor(dialog);
		return dialog;
	}

    /***
     * Show alert dialog with list of options
     * @param context Activity context
     * @param title Title
     * @param items String array of all list options
     * @param onItemClick DialogInterface.OnClickListener for detect every item click
     * @return AlertDialog
     */
	public static AlertDialog showListAlert(Context context, String title,
                                            String[] items, final OnClickListener onItemClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(0);
		builder.setTitle(title);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,items);
		builder.setAdapter(adapter, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (onItemClick != null) {
					onItemClick.onClick(dialog, which);
				}
			}
		});
		builder.setNegativeButton(context.getString(R.string.cancel), null);

		AlertDialog dialog = builder.show();
		changeDefaultColor(dialog);
		return dialog;
	}

    /***
     * Show alert dialog with list of options
     * @param context Activity context
     * @param title Title
     * @param adapter List adapter for show customized list row
     * @param onItemClick DialogInterface.OnClickListener for detect every item click
     * @return AlertDialog
     */
	public static AlertDialog showListAlert(Context context, String title,
                                            ListAdapter adapter, final OnClickListener onItemClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(0);
		builder.setTitle(title);
		builder.setAdapter(adapter, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(onItemClick!=null)
				{
					onItemClick.onClick(dialog, which);
				}
			}
		});
		builder.setNegativeButton(context.getString(R.string.cancel), null);

		AlertDialog dialog = builder.show();
		changeDefaultColor(dialog);
		return dialog;
	}

    /***
     * Change default color theme for Alert dialog
     * @param dialog AlertDialog
     */
	public static void changeDefaultColor(AlertDialog dialog) {
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				// only for gingerbread and newer versions
				Button b = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
				if (b != null)
					b.setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.colorAccent));

				b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
				if (b != null)
					b.setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.colorAccent));

				b = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
				if (b != null)
					b.setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.colorAccent));
			} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				ViewGroup decorView = (ViewGroup) dialog.getWindow()
						.getDecorView();
				FrameLayout windowContentView = (FrameLayout) decorView
						.getChildAt(0);
				FrameLayout contentView = (FrameLayout) windowContentView
						.getChildAt(0);
				LinearLayout parentPanel = (LinearLayout) contentView
						.getChildAt(0);
				LinearLayout topPanel = (LinearLayout) parentPanel
						.getChildAt(0);
				View titleDivider = topPanel.getChildAt(2);
				LinearLayout titleTemplate = (LinearLayout) topPanel
						.getChildAt(1);
				TextView alertTitle = (TextView) titleTemplate.getChildAt(1);

				int textColor = ContextCompat.getColor(dialog.getContext(), R.color.colorAccent);
				alertTitle.setTextColor(textColor);

				int primaryColor = ContextCompat.getColor(dialog.getContext(), R.color.colorAccent);
				titleDivider.setBackgroundColor(primaryColor);
			}

		} catch (Exception e) {
			// e.printStackTrace();

		}
	}


}
