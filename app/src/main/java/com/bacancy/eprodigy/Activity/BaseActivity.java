package com.bacancy.eprodigy.Activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bacancy.eprodigy.API.ApiClient;
import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.Models.ChatStateModel;
import com.bacancy.eprodigy.Models.GroupDetailResponse;
import com.bacancy.eprodigy.Models.GroupPojo;
import com.bacancy.eprodigy.Models.PresenceModel;
import com.bacancy.eprodigy.MyApplication;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;
import com.bacancy.eprodigy.ResponseModel.LastSeenResponse;
import com.bacancy.eprodigy.ResponseModel.UpdateGroupDetailResponse;
import com.bacancy.eprodigy.custom_loader.CustomProgressDialog;
import com.bacancy.eprodigy.db.DataManager;
import com.bacancy.eprodigy.permission.MyPermission;
import com.bacancy.eprodigy.permission.PermissionActivity;
import com.bacancy.eprodigy.permission.PermissionListener;
import com.bacancy.eprodigy.utils.AlertUtils;
import com.bacancy.eprodigy.utils.Constants;

import com.bacancy.eprodigy.utils.InternetUtils;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.utils.Pref;
import com.bacancy.eprodigy.utils.SCUtils;
import com.bacancy.eprodigy.xmpp.XMPPEventReceiver;
import com.bacancy.eprodigy.xmpp.XMPPHandler;
import com.bacancy.eprodigy.xmpp.XMPPService;
import com.bacancy.eprodigy.xmpp.XmppCustomEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BaseActivity extends AppCompatActivity {
    public String TAG = "BaseActivity";


    PermissionActivity permissionActivity;

    public Activity activity;
    CustomProgressDialog customProgressDialog;

    public XMPPEventReceiver xmppEventReceiver;
    public MyApplication mChatApp = MyApplication.getInstance();
    public XMPPHandler xmppHandler;
    public String username, password;


    public XmppCustomEventListener xmppCustomEventListener = new XmppCustomEventListener() {

        //Event Listeners
        public void onNewMessageReceived(ChatPojo chatPojo) {

            Log.e("ad", "BaseActivity onNewMessageReceived>" + chatPojo.toString());


            if (chatPojo != null && chatPojo.getMsgMode().equalsIgnoreCase(AppConfing.GROUP_CHAT_MSG_MODE)
                    && chatPojo.getChatText().equals(AppConfing.GROUP_GREETINGS)) {
                getGroupDetailsApiCall(activity, chatPojo);
            } else if (chatPojo != null) {
                chatPojo.setMsgMode(AppConfing.SINGLE_CHAT_MSG_MODE);
                chatPojo.setShowing(true);
                chatPojo.setMine(false);
                DataManager.getInstance().AddChat(chatPojo);
            }

            LogM.e("onNewMessageReceived ChatActivity");

            if (SingleChatActivity.isChatActivityOpened)
                BaseActivity.SendNotification(activity, chatPojo);

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

            username = Pref.getValue(activity, "username", "");
            password = Pref.getValue(activity, "password", "");

            Log.d("startXmppService", "login-onConnected=" + username + " " + password);

            xmppHandler = MyApplication.getmService().xmpp;
            xmppHandler.setUserPassword(username, password);

            if (!xmppHandler.loggedin) {
                //  new XMPPHandler.LoginTask(mActivity,username,password);
                xmppHandler.login();
            }
        }

        public void onLoginFailed() {
            xmppHandler.disconnect();
            Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        xmppEventReceiver = mChatApp.getEventReceiver();


        permissionActivity = new PermissionActivity(BaseActivity.this);


        customProgressDialog = new CustomProgressDialog();

        username = Pref.getValue(activity, "username", "");
        password = Pref.getValue(activity, "password", "");


    }

    /**
     * @param mActivity
     * @param permissionlistener
     * @param isDenied           : for denied message ,if true then user must allow permission
     * @param permissions
     */
    public void initPermission(Activity mActivity, PermissionListener permissionlistener, boolean isDenied, String... permissions) {
        if (!isDenied) {
            MyPermission.with(mActivity)
                    .setPermissionListener(permissionlistener)
                    .setPermissions(permissions)
                    .check();
        } else {
            MyPermission.with(mActivity)
                    .setPermissionListener(permissionlistener)
                    // .setRationaleTitle(R.string.rationale_title) //confirmation dialog
                    //.setRationaleMessage(R.string.rationale_message)//confirmation dialog
                    .setDeniedTitle(mActivity.getResources().getString(R.string.denied_message))
                    .setDeniedMessage(
                            mActivity.getResources().getString(R.string.reject_message))
                    .setGotoSettingButtonText(mActivity.getResources().getString(R.string.btn_go_title))
                    .setPermissions(permissions)
                    .check();
        }

    }

    /**
     * @return true or false (if false show no internet dialog)
     */
    public boolean validateInternetConn(Activity mActivity) {
        if (InternetUtils.isNetworkConnected((mActivity))) {

            return true;
        } else {
            dismissLoadingDialog();
            showNoInternetDialog(mActivity, false);
        }
        return false;
    }


    /**
     * no internet dialog
     */
    public void showNoInternetDialog(final Activity mActivity, final boolean needToFinishonOk) {
        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mActivity, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(mActivity);
        }

        builder.setTitle(mActivity.getResources().getString(R.string.no_internet_title))
                .setMessage(mActivity.getResources().getString(R.string.no_internet_msg))
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        if (needToFinishonOk) {
                            mActivity.finish();
                        }
                        //validateInternetConn();


                    }
                })
                /* .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                // do nothing
                }
                })*/
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    @Override
    protected void onResume() {

        xmppEventReceiver.setListener(xmppCustomEventListener);
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionActivity.onPermissionsResult(requestCode, permissions, grantResults);
        LogM.v("OnResult Base");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void showLoadingDialog(Context mActivity) {

        if (mActivity != null && customProgressDialog != null) {

            customProgressDialog.showCustomDialog(mActivity);
        }
    }



    public void dismissLoadingDialog() {
        if (customProgressDialog != null) {
            customProgressDialog.dismissCustomDialog();
        }
    }

    public boolean validateUser(final Activity mActivity, int responseStatus, String message) {
        if (responseStatus == Constants.SESSION_TIME_OUT_STATUS) {
            dismissLoadingDialog();
            // AlertUtils.showSimpleAlert(mContext, TextUtils.isEmpty(message)?mContext.getString(R.string.session_time_out):message);

            AlertDialog dialog = new AlertDialog.Builder(mActivity)
                    .setIcon(0).setMessage(TextUtils.isEmpty(message) ? mActivity.getString(R.string.session_time_out) : message)
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //clear session
                            Pref.setValue(mActivity, "verified", "0");

                            Intent intent = new Intent(mActivity, MobileRegistrationActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mActivity.startActivity(intent);
                            mActivity.finish();

                        }
                    }).show();
            AlertUtils.changeDefaultColor(dialog);

            return true;
        }
        return false;
    }


    private static int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.add_btn : R.mipmap.add_btn;
    }


    private static String displayName = "";
    static String channelId = "channel-01";
    static String channelName = "Channel Name";
    static int importance = NotificationManager.IMPORTANCE_HIGH;

    public static void SendNotification(Context mContext, ChatPojo chatPojo) {

        ContactListResponse.ResponseDataBean singleUser = DataManager.getInstance().getUser(chatPojo.getChatSender());
        if (singleUser != null) {
            displayName = singleUser.getDisplayname();
        }

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        int num = (int) System.currentTimeMillis();

        Intent intent = new Intent(mContext, SingleChatActivity.class);
        intent.putExtra(AppConfing.CHAT_USER_NAME, chatPojo.getChatSender());
        intent.putExtra(AppConfing.DISPLAY_NAME, displayName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext.getApplicationContext(),
                0 /* Request code */,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
            notificationBuilder = new NotificationCompat.Builder(mContext, channelId);
            notificationBuilder.setChannelId(channelId);

        } else {
            notificationBuilder = new NotificationCompat.Builder(mContext);

        }


        notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX); //HIGH, MAX, FULL_SCREEN and setDefaults(Notification.DEFAULT_ALL) will make it a Heads Up Display Style
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL); // also requires VIBRATE permission
        notificationBuilder.setSmallIcon(getNotificationIcon());
        notificationBuilder.setColor(mContext.getResources().getColor(R.color.colorPrimary));
        notificationBuilder.setContentTitle(displayName);
        notificationBuilder.setContentText(chatPojo.getChatText());
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setContentIntent(pendingIntent);
// notificationBuilder.setNumber(4);

