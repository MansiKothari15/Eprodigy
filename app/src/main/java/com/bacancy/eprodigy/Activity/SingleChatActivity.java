package com.bacancy.eprodigy.Activity;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.Adapters.ChatAdapter;
import com.bacancy.eprodigy.Models.ChatMediaModel;
import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.Models.ChatStateModel;
import com.bacancy.eprodigy.Models.PresenceModel;
import com.bacancy.eprodigy.Models.UserLocation;
import com.bacancy.eprodigy.MyApplication;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.LastSeenResponse;
import com.bacancy.eprodigy.ResponseModel.MediaUploadResponse;
import com.bacancy.eprodigy.db.DataManager;
import com.bacancy.eprodigy.permission.PermissionListener;
import com.bacancy.eprodigy.utils.AlertUtils;
import com.bacancy.eprodigy.utils.Constants;
import com.bacancy.eprodigy.utils.ImageSelectUtils;
import com.bacancy.eprodigy.utils.InternetUtils;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.utils.Pref;
import com.bacancy.eprodigy.utils.SCUtils;
import com.bacancy.eprodigy.xmpp.XMPPEventReceiver;
import com.bacancy.eprodigy.xmpp.XMPPHandler;
import com.bacancy.eprodigy.xmpp.XMPPService;
import com.bacancy.eprodigy.xmpp.XmppCustomEventListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;


