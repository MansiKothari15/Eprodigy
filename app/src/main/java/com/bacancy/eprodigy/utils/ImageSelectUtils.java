package com.bacancy.eprodigy.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * Created by Sumeet Bhut on 1/6/2016.
 */
public class ImageSelectUtils {
    private  final int ACTION_REQUEST_CAMERA = 1001;
    private final int ACTION_REQUEST_CROP = 1002;
    private final int ACTION_REQUEST_GALLERY = 1003;

    private Uri mImageCaptureUri, mImageCropUri;

    private Activity mActivity;
    private OnImageSelectListener mListener;

    public interface OnImageSelectListener
    {
        public void onSelect(Bitmap bitmap, byte[] bytes, String filePath);
        public void onError();
    }

    public ImageSelectUtils(Activity activity){
        mActivity =activity;
    }

    public void selectImage(OnImageSelectListener listener)
    {
        mListener=listener;
        showSelectOptionDialog();
    }

    public void selectImageCamera(OnImageSelectListener listener){
        mListener=listener;
        captureImageFromCamera();
    }

    public void selectImageGallery(OnImageSelectListener listener){
        mListener=listener;
        selectImageFromGallery();
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        mActivity.startActivityForResult(Intent.createChooser(intent, "Complete action using"), ACTION_REQUEST_GALLERY);

    }

    private void captureImageFromCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp_" + String.valueOf(System.currentTimeMillis()) + ".png"));
//
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

        try {
            intent.putExtra("return-data", true);

            mActivity.startActivityForResult(intent, ACTION_REQUEST_CAMERA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            if(mListener!=null){
                mListener.onError();
            }
        }
    }

    private void showSelectOptionDialog() {

        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setIcon(0);
        builder.setTitle("Select Image");
        builder.setNegativeButton("Cancel", null);

        CharSequence[] items = {"Take from camera", "Select from gallery"};
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                if (which == 0) {
                    captureImageFromCamera();
                } else if (which == 1) {
                    selectImageFromGallery();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        AlertUtils.changeDefaultColor(dialog);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case ACTION_REQUEST_CAMERA:
                afterGetPhoto(mImageCaptureUri,false, true);
                break;

            case ACTION_REQUEST_GALLERY:
                mImageCaptureUri = data.getData();
                afterGetPhoto(mImageCaptureUri,true, true);
                break;

            case ACTION_REQUEST_CROP:
                afterGetPhoto(mImageCropUri,false, false);
                break;

        }
    }

    private void doCrop() {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            cropIntent.setDataAndType(mImageCaptureUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 300);
            cropIntent.putExtra("outputY", 300);
            cropIntent.putExtra("return-data", true);

            mImageCropUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp_" + String.valueOf(System.currentTimeMillis()) + ".png"));
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCropUri);
            mActivity.startActivityForResult(cropIntent, ACTION_REQUEST_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            Log.e("log_tag", "No activity for crop in device");
            afterGetPhoto(mImageCaptureUri, false,true);
        }
    }

    private void afterGetPhoto(Uri uri, boolean fromGallery, boolean needResized) {
        try {
            Bitmap bitmapProfileGallery;
            if (needResized) {
                bitmapProfileGallery = BitmapUtility.getResizedBitmap(mActivity, uri, 300);
            } else {
                bitmapProfileGallery = BitmapFactory.decodeStream(mActivity.getContentResolver().openInputStream(uri));
            }
            Log.e("log_tag", bitmapProfileGallery.getWidth() + " result " + bitmapProfileGallery.getHeight());

            byte[] bytesProfile = BitmapUtility.convertBitmapToByteArray(bitmapProfileGallery);

            String path= SDCardUtils.saveBitmapOnExternalCache(mActivity,"Image_"+ String.valueOf(System.currentTimeMillis()) +".png",bytesProfile);

            if(mListener!=null)
            {
                mListener.onSelect(bitmapProfileGallery, bytesProfile, path);
            }
        } catch (Exception e) {
            Log.e("log_tag", "Error at get bitmap");
            e.printStackTrace();
            if(mListener!=null){
                mListener.onError();
            }
        }
    }



    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
