package com.bacancy.eprodigy.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CommonUtils class
 * <p/>
 * <p>
 * This is util class for String
 * </p>
 *
 * @author Sumeet Bhut
 * @version 1.0
 * @since 2016-10-15
 */
public class StringUtils {

    public static boolean isEmpty(String string)
    {
        return string==null || string.length()<=0;
    }
    public static boolean isServerUrl(String url){return !StringUtils.isEmpty(url) && url.startsWith("http:");}

    public final static boolean isEmailInValid(CharSequence target) {
        return !(!TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public final static boolean isPasswordInValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() < 6;
    }

    public static String getQuantityString(int count, String word) {
        switch(count) {
            case 0:
                return "No "+word+"s";
            case 1:
                return "1 "+word;
            default:
                return count+" "+word+"s";
        }
    }

    public static SpannableString getKeyValueSpan(String key, String value)
    {
        if(!key.substring(key.length()-2).equals(": ")) {
            key += ": ";
        }
        SpannableString span=new SpannableString(key+value);
        span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0,key.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        return span;
    }

    public static String formatDouble(double num)
    {
        return String.format(Locale.ENGLISH,"%.2f",num);
    }

    public static String format1PDouble(double num)
    {
        return String.format(Locale.ENGLISH,"%.1f",num);
    }
    public static void doUnderline(TextView tv) {
        SpannableString span = new SpannableString(tv.getText().toString());
        span.setSpan(new UnderlineSpan(), 0, tv.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(span);
    }
    public static String removeAmPm(String orgStr)
    {
        if (!TextUtils.isEmpty(orgStr))
        {
            String s=orgStr.replace("AM","");
            return  s.replace("PM","");
        }
        return "Not Available";
    }
    public static boolean isEmailValid(String email) {
        Matcher matcher = null;
        try {
            Pattern pattern;
            final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(email);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return matcher.matches();
    }
}
