package com.bacancy.eprodigy.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.Activity.BaseActivity;
import com.bacancy.eprodigy.Activity.SingleChatActivity;
import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.Models.ChatStateModel;
import com.bacancy.eprodigy.Models.PresenceModel;
import com.bacancy.eprodigy.MyApplication;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.custom_loader.CustomProgressDialog;
import com.bacancy.eprodigy.db.DataManager;
import com.bacancy.eprodigy.permission.PermissionActivity;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.utils.Pref;
import com.bacancy.eprodigy.xmpp.XMPPEventReceiver;
import com.bacancy.eprodigy.xmpp.XMPPHandler;
import com.bacancy.eprodigy.xmpp.XmppCustomEventListener;

public class BaseFragment extends Fragment {
    Activity activity;
    public XMPPEventReceiver xmppEventReceiver;
    public MyApplication mChatApp = MyApplication.getInstance();
    public XMPPHandler xmppHandler;
    public String username, password;


    public XmppCustomEventListener xmppCustomEventListener = new XmppCustomEventListener() {

        //Event Listeners
        public void onNewMessageReceived(ChatPojo chatPojo) {

            Log.e("ad", "BaseFragment onNewMessageReceived>" + chatPojo.toString());


            if (chatPojo != null && chatPojo.getMsgMode().equalsIgnoreCase(AppConfing.GROUP_CHAT_MSG_MODE)
                    && chatPojo.getChatText().equals(AppConfing.GROUP_GREETINGS)) {
                ((BaseActivity)getActivity()).getGroupDetailsApiCall(activity, chatPojo);
            } else if (chatPojo != null) {
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

            if (!xmppHandler.loggedin)
                //  new XMPPHandler.LoginTask(mActivity,username,password);
                xmppHandler.login();
        }

        public void onLoginFailed() {
            xmppHandler.disconnect();
            Toast.makeText(activity, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        xmppEventReceiver = mChatApp.getEventReceiver();



        username = Pref.getValue(activity, "username", "");
        password = Pref.getValue(activity, "password", "");


    }

    @Override
    public void onResume() {
      xmppEventReceiver.setListener(xmppCustomEventListener);
        super.onResume();
    }
}
