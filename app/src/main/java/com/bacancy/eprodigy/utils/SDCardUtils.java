package com.bacancy.eprodigy.utils;

import android.content.Context;
import android.os.Environment;

import com.bacancy.eprodigy.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Sumeet Bhut on 1/6/2016.
 */
public class SDCardUtils {

    public static byte[] getBytesFromSDCardFile(String path) {
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
            return bytes;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    /***
     * This method helps save bitmap on SD card
     * @param fileName Filename
     * @param bytes bytes array
     * @return String(file path)
     */
    public static String saveBitmapOnExternalCache(Context context, String fileName, byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        File folder = new File(context.getExternalCacheDir(), "Images");
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        File file = new File(folder, fileName);
        if (file.exists()) file.delete();

        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            out.write(bytes);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /***
     * This method helps save bitmap on SD card
     * @param fileName Filename
     * @param bytes bytes array
     * @return String(file path)
     */
    public static String saveBitmapOnExternalStorage(Context context, String fileName, byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        File folder = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name));
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        File file = new File(folder, fileName);
        if (file.exists()) file.delete();

        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            out.write(bytes);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