// Notification notification = new NotificationCompat.InboxStyle(notificationBuilder)
// .addLine(chatPojo.getChatText())
//// .addLine("Second Message")
//// .addLine("Third Message")
//// .addLine("Fourth Message")
// .setBigContentTitle(displayName)
// .setSummaryText("+3 more")
// .build();


        notificationManager.notify(num /* ID of list_menu */, notificationBuilder.build());
    }


    public void startXmppService(Activity mActivity) {

            if (!isMyServiceRunning(XMPPService.class)) {
                Log.d("startXmppService--", "running already");
                final Intent intent = new Intent(mActivity, XMPPService.class);
// mChatApp.UnbindService();
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mChatApp.BindService(intent);
                    }
                }, 200);

            } else {
                xmppHandler = MyApplication.getmService().xmpp;
                xmppHandler.setUserPassword(username, password);

                if (!xmppHandler.isConnected()) {
                    xmppHandler.connect();


                    // new XMPPHandler.ConnectXMPP(mActivity).execute();
                } else {
                    Log.d("Login startXmppService-", username + " " + password);

                    if (!xmppHandler.loggedin) {
                        //  new XMPPHandler.LoginTask(mActivity,username,password);
                        xmppHandler.login();
                    }
                }
            }


    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void getGroupDetailsApiCall(final Activity mActivity, final ChatPojo chatPojo) {

        showLoadingDialog(this);

        String username = Pref.getValue(this, AppConfing.USERNAME, "");
        String login_token = Pref.getValue(this, AppConfing.LOGIN_TOKEN, "");

        HashMap<String, String> data = new HashMap<>();


        /*[{"name":"qwertyu_1546334361"}]*/

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", chatPojo.getGroupId());
            //  jsonObject.put("name", "video_1546337418");
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        data.put("group_list", jsonArray.toString());
        data.put("username", username);
        data.put("login_token", login_token);

        Call<GroupDetailResponse> call = ApiClient.getClient().getGroupDetailsApiCall(data);
        call.enqueue(new Callback<GroupDetailResponse>() {
            @Override
            public void onResponse(Call<GroupDetailResponse> call, Response<GroupDetailResponse> response) {
                if (response.isSuccessful()) {
                    dismissLoadingDialog();

                    if (validateUser(mActivity,
                            response.body().getStatus(),
                            response.body().getMessage())) {
                        return;
                    }
                    List<GroupDetailResponse.UserdataBean> beanList = response.body().getUserdata();

                    for (GroupDetailResponse.UserdataBean userdataBean : beanList) {
                        if (userdataBean != null) {

                            chatPojo.setShowing(true);
                            chatPojo.setMine(false);
                            chatPojo.setMsgMode(AppConfing.GROUP_CHAT_MSG_MODE);
                            chatPojo.setGroupImage(userdataBean.getGroupimage());
                            chatPojo.setGroupName(userdataBean.getGroup_title());
                            DataManager.getInstance().AddChat(chatPojo);


                            LogM.e("getGroupId=" + chatPojo.getGroupId());

                            GroupPojo groupPojo = new GroupPojo();
                            groupPojo.setGroupId(chatPojo.getGroupId());
                            groupPojo.setGroupTitle(userdataBean.getGroup_title());
                            groupPojo.setGroupName(userdataBean.getGroupname());
                            groupPojo.setGroupImage(userdataBean.getGroupimage());
                            groupPojo.setCreatedAt(userdataBean.getCreated_at());
                            groupPojo.setModifyAt(userdataBean.getModify_at());

                            DataManager.getInstance().AddGroup(groupPojo);
                        }
                    }
//                     GroupDetailResponse.UserdataBean userdataBean = response.body().getUserdata().get(0);


                } else {
                    dismissLoadingDialog();
                    AlertUtils.showSimpleAlert(mActivity, mActivity.getString(R.string.server_error));
                }
            }

            @Override
            public void onFailure(Call<GroupDetailResponse> call, Throwable t) {
                LogM.e("errrrrrr" + t.toString());
                dismissLoadingDialog();
            }
        });
    }


}
