package com.bacancy.eprodigy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Sumeet Bhut on 15/07/2015.
 */
public class BitmapUtility {
    public static Bitmap decodeBitmapFromFilePath(String file_path, int reqWidth, int reqHeight)
    {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file_path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file_path, options);
    }

    public  static Bitmap decodeBitmapFromUri(Context mContext, Uri uri, int reqWidth, int reqHeight) {
        InputStream in = null;
        try {
            in = mContext.getContentResolver().openInputStream(uri);

            //Decode image size
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(in, null, options);
            in.close();

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            in = mContext.getContentResolver().openInputStream(uri);
            Bitmap b = BitmapFactory.decodeStream(in, null, options);
            in.close();

            return b;

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return null;
    }


    public static Bitmap getResizedBitmap(Context context, Uri uri, int maxSize) throws Exception {

        Bitmap image = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        int width = image.getWidth();
        int height = image.getHeight();
        Log.e("log_tag", width + " image " + height);
        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        Bitmap result= Bitmap.createScaledBitmap(image, width, height, true);
        try {
            image.recycle();
            System.gc();
        }catch (Exception e)
        {

        }
        return result;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        if(bm==null){
            return null;
        }

        int width = bm.getWidth();

        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION

        Matrix matrix = new Matrix();

        // RESIZE THE BIT MAP

        matrix.postScale(scaleWidth, scaleHeight);

        // RECREATE THE NEW BITMAP

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth)
            {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byte_arr = stream.toByteArray();
        // String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);

        return byte_arr;
    }

    public static Bitmap maskingRoundBitmap(Bitmap original)
    {
        int minWidth = Math.min(original.getWidth(), original.getHeight());
        Bitmap mask = Bitmap.createBitmap(minWidth, minWidth, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(mask);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(minWidth / 2, minWidth / 2, minWidth / 2, paint);

        Bitmap result = Bitmap.createBitmap(minWidth, minWidth, Bitmap.Config.ARGB_8888);

        Canvas mCanvas = new Canvas(result);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);

        paint.setXfermode(null);

        if (mask != null)
        {
            mask.recycle();
            mask = null;
        }
//	if (original != null)
//	{
//	    original.recycle();
//	    original = null;
//	}

        return result;
    }

    public static Bitmap getCorrectOrientationBitmap(String mImagePath)
    {
        if (mImagePath == null)
        {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(mImagePath);
        if (bitmap == null)
        {
            return null;
        }
        try
        {
            ExifInterface exif = new ExifInterface(mImagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.e("log_tag", "orientation: " + orientation);
            int angle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
            {
                angle = 90;
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
            {
                angle = 180;
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
            {
                angle = 270;
            }

            Log.d("Tag", "Angle: " + angle);

            Matrix mat = new Matrix();
            mat.postRotate(angle);

            if (angle != 0)
            {
                Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);

                bitmap.recycle();

                return bitmap2;
            }
            return bitmap;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }
}
