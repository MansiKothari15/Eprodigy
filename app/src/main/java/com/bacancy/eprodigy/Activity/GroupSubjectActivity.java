package com.bacancy.eprodigy.Activity;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.Adapters.CreateGroupAdapter;
import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.Models.GroupPojo;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.UpdateGroupDetailResponse;
import com.bacancy.eprodigy.db.DataManager;
import com.bacancy.eprodigy.utils.Constants;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.utils.Pref;
import com.bacancy.eprodigy.utils.SCUtils;
import com.bacancy.eprodigy.xmpp.XMPPHandler;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupSubjectActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_label, tv_right, tv_left, tv_back, tv_create;
    RecyclerView rv_createdGroup;
    CreateGroupAdapter createGroupAdapter;
    EditText edt_groupName;
    ImageView img_groupPic;
    private int REQUEST_PICK_GALLERY = 111, REQUEST_CAMERA_PICTURE = 222;
    private static final String IMAGE_DIRECTORY = "/eProdigy";
    File f;
    ArrayList<String> NameArrayList = new ArrayList<>();
    ArrayList<String> mCheckset = new ArrayList<>();
    List<LocalMedia> selectedImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupsubject);

        Bundle bundle = new Bundle(getIntent().getExtras());
        if (bundle != null) {
            mCheckset = (ArrayList<String>) getIntent().getSerializableExtra("checked_array_list");
            NameArrayList = (ArrayList<String>) getIntent().getSerializableExtra("name_array_list");
            LogM.e("GroupSubjectActivity  uname>>" + mCheckset.size());
            LogM.e("GroupSubjectActivity  name>>" + NameArrayList.size());
        }

        init();
    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_label = (TextView) findViewById(R.id.tv_label);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_back = (TextView) findViewById(R.id.tv_back);
        img_groupPic = (ImageView) findViewById(R.id.img_groupPic);
        tv_back.setOnClickListener(this);
        tv_create = (TextView) findViewById(R.id.tv_create);
        tv_create.setOnClickListener(this);
        img_groupPic.setOnClickListener(this);

        tv_label.setText("Add Users");
        hideCustomToolbar();

        edt_groupName = (EditText) findViewById(R.id.edt_groupName);
        rv_createdGroup = (RecyclerView) findViewById(R.id.rv_createdGroup);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_createdGroup.setLayoutManager(mLayoutManager);
        rv_createdGroup.setItemAnimator(new DefaultItemAnimator());
        createGroupAdapter = new CreateGroupAdapter(GroupSubjectActivity.this, NameArrayList);
        rv_createdGroup.setAdapter(createGroupAdapter);

    }

    public void hideCustomToolbar() {
        tv_right.setVisibility(View.INVISIBLE);
        tv_left.setVisibility(View.INVISIBLE);
        tv_back.setVisibility(View.VISIBLE);
        tv_create.setVisibility(View.VISIBLE);
    }

    public void uploadGroupDetail(final String groupId, final String gName) {

        showLoadingDialog(this);

        final String username = Pref.getValue(this, AppConfing.USERNAME, "");
        String login_token = Pref.getValue(this, AppConfing.LOGIN_TOKEN, "");

        String group_title = edt_groupName.getText().toString();

        RequestBody userName = RequestBody.create(MediaType.parse(""), username);
        RequestBody loginToken = RequestBody.create(MediaType.parse(""), login_token);
        RequestBody groupName = RequestBody.create(MediaType.parse(""), groupId);
        RequestBody groupTitle = RequestBody.create(MediaType.parse(""), group_title);

        File file = new File(selectedImageList.get(0).getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Log.d("Params->", userName.toString() + " " + loginToken.toString());

        Call<UpdateGroupDetailResponse> call = ApiClient.getClient().updateGroupDetail(userName, loginToken, groupName, groupTitle, body);
        call.enqueue(new Callback<UpdateGroupDetailResponse>() {
            @Override
            public void onResponse(Call<UpdateGroupDetailResponse> call, Response<UpdateGroupDetailResponse> response) {

                dismissLoadingDialog();
                Log.d("UpdateGroupDetailRes", response.toString());


                mCheckset.add(username);
                if (XMPPHandler.createRoom(gName, mCheckset, groupId)) {

                    Toast.makeText(GroupSubjectActivity.this, "Group Created", Toast.LENGTH_SHORT).show();

                    ChatPojo chatPojo=new ChatPojo();
                    chatPojo.setGroupId(groupId);
                    chatPojo.setGroupImage(response.body().getUserdata().getGroupimage());
                    chatPojo.setGroupName(response.body().getUserdata().getGroup_title());
                    chatPojo.setMsgMode(AppConfing.GROUP_CHAT_MSG_MODE);
                    chatPojo.setChatText(AppConfing.GROUP_GREETINGS);
                    DataManager.getInstance().AddChat(chatPojo);

                    GroupPojo groupPojo = new GroupPojo();
                    groupPojo.setGroupId(response.body().getUserdata().getGroupname());
                    groupPojo.setGroupTitle(response.body().getUserdata().getGroup_title());
                    groupPojo.setGroupName(response.body().getUserdata().getGroupname());
                    groupPojo.setGroupImage(response.body().getUserdata().getGroupimage());
                    groupPojo.setCreatedAt(response.body().getUserdata().getCreated_at());
                    groupPojo.setModifyAt(response.body().getUserdata().getModify_at());

                    DataManager.getInstance().AddGroup(groupPojo);
                }

               /* DataManager.getInstance()
                        .getAllGroup()
                        .observe(GroupSubjectActivity.this, new Observer<List<GroupPojo>>() {
                            @Override
                            public void onChanged(@Nullable List<GroupPojo> groupPojoList) {
                                if (groupPojoList != null)
                                    Log.d("groupPojoList", "" + groupPojoList.size());
                             *//*   if (chatListAdapter != null) {
                                    chatListAdapter.swapItems(userList);
                                    chatListAdapter.notifyDataSetChanged();
                                }*//*

                            }
                        });*/

                Intent i = new Intent(GroupSubjectActivity.this, MessagingActivity.class);
                startActivity(i);
                finish();

            }

            @Override
            public void onFailure(Call<UpdateGroupDetailResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                dismissLoadingDialog();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_create:


                if (edt_groupName.getText().length() != 0) {

                    String groupId = edt_groupName.getText().toString() + "_" + SCUtils.getCurrentTimeStamp2();
                    String grpName = edt_groupName.getText().toString();
                    uploadGroupDetail(groupId, grpName);

                } else {
                    Toast.makeText(this, "Group subject is required!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_groupPic:

                showPictureDialog();

                break;
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (ContextCompat.checkSelfPermission(GroupSubjectActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(GroupSubjectActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                                } else {
                                    choosePhotoFromGallary();
                                }
                                break;
                            case 1:
                                if (ContextCompat.checkSelfPermission(GroupSubjectActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(GroupSubjectActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                } else {
                                    takePhotoFromCamera();
                                }

                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {

        PictureSelector.create(GroupSubjectActivity.this)
                .openGallery(PictureMimeType.ofAll())
                .setOutputCameraPath("/" + getResources().getString(R.string.app_name) + "/media")
                .freeStyleCropEnabled(true)
                // .circleDimmedLayer(true)
                .selectionMode(PictureConfig.SINGLE)
                .showCropFrame(true)
                .showCropGrid(true)
                .rotateEnabled(true)
                .enableCrop(true)
                .compress(true)
                .minimumCompressSize(100)
                .forResult(REQUEST_PICK_GALLERY);


    }

    private void takePhotoFromCamera() {

        PictureSelector.create(GroupSubjectActivity.this)
                .openCamera(PictureMimeType.ofAll())
                .setOutputCameraPath("/" + getResources().getString(R.string.app_name) + "/media")
                .freeStyleCropEnabled(true)
                // .circleDimmedLayer(true)
                .showCropFrame(true)
                .showCropGrid(true)
                .rotateEnabled(true)
                .enableCrop(true)
                .compress(true)
                .minimumCompressSize(100)
                .forResult(REQUEST_CAMERA_PICTURE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if ((requestCode == REQUEST_PICK_GALLERY && resultCode == RESULT_OK) ||
                (requestCode == REQUEST_CAMERA_PICTURE && resultCode == RESULT_OK)) {
            if (data != null) {
                selectedImageList = PictureSelector.obtainMultipleResult(data);

                if (selectedImageList != null && selectedImageList.size() > 0) {
                    File imgFile = new File(selectedImageList.get(0).getPath());

                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        img_groupPic.setImageBitmap(myBitmap);

                    }
                }
            }

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
            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