import org.jivesoftware.smack.SmackException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SingleChatActivity extends BaseActivity implements View.OnClickListener {
    Activity mActivity;
      PopupMenu popup;
    private ImageSelectUtils imageSelectUtils;
    TextView tv_label, tv_newMessage, tv_createGroup, tv_back, tv_lastseen;
    RecyclerView rv_singleChat;
    ImageView img_profile, img_add, imgSend, img_camera, img_audio;
    EditText edtMessage;
    ChatAdapter mMessageAdapter;
    String ChatUserId, mName="";
    ArrayList<ChatPojo> chatPojoArrayList = new ArrayList<ChatPojo>();
    private static final String IMAGE_DIRECTORY = "/eProdigyMedia";

    //Chat App to start services etc
    MyApplication mChatApp = MyApplication.getInstance();

    //Get our custom event receiver so that we can bind our event listener to it


    private static final int REQUEST_CAMERA_PICTURE = 1, REQUEST_PICK_GALLERY = 2, REQUEST_SHARE_CONTACT = 3, REQUEST_PLACE_PICKER = 4, REQUEST_PICK_VIDEO = 5;
    private Uri imageUri;
    private List<ChatPojo> conversation_ArrayList = new ArrayList<>();
    private String selectedImagePath = "";
    List<LocalMedia> listLocalMedia = new ArrayList<>();

    private String sharedContactSenderNumber = "";
    private String sharedContactSenderName = "";
    private String sharedContactSenderImage = "";
    UserLocation userLocation = null;
    String AudioSavePathInDevice = "";
    MediaRecorder mediaRecorder;
    String RandomAudioFileName = "ABCDEFGHIJKLM";
    MediaPlayer mediaPlayer;
    Random random;

    private boolean ifshow = false;



    public XmppCustomEventListener xmppCustomEventListener = new XmppCustomEventListener() {

        //Event Listeners
        public void onNewMessageReceived(ChatPojo chatPojo) {

            Log.e("ad", "onNewMessageReceived>>" + chatPojo.toString());

            chatPojo.setShowing(true);
            chatPojo.setMine(false);
            DataManager.getInstance().AddChat(chatPojo);

            LogM.e("onNewMessageReceived ChatActivity");

            if (ifshow) BaseActivity.SendNotification(SingleChatActivity.this, chatPojo);

        }

        @Override
        public void onPresenceChanged(PresenceModel presenceModel) {
//            final String presence = com.coinasonchatapp.app.utils.Utils.getStatusMode(presenceModel.getUserStatus());
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    txtUserStatus.setText(presence);
//                    LogM.e("onPresenceChanged" + presence);
//                }
//            });
        }


        //On Chat Status Changed
        public void onChatStateChanged(ChatStateModel chatStateModel) {

//            String chatStatus = com.coinasonchatapp.app.utils.Utils.getChatMode(chatStateModel.getChatState());
            LogM.e("chatStatus --- onChatStateChanged");
            if (MyApplication.getmService().xmpp.checkSender(username, chatStateModel.getUser())) {
                //  chatStatusTv.setText(chatStatus);
                LogM.e("onChatStateChanged");
            }
        }

        @Override
        public void onConnected() {

            username = Pref.getValue(SingleChatActivity.this, "username", "");
            password = Pref.getValue(SingleChatActivity.this, "password", "");

            Log.d("startXmppService","login-onConnected="+username + " " + password);

            xmppHandler = MyApplication.getmService().xmpp;
            xmppHandler.setUserPassword(username, password);

            if (!xmppHandler.loggedin)
                new XMPPHandler.LoginTask(mActivity,username,password);
           // xmppHandler.login();
        }

        public void onLoginFailed() {
            xmppHandler.disconnect();
            Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_singlechat);
        mActivity = this;

        startXmppService(mActivity);
        xmppEventReceiver = mChatApp.getEventReceiver();

        imageSelectUtils = new ImageSelectUtils(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mName = extras.getString("name");
            ChatUserId = extras.getString("receiverJid");

        }
        init();

        /*KeyboardVisibilityEvent.setEventListener(
                SingleChatActivity.this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        LinearLayoutManager layoutManager = ((LinearLayoutManager) rv_singleChat.getLayoutManager());
                        int firstVisiblePosition = layoutManager.findLastVisibleItemPosition();
                        if ((firstVisiblePosition >= 0)
                                && (firstVisiblePosition < mMessageAdapter.getItemCount())) {
                            ChatPojo item =
                                    mMessageAdapter.getItem(firstVisiblePosition);
                            if (item != null && conversation_ArrayList != null && conversation_ArrayList.size() > 0) {
                                conversation_ArrayList.get(
                                        conversation_ArrayList.size() - 1);
                                rv_singleChat.smoothScrollToPosition(conversation_ArrayList.size() - 1);
                            }
                        }
                    }
                });*/


    }

    private void init() {



        Log.d("Login init-", username + " " + password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        random = new Random();

        tv_label = (TextView) findViewById(R.id.tv_label);
        tv_newMessage = (TextView) findViewById(R.id.tv_right);
        tv_createGroup = (TextView) findViewById(R.id.tv_left);
        tv_lastseen = (TextView) findViewById(R.id.tv_lastseen);
        tv_back = (TextView) findViewById(R.id.tv_back);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        img_profile = (ImageView) findViewById(R.id.img_profile);
        img_camera = (ImageView) findViewById(R.id.img_camera);
        img_add = (ImageView) findViewById(R.id.img_add);
        imgSend = (ImageView) findViewById(R.id.imgSend);


        rv_singleChat = (RecyclerView) findViewById(R.id.rv_singleChat);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager.setReverseLayout(true); //reverse RecyclerView because we need reversible data for category
        mLayoutManager.setStackFromEnd(true);
        rv_singleChat.setLayoutManager(mLayoutManager);
        rv_singleChat.setHasFixedSize(true);


        tv_back.setOnClickListener(this);
        img_add.setOnClickListener(this);
        imgSend.setOnClickListener(this);
        img_camera.setOnClickListener(this);
        img_audio = (ImageView) findViewById(R.id.img_audio);


        tv_label.setText(mName);

        img_audio.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    Toast.makeText(SingleChatActivity.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }
                return true;
            }

        });

        img_audio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.onTouchEvent(motionEvent);

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    if (mediaRecorder != null)
                        mediaRecorder.stop();
                    sendAudio();

                }
                return true;
            }
        });


        hideCustomToolbar();

        mMessageAdapter = new ChatAdapter(this, new ArrayList<ChatPojo>(), username);
        rv_singleChat.setAdapter(mMessageAdapter);

        if (mMessageAdapter.getItemCount() > 2)
            rv_singleChat.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);


        edtMessage.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                if (view.getId() == R.id.edtMessage) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
        LoadData();
    }

    public void sendAudio() {
//        sendMsg(Constants.TYPE_AUDIO);
        listLocalMedia=new ArrayList<>();
        LocalMedia localMedia=new LocalMedia();
        localMedia.setPath(AudioSavePathInDevice);
        localMedia.setPictureType("audio/*");//audio/mpeg
        listLocalMedia.add(localMedia);
        mediaUpload(Constants.TYPE_AUDIO);
    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(SingleChatActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, Constants.TYPE_AUDIO);
    }

    public String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void LoadData() {

        ChatUserId = ChatUserId.replace(" ", "");

        DataManager.getInstance().showingmsgUser(ChatUserId);

        DataManager.getInstance().getAll(ChatUserId).observe(SingleChatActivity.this, new Observer<List<ChatPojo>>() {
            @Override
            public void onChanged(@Nullable List<ChatPojo> chatPojos) {

                if (mMessageAdapter != null && chatPojos != null) {
                    mMessageAdapter.swapItems(chatPojos);
                    if (mMessageAdapter.getItemCount() > 2)
                        rv_singleChat.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);

                    conversation_ArrayList = chatPojos;

                }

            }
        });
    }


    private void mediaUpload(final int msgType) {

        showLoadingDialog(mActivity);

        String username = Pref.getValue(this, AppConfing.USERNAME, "");
        String login_token = Pref.getValue(this, AppConfing.LOGIN_TOKEN, "");
        int mCount = listLocalMedia != null ? listLocalMedia.size() : 0;

        RequestBody userName = RequestBody.create(MediaType.parse(""), username);
        RequestBody loginToken = RequestBody.create(MediaType.parse(""), login_token);
        RequestBody mediaCount = RequestBody.create(MediaType.parse(""), String.valueOf(mCount));


        MultipartBody.Part[] chatImagesParts = new MultipartBody.Part[listLocalMedia.size()];
        //MIME TYPE-http://blog.icodejava.com/tag/mime-type-multipartform-data/
        for (int index = 0; index < listLocalMedia.size(); index++) {

            //validate path
            String filePath = !TextUtils.isEmpty(listLocalMedia.get(index).getCompressPath())
                    ? listLocalMedia.get(index).getCompressPath()
                    : listLocalMedia.get(index).getPath();
            if (TextUtils.isEmpty(filePath)) {
                Log.d(TAG, "path is empty");
                return;
            }
            //
            Log.d(TAG, "requestUpload:" + index + "  " + filePath);

            File file = new File(filePath);
          //  String fileMimeType = SCUtils.getMimeTypeFomFile(file);
            Log.e("ad", "mime type=" + listLocalMedia.get(index).getPictureType());

            RequestBody mBody = RequestBody.create(MediaType.parse(listLocalMedia.get(index).getPictureType()), file);

            chatImagesParts[index] = MultipartBody.Part.createFormData("media" + (index + 1), file.getName(), mBody);

        }

        Call<MediaUploadResponse> call = ApiClient.getClient().mediaUpload(userName, loginToken, mediaCount, chatImagesParts);
        call.enqueue(new Callback<MediaUploadResponse>() {
            @Override
            public void onResponse(Call<MediaUploadResponse> call, Response<MediaUploadResponse> response) {
                dismissLoadingDialog();

                Log.d("MediaUploadResponse", response.toString());

                if (response.isSuccessful()) {
                    if (validateUser(mActivity,
                            response.body().getStatus(),
                            response.body().getMessage())) {
                        return;
                    }
                    if (response.body().getStatus() == Constants.RESPONSE_SUCCESS_STATUS) {
                        List<String> mediaUrlList = response.body().getMediaurl();
                        if (mediaUrlList != null && mediaUrlList.size() > 0) {

                            for (int i = 0; i < mediaUrlList.size(); i++) {
                                String filePath = !TextUtils.isEmpty(listLocalMedia.get(i).getCompressPath())
                                        ? listLocalMedia.get(i).getCompressPath()
                                        : listLocalMedia.get(i).getPath();
                                if (TextUtils.isEmpty(filePath) && TextUtils.isEmpty(listLocalMedia.get(i).getCutPath())) {
                                    Log.d(TAG, "path is empty");
                                    return;
                                }

                                selectedImagePath = filePath;

                                final String url = mediaUrlList.get(i);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!TextUtils.isEmpty(url))
                                            sendMediaMsg(msgType, url);
                                    }
                                });
                            }
                        }

                    } else {
                        dismissLoadingDialog();
                        String msg = response.body().getMessage();
                        AlertUtils.showSimpleAlert(mActivity, TextUtils.isEmpty(msg) ? mActivity.getString(R.string.server_error) : msg);
                    }

                } else {
                    dismissLoadingDialog();
                    AlertUtils.showSimpleAlert(mActivity, mActivity.getString(R.string.server_error));
                }

            }

            @Override
            public void onFailure(Call<MediaUploadResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                dismissLoadingDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Here we bind our event listener (XmppCustomEventListener)
        xmppEventReceiver.setListener(xmppCustomEventListener);
        ifshow = false;

    }


    public void hideCustomToolbar() {
        tv_newMessage.setVisibility(View.INVISIBLE);
        tv_createGroup.setVisibility(View.INVISIBLE);
        tv_back.setVisibility(View.VISIBLE);
        img_profile.setVisibility(View.VISIBLE);
//        tv_lastseen.setVisibility(View.VISIBLE);
    }

    private void getLastSeen() {

        showLoadingDialog(this);

        String username = Pref.getValue(this, AppConfing.USERNAME, "");
        String login_token = Pref.getValue(this, AppConfing.LOGIN_TOKEN, "");

        HashMap<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("login_token", login_token);

        Call<LastSeenResponse> call = ApiClient.getClient().lastSeen(data);
        call.enqueue(new Callback<LastSeenResponse>() {
            @Override
            public void onResponse(Call<LastSeenResponse> call, Response<LastSeenResponse> response) {

                dismissLoadingDialog();
                Log.d("LastSeenResponse", response.toString());

            }

            @Override
            public void onFailure(Call<LastSeenResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                dismissLoadingDialog();
            }
        });
    }



    private void sendMsg(int msgType) {

        if (!InternetUtils.isNetworkConnected(SingleChatActivity.this)) {
            AlertUtils.showSimpleAlert(SingleChatActivity.this, getResources().getString(R.string.e_no_internet));
            return;
        }


        ChatUserId = ChatUserId.replace(" ", "");
        Log.d("ChatUserId", ChatUserId);

        ChatPojo chatPojo = new ChatPojo();
        chatPojo.setChatId(ChatUserId);//to
        chatPojo.setChatSender(username);//from
        chatPojo.setChatRecv(ChatUserId);
        chatPojo.setShowing(true);
        chatPojo.setChatTimestamp(SCUtils.getNow());
        chatPojo.setMsgType(msgType);
        chatPojo.setMine(true);


        switch (msgType) {
            case Constants.TYPE_MESSAGE:
                if (TextUtils.isEmpty(edtMessage.getText().toString().trim())) {
                    Toast.makeText(this, "Please enter message", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    chatPojo.setChatText(edtMessage.getText().toString().trim());//message
                }
                break;

            case Constants.TYPE_IMAGE:

                if (listLocalMedia != null && listLocalMedia.size() == 0 && TextUtils.isEmpty(selectedImagePath)) {
                    Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    chatPojo.setChatImage(selectedImagePath);
                }
                break;

            case Constants.TYPE_CONTACT:
                if (TextUtils.isEmpty(sharedContactSenderName) || TextUtils.isEmpty(sharedContactSenderNumber)) {
                    Toast.makeText(this, "Please select contact", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    chatPojo.setSharedContactSenderName(sharedContactSenderName);
                    chatPojo.setSharedContactSenderNumber(sharedContactSenderNumber);
                    chatPojo.setSharedContactSenderImage(sharedContactSenderImage);
                }
                break;

            case Constants.TYPE_AUDIO:
                if (TextUtils.isEmpty(AudioSavePathInDevice)) {

                    Toast.makeText(this, "Long press to record your audio", Toast.LENGTH_SHORT).show();

                    return;
                } else {
                    chatPojo.setSendAudioPath(AudioSavePathInDevice);
                }
                break;
            case Constants.TYPE_LOCATION:

                if (userLocation != null) {
                    chatPojo.setLocationAddressTitle(userLocation.getAddressTitle());
                    chatPojo.setLocationAddressDesc(userLocation.getAddressDesc());
                    chatPojo.setLocationAddressLatitude(userLocation.getAddressLatitude());
                    chatPojo.setLocationAddressLongitude(userLocation.getAddressLongitude());
                }

                break;
           /* default:
                if (edtMessage.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please enter message", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    chatPojo.setChatText(edtMessage.getText().toString().trim());//message
                }
                break;*/
        }


        try {
            if (MyApplication.getmService().xmpp.sendMessage(chatPojo)) {
                DataManager.getInstance().AddChat(chatPojo);
                edtMessage.setText("");

                if (mMessageAdapter.getItemCount() > 2)
                    rv_singleChat.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
            }

        } catch (SmackException e) {
            e.printStackTrace();
            Toast.makeText(SingleChatActivity.this, "SmackException=" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void sendMediaMsg(int msgType, String url) {

        if (!InternetUtils.isNetworkConnected(SingleChatActivity.this)) {
            AlertUtils.showSimpleAlert(SingleChatActivity.this, getResources().getString(R.string.e_no_internet));
            return;
        }


        ChatUserId = ChatUserId.replace(" ", "");
        Log.d("ChatUserId", ChatUserId);

        ChatPojo chatPojo = new ChatPojo();
        chatPojo.setChatId(ChatUserId);//to
        chatPojo.setChatSender(username);//from
        chatPojo.setChatRecv(ChatUserId);
        chatPojo.setShowing(true);
        chatPojo.setChatTimestamp(SCUtils.getNow());
        chatPojo.setMsgType(msgType);
        chatPojo.setMine(true);

        if (msgType == Constants.TYPE_IMAGE) {
            chatPojo.setChatImage(selectedImagePath);
        }
        if (msgType == Constants.TYPE_AUDIO) {
            chatPojo.setSendAudioPath(AudioSavePathInDevice);
        }



         if (msgType == Constants.TYPE_IMAGE || msgType == Constants.TYPE_AUDIO || msgType == Constants.TYPE_VIDEO) {
            if (TextUtils.isEmpty(url)) {
                Toast.makeText(this, "Media url empty", Toast.LENGTH_SHORT).show();
            } else {
                chatPojo.setChatText(url);
            }
        }


        try {
            if (MyApplication.getmService().xmpp.sendMessage(chatPojo)) {
                DataManager.getInstance().AddChat(chatPojo);
                edtMessage.setText("");

                if (mMessageAdapter.getItemCount() > 2)
                    rv_singleChat.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
            }

        } catch (SmackException e) {
            e.printStackTrace();
            Toast.makeText(SingleChatActivity.this, "SmackException=" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data); // no need for super
        imageSelectUtils.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PICK_GALLERY:
                if (resultCode == RESULT_OK) {
                    listLocalMedia = new ArrayList<>();
                    listLocalMedia = PictureSelector.obtainMultipleResult(data);

                    // For example, LocalMedia returns three paths
                    //EXTENSION OPTIONS
                    // 1.media.getPath(); Path to the original
                    // 2.media.getCutPath();To crop the path, you need to determine whether media.isCut(); is true. Note: except audio and video.
                    // 3.media.getCompressPath();o compress the path, you need to determine whether media.isCompressed(); is true. Note: except for audio and video
                    // If it is cropped and compressed, take the compression path as the first, then cut and compress

                    Log.e("ad", "REQUEST_PICK_GALLERY =" + listLocalMedia.size());

                    mediaUpload(Constants.TYPE_IMAGE);

                }

                break;
            case REQUEST_PICK_VIDEO:
                if (resultCode == RESULT_OK) {
                listLocalMedia = new ArrayList<>();
                listLocalMedia = PictureSelector.obtainMultipleResult(data);
                Log.e("ad", "REQUEST_PICK_VIDEO =" + listLocalMedia.size());
                mediaUpload(Constants.TYPE_VIDEO);}
                break;
            case REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK) {
                    listLocalMedia = new ArrayList<>();
                    listLocalMedia = PictureSelector.obtainMultipleResult(data);
                    Log.e("ad", "REQUEST_CAMERA_PICTURE =" + listLocalMedia.size());
                    mediaUpload(Constants.TYPE_IMAGE);
                }
                break;

            case REQUEST_PLACE_PICKER:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, mActivity);

                    String mName = place.getName().toString();
                    //  if (!TextUtils.isEmpty(mName) && SCUtils.isContainLatLng(mName))
                    if (!TextUtils.isEmpty(mName) && mName.contains("°") && SCUtils.containsDigit(mName)) {
                        mName = "";
                    }
                    userLocation = new UserLocation(SCUtils.validateStr(mName)
                            , SCUtils.validateStr(place.getAddress().toString())
                            , SCUtils.validateStr(String.valueOf(place.getLatLng().latitude))
                            , SCUtils.validateStr(String.valueOf(place.getLatLng().longitude)));


                    String toastMsg = String.format("Place: %s", place.getName());
                    Log.e(TAG, toastMsg);


                    sendMsg(Constants.TYPE_LOCATION);
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    // TODO: Handle the error.
                    Log.e(TAG, status.getStatusMessage());

                } else if (resultCode == RESULT_CANCELED) {
                    Log.e(TAG, "the user cancelled the operation");
                }

                break;

            case REQUEST_SHARE_CONTACT:
                if (data != null) {

                    sharedContactSenderName = data.getStringExtra("name");
                    sharedContactSenderNumber = data.getStringExtra("phone");
                    sharedContactSenderImage = "";
                    Log.e("ad", ">" + sharedContactSenderName + ":" + sharedContactSenderNumber);

                    sendMsg(Constants.TYPE_CONTACT);

                } else {

                    sharedContactSenderName = "";
                    sharedContactSenderNumber = "";
                    sharedContactSenderImage = "";
                }
                break;

            default:
                sharedContactSenderName = "";
                sharedContactSenderNumber = "";
                sharedContactSenderImage = "";
                break;

        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_add:
                openPopup();

                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.imgSend:

                if (InternetUtils.isNetworkConnected(SingleChatActivity.this)) {
                    sendMsg(Constants.TYPE_MESSAGE);
                } else {
                    AlertUtils.showSimpleAlert(SingleChatActivity.this, getResources().getString(R.string.e_no_internet));

                }
                break;
            case R.id.img_camera:
                PictureSelector.create(SingleChatActivity.this)
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


                break;
        }
    }

    private void openPopup() {


         popup = new PopupMenu(SingleChatActivity.this, img_add);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());


        //registering popup with OnMenuItemClickListener

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_camera:
//                        takePhoto(Constants.PICK_CAMERA_IMAGE, 1, REQUEST_CAMERA_PICTURE);
                        PictureSelector.create(SingleChatActivity.this)
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
                        return true;
                    case R.id.menu_gallery:


                        //takePhoto(Constants.PICK_MULTIPLE_GALLERY_IMAGE, 10, REQUEST_PICK_GALLERY);
                        PictureSelector.create(SingleChatActivity.this)
                                .openGallery(PictureMimeType.ofAll())
                                //.theme(R.style.MyPictureStyle)
                                .maxSelectNum(10)
                                .minSelectNum(1)
                                .imageSpanCount(3)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .previewImage(true)
                                .previewVideo(true)
                                .enablePreviewAudio(false)
                                .isCamera(true)
                                // .imageFormat(PictureMimeType.PNG)// Take a photo save image format suffix, default jpeg
                                .isZoomAnim(true)
                                .sizeMultiplier(0.5f)
                                .setOutputCameraPath("/" + getResources().getString(R.string.app_name) + "/media")// Custom photo save path, no need to fill
                                .enableCrop(true)
                                .compress(true)
                                //.glideOverride()// Int glide loading width and height, the smaller the picture list, the smoother it will affect the clarity of the list image browsing
                                //.withAspectRatio()// Int crop ratio such as 16:9 3:2 3:4 1:1 customizable
                                .hideBottomControls(false)
                                .isGif(true)
                                //  .compressSavePath(getPath())
                                .freeStyleCropEnabled(true)
                                // .circleDimmedLayer(true)
                                .showCropFrame(true)
                                .showCropGrid(true)
                                .openClickSound(false)
                                .selectionMedia(listLocalMedia)
                                .previewEggs(true)
                                //  .cropCompressQuality()
                                .minimumCompressSize(100)
                                .synOrAsy(true)
                                // .cropWH()
                                .rotateEnabled(true)
                                //   .scaleEnabled()
                                // .videoQuality()
                                //  .videoMaxSecond(15)
                                // .videoMinSecond(10)
                                //.recordVideoSecond()
                                .isDragFrame(false)
                                .forResult(REQUEST_PICK_GALLERY);

                        return true;
                    case R.id.menu_video:
                        //takePhoto(Constants.PICK_MULTIPLE_GALLERY_IMAGE, 10, REQUEST_PICK_GALLERY);
                        PictureSelector.create(SingleChatActivity.this)
                                .openGallery(PictureMimeType.ofVideo())
                                //.theme(R.style.MyPictureStyle)
                                .maxSelectNum(10)
                                .minSelectNum(1)
                                .imageSpanCount(3)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .previewImage(true)
                                .previewVideo(true)
                                .enablePreviewAudio(false)
                                .isCamera(true)
                                // .imageFormat(PictureMimeType.PNG)// Take a photo save image format suffix, default jpeg
                                .isZoomAnim(true)
                                .sizeMultiplier(0.5f)
                                .setOutputCameraPath("/" + getResources().getString(R.string.app_name) + "/media")// Custom photo save path, no need to fill
                                .enableCrop(true)
                                .compress(true)
                                //.glideOverride()// Int glide loading width and height, the smaller the picture list, the smoother it will affect the clarity of the list image browsing
                                //.withAspectRatio()// Int crop ratio such as 16:9 3:2 3:4 1:1 customizable
                                .hideBottomControls(false)
                                .isGif(true)
                                //  .compressSavePath(getPath())
                                .freeStyleCropEnabled(true)
                                // .circleDimmedLayer(true)
                                .showCropFrame(true)
                                .showCropGrid(true)
                                .openClickSound(false)
                                .selectionMedia(listLocalMedia)
                                .previewEggs(true)
                                //  .cropCompressQuality()
                                .minimumCompressSize(100)
                                .synOrAsy(true)
                                // .cropWH()
                                .rotateEnabled(true)
                                //   .scaleEnabled()
                                // .videoQuality()
                                //  .videoMaxSecond(15)
                                // .videoMinSecond(10)
                                //.recordVideoSecond()
                                .isDragFrame(false)
                                .forResult(REQUEST_PICK_VIDEO);
                        return true;
                  /*  case R.id.menu_document:
                        return true;*/
                    case R.id.menu_location:


                       /* initPermission(mActivity, new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                try {
                                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                                    Intent intent = intentBuilder.build(mActivity);
                                    // Start the intent by requesting a result,
                                    // identified by a request code.
                                    startActivityForResult(intent, REQUEST_PLACE_PICKER);

                                } catch (GooglePlayServicesRepairableException e) {
                                    Log.e(TAG, e.toString(), e);
                                } catch (GooglePlayServicesNotAvailableException e) {
                                    Log.e(TAG, e.toString(), e);
                                }
                            }

                            @Override
                            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                                Toast.makeText(mActivity, "PLease provide location permission.", Toast.LENGTH_SHORT).show();
                            }
                        },true,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION);*/

                        try {
                            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                            Intent intent = intentBuilder.build(mActivity);
                            // Start the intent by requesting a result,
                            // identified by a request code.
                            startActivityForResult(intent, REQUEST_PLACE_PICKER);

                        } catch (GooglePlayServicesRepairableException e) {
                            Log.e(TAG, e.toString(), e);
                        } catch (GooglePlayServicesNotAvailableException e) {
                            Log.e(TAG, e.toString(), e);
                        }

                        return true;
                    case R.id.menu_contact:
                        Intent intent = new Intent(SingleChatActivity.this, ContactListActivity.class);
                        startActivityForResult(intent, REQUEST_SHARE_CONTACT);
                        return true;
                    case R.id.menu_cancel:
                        popup.dismiss();
                        return true;
                    default:
                        return false;
                }
            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (popup != null) {
                    popup.dismiss();
                }
                popup.show();

            }
        }, 100);


    }

    @Override
    protected void onStop() {
        if (popup != null) {
            popup.dismiss();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (popup != null) {
            popup.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (popup != null) {
            popup.dismiss();
        }
        ifshow = true;
    }
}
