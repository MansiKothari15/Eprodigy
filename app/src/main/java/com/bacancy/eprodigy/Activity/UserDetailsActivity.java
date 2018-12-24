package com.bacancy.eprodigy.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.ProfileUploadResponse;
import com.bacancy.eprodigy.ResponseModel.UserDisplayNameResponse;
import com.bacancy.eprodigy.utils.AlertUtils;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.utils.Pref;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailsActivity extends BaseActivity implements View.OnClickListener {
    Activity mActivity;
    Button btn_username;
    ImageView img_profile;
    private static final String IMAGE_DIRECTORY = "/eProdigy";
    private int GALLERY = 1, CAMERA = 2;
    EditText edt_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetail);

        mActivity=this;
        init();
    }

    private void init() {
        edt_username = (EditText)findViewById(R.id.edt_username);
        img_profile = (ImageView) findViewById(R.id.img_profile);
        btn_username = (Button)findViewById(R.id.btn_username);
        img_profile.setOnClickListener(this);
        btn_username.setOnClickListener(this);

        String ProfilePic = Pref.getValue(this,"ProfilePic","");
        if(!ProfilePic.equals("") && ProfilePic != null){
            Picasso.with(this).load(ProfilePic).into(img_profile);
        }
        String DisplayName = Pref.getValue(this,"DisplayName","");
        if (!DisplayName.equals("")){
            edt_username.setText(DisplayName);
        }
        Log.d("init--",ProfilePic+" "+DisplayName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_username:
                if(edt_username.getText().length() != 0){
                    updateUserDisplayName();
                }else {
                    Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.img_profile:
                showPictureDialog();
                break;
        }
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (ContextCompat.checkSelfPermission(UserDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(UserDetailsActivity.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                                }else {
                                    choosePhotoFromGallary();
                                }
                                break;
                            case 1:
                                if (ContextCompat.checkSelfPermission(UserDetailsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(UserDetailsActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                                }else {
                                    takePhotoFromCamera();
                                }

                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                choosePhotoFromGallary();
            }
        }else if(requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePhotoFromCamera();
            }
        }
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    img_profile.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(UserDetailsActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            img_profile.setImageBitmap(thumbnail);
            saveImage(thumbnail);
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            uploadProfile(f);
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void uploadProfile(File file){

        showLoadingDialog(this);

        String username = Pref.getValue(this, AppConfing.USERNAME, "");
        String login_token = Pref.getValue(this, AppConfing.LOGIN_TOKEN, "");
        RequestBody userName = RequestBody.create(MediaType.parse(""), username);
        RequestBody loginToken = RequestBody.create(MediaType.parse(""), login_token);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Log.d("Params->",userName.toString()+" "+loginToken.toString());

        Call<ProfileUploadResponse> call = ApiClient.getClient().profileUpload(userName,loginToken,body);
        call.enqueue(new Callback<ProfileUploadResponse>() {
            @Override
            public void onResponse(Call<ProfileUploadResponse> call, Response<ProfileUploadResponse> response) {

                if (response.isSuccessful()) {
                    if (validateUser(mActivity,
                            response.body().getStatus(),
                            response.body().getMessage())) {
                        return;
                    }
                    dismissLoadingDialog();
                    Log.d("ProfileResponse", response.toString());
                    String profilePic = response.body().getProfilepicture();
                    String displayName = response.body().getUserdata().getDisplayname();
                    Pref.setValue(UserDetailsActivity.this, "ProfilePic", profilePic);

                    Toast.makeText(UserDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                else {
                    dismissLoadingDialog();
                    AlertUtils.showSimpleAlert(mActivity, mActivity.getString(R.string.server_error));
                }
            }

            @Override
            public void onFailure(Call<ProfileUploadResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                dismissLoadingDialog();
            }
        });

    }


    private void updateUserDisplayName() {
        showLoadingDialog(this);

        String username = Pref.getValue(this, AppConfing.USERNAME, "");
        String login_token = Pref.getValue(this, AppConfing.LOGIN_TOKEN, "");

        HashMap<String, String> data = new HashMap<>();
        data.put("username",username);
        data.put("login_token",login_token);
        data.put("displayname",edt_username.getText().toString());

        Call<UserDisplayNameResponse> call = ApiClient.getClient().userDisplayName(data);
        call.enqueue(new Callback<UserDisplayNameResponse>() {
            @Override
            public void onResponse(Call<UserDisplayNameResponse> call, Response<UserDisplayNameResponse> response) {



                if (response.isSuccessful()) {


                    if (validateUser(mActivity,
                            response.body().getStatus(),
                            response.body().getMessage())) {
                        return;
                    }
                    dismissLoadingDialog();
                    Log.d("UsernameResponse", response.toString());
                    Pref.setValue(UserDetailsActivity.this,"DisplayName",edt_username.getText().toString());
                    Toast.makeText(UserDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(UserDetailsActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();



                }
                else {
                    dismissLoadingDialog();
                    AlertUtils.showSimpleAlert(mActivity, mActivity.getString(R.string.server_error));
                }
            }

            @Override
            public void onFailure(Call<UserDisplayNameResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                dismissLoadingDialog();
            }
        });

    }
}
