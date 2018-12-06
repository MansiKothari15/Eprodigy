package com.bacancy.eprodigy.utils;

import android.util.Log;

/**
 * CommonUtils class
 * <p/>
 * <p>
 * This is Log utils class which show log with tag 'log_tag'
 * </p>
 *
 * @author Sumeet Bhut
 * @version 1.0
 * @since 2015-11-30
 */
public class LogM {
	public static void e(String message) {
		Log.e("log_tag", message);
	}

	public static void w(String message) {
		Log.w("log_tag", message);
	}

	public static void i(String message) {
		Log.i("log_tag", message);
	}

	public static void v(String message) {
		Log.v("log_tag", message);
	}
}
