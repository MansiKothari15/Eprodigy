package com.bacancy.eprodigy.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.Activity.AccountDetailActivity;
import com.bacancy.eprodigy.Activity.EditUsernameActivity;
import com.bacancy.eprodigy.Activity.MobileRegistrationActivity;
import com.bacancy.eprodigy.Activity.StatusActivity;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.LogoutResponse;
import com.bacancy.eprodigy.ResponseModel.ProfileUploadResponse;
import com.bacancy.eprodigy.ResponseModel.UserDetailResponse;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.utils.Pref;
import com.bacancy.eprodigy.utils.ProgressUtils;

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

public class SettingsFragment extends Fragment implements View.OnClickListener {

    TextView tv_logout,tv_chats,tv_account,tv_username,tv_phoneNo,tv_status;
    public ProgressUtils progressUtils ;
    ImageView img_edit,img_profile;
    RelativeLayout rv_status;
    private static final String IMAGE_DIRECTORY = "/eProdigy";
    private int GALLERY = 1, CAMERA = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressUtils = new ProgressUtils(getActivity());
        tv_username = (TextView)view.findViewById(R.id.tv_username);
        tv_phoneNo = (TextView)view.findViewById(R.id.tv_phoneNo);
        tv_status = (TextView)view.findViewById(R.id.tv_status);
        tv_logout = (TextView)view.findViewById(R.id.tv_logout);
        tv_chats = (TextView)view.findViewById(R.id.tv_chats);
        tv_account = (TextView)view.findViewById(R.id.tv_account);
        tv_logout.setOnClickListener(this);
        tv_chats.setOnClickListener(this);
        tv_account.setOnClickListener(this);
        img_edit = (ImageView)view.findViewById(R.id.img_edit);
        img_profile = (ImageView)view.findViewById(R.id.img_profile);
        img_edit.setOnClickListener(this);
        img_profile.setOnClickListener(this);
        rv_status = (RelativeLayout)view.findViewById(R.id.rv_status);
        rv_status.setOnClickListener(this);

        userDetails();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_logout:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                // set title
                alertDialogBuilder.setTitle("Alert!");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Do you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                logOut();
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();

                break;
            case R.id.tv_chats:
                break;
            case R.id.tv_account:
                Intent i = new Intent(getActivity(),AccountDetailActivity.class);
                startActivity(i);
                break;
            case R.id.img_edit:
                Intent ii = new Intent(getActivity(),EditUsernameActivity.class);
                startActivity(ii);
                break;
            case R.id.img_profile:
                showPictureDialog();
                break;
            case R.id.rv_status:
                Intent i3 = new Intent(getActivity(),StatusActivity.class);
                startActivity(i3);
                break;
        }
    }


    private void showPictureDialog(){
        android.support.v7.app.AlertDialog.Builder pictureDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
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
                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                                }else {
                                    choosePhotoFromGallary();
                                }
                                break;
                            case 1:
                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
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
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    img_profile.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
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
            MediaScannerConnection.scanFile(getActivity(),
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

        progressUtils.showProgressDialog("Please wait...");

        String username = Pref.getValue(getActivity(), AppConfing.USERNAME, "");
        String login_token = Pref.getValue(getActivity(), AppConfing.LOGIN_TOKEN, "");
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
                progressUtils.dismissProgressDialog();
                Log.d("ProfileResponse", response.toString());
                String profilePic = response.body().getProfilepicture();
                String displayName = response.body().getUserdata().getDisplayname();
                Pref.setValue(getActivity(),"ProfilePic",profilePic);

                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ProfileUploadResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                progressUtils.dismissProgressDialog();
            }
        });

    }

    private void userDetails(){

        progressUtils.showProgressDialog("Please wait...");
        String login_token = Pref.getValue(getActivity(),"login_token","");
        String username = Pref.getValue(getActivity(),"username","");

        HashMap<String, String> data = new HashMap<>();
        data.put("login_token",login_token);
        data.put("username",username);

        Call<UserDetailResponse> call = ApiClient.getClient().userDetails(data);
        call.enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {

                Log.d("UserDetailResponse", response.toString());
                progressUtils.dismissProgressDialog();
                tv_username.setText(response.body().getUserdata().getDisplayname());
                tv_phoneNo.setText(response.body().getUserdata().getPhoneNumber());
                tv_status.setText(response.body().getUserdata().getUserstatus());

            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                progressUtils.dismissProgressDialog();
            }
        });

    }


    private void logOut(){
        progressUtils.showProgressDialog("Please wait...");
        String login_token = Pref.getValue(getActivity(),"login_token","");
        String username = Pref.getValue(getActivity(),"username","");

        HashMap<String, String> data = new HashMap<>();
        data.put("login_token",login_token);
        data.put("username",username);

        Call<LogoutResponse> call = ApiClient.getClient().logout(data);
        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {

                Log.d("logoutResponse", response.toString());
                progressUtils.dismissProgressDialog();

                if (response.isSuccessful()) {
                    Pref.setValue(getActivity(),"verified","0");
                    Intent i = new Intent(getActivity(), MobileRegistrationActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }

            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                progressUtils.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        userDetails();
    }
}
