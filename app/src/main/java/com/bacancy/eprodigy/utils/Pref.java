package com.bacancy.eprodigy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Pref {

	private static SharedPreferences sharedPreferences = null;

	public static void openPref(Context context) {

		sharedPreferences = context.getSharedPreferences("ProjectName",
				Context.MODE_PRIVATE);

	}

	public static String getValue(Context context, String key,
                                  String defaultValue) {
		Pref.openPref(context);
		String result = Pref.sharedPreferences.getString(key, defaultValue);
		Pref.sharedPreferences = null;
		return result;
	}

	public static void setValue(Context context, String key, String value) {
		Pref.openPref(context);
		Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
		prefsPrivateEditor.putString(key, value);
		prefsPrivateEditor.commit();
		prefsPrivateEditor = null;
		Pref.sharedPreferences = null;
	}

	public static boolean getValueBoolean(Context context, String key,
                                          boolean defaultValue) {
		Pref.openPref(context);
		boolean result = Pref.sharedPreferences.getBoolean(key, defaultValue);
		Pref.sharedPreferences = null;
		return result;
	}

	public static void setValueBoolean(Context context, String key, boolean value) {
		Pref.openPref(context);
		Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
		prefsPrivateEditor.putBoolean(key, value);
		prefsPrivateEditor.commit();
		prefsPrivateEditor = null;
		Pref.sharedPreferences = null;
	}

	public static void setClear(Context context) {
		Pref.openPref(context);
		Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
		prefsPrivateEditor.clear().commit();
		prefsPrivateEditor = null;
		Pref.sharedPreferences = null;
	}

}