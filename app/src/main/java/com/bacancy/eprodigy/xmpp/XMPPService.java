package com.bacancy.eprodigy.xmpp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.Models.ChatStateModel;
import com.bacancy.eprodigy.Models.PresenceModel;
import com.bacancy.eprodigy.utils.Constants;


public class XMPPService extends Service {

    private final String TAG = getClass().getSimpleName();
    public XMPPHandler xmpp;
    public static boolean isServiceRunning = false;
    private static Context appContext;
    public XMPPService(){}

    @Override
    public void onCreate() {
        super.onCreate();
        xmpp = new XMPPHandler(XMPPService.this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        appContext = getBaseContext();//Get the context here
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(intent != null){
            xmpp.connect();
            XMPPService.isServiceRunning = true;
        }

        return new LocalBinder<>(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(xmpp != null) xmpp.disconnect();
        XMPPService.isServiceRunning = false;
    }

    public void onNewMessage(final ChatPojo chatItem) {

        Intent intent = new Intent(Constants.EVT_NEW_MSG);
        intent.putExtra(Constants.INTENT_KEY_NEWMSG,chatItem);
        sendBroadcast(intent);
    }

    public void onAuthenticated() {
        sendBroadcast(new Intent(Constants.EVT_AUTH_SUC));
    }

    public void onReConnectionError() {
        sendBroadcast(new Intent(Constants.EVT_RECONN_ERR));
    }

    public void onConnected() {
        sendBroadcast(new Intent(Constants.EVT_CONN_SUC));
    }

    public void onReConnected() {
        sendBroadcast(new Intent(Constants.EVT_RECONN_SUC));
    }

    public void onConnectionClosed() {
        sendBroadcast(new Intent(Constants.EVT_CONN_CLOSE));
    }

    public void onReConnection() {
        sendBroadcast(new Intent(Constants.EVT_RECONN_WAIT));
    }

    public void onLoginFailed(){
        sendBroadcast(new Intent(Constants.EVT_LOGIN_ERR));
    }

    public void onLoggedIn(){
        sendBroadcast(new Intent(Constants.EVT_LOGGED_IN));
    }

    public void onSignupSuccess(){
        sendBroadcast(new Intent(Constants.EVT_SIGNUP_SUC));
    }

    public void onSignupFailed(String error) {
        Intent intent = new Intent(Constants.EVT_SIGNUP_ERR);
        intent.putExtra(Constants.INTENT_KEY_SIGNUP_ERR,error);
        sendBroadcast(intent);
    }

    public void onPresenceChange(PresenceModel presenceModel){
        Intent intent = new Intent(Constants.EVT_PRESENCE_CHG);
        intent.putExtra(Constants.INTENT_KEY_PRESENCE,presenceModel);
        sendBroadcast(intent);
    }

    public void onChatStateChange(ChatStateModel chatStateModel){
        Intent intent = new Intent(Constants.EVT_CHATSTATE_CHG);
        intent.putExtra(Constants.INTENT_KEY_CHATSTATE,chatStateModel);
        sendBroadcast(intent);
    }

    //
    public void onRequestSubscribe(String fromUserID) {
        Intent intent = new Intent(Constants.EVT_REQUEST_SUBSCRIBE);
        intent.putExtra(Constants.INTENT_KEY_NEWREQUEST,fromUserID);
        sendBroadcast(intent);
    }

//    public boolean createRoom(String grp_name) {
//        try {
//            // Create the XMPP address (JID) of the MUC.
//            EntityBareJid mucJid = JidCreate.entityBareFrom(grp_name + "@" + grp_service);
//            // Create the nickname.
//            Resourcepart nickname = Resourcepart.from(mUser);
//            // Get the MultiUserChatManager
//            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
//            // Get a MultiUserChat using MultiUserChatManager
//            MultiUserChat muc = manager.getMultiUserChat(mucJid);
//            // Prepare a list of owners of the new room
//            try {
//                Set<Jid> owners = JidUtil.jidSetFrom(new String[]{mUser + "@" + Constant.IP});
//                // Create the room
//                muc.create(nickname)
//                        .getConfigFormManager()
//                        .setRoomOwners(owners);
//                Form form = muc.getConfigurationForm();
//                Form submitForm = form.createAnswerForm();
//                for (FormField formField : submitForm.getFields()) {
//                    if (!FormField.Type.hidden.equals(formField.getType())
//                            && formField.getVariable() != null) {
//                        submitForm.setDefaultAnswer(formField.getVariable());
//                    }
//                }
//                submitForm.setAnswer("muc#roomconfig_publicroom", true);
//                submitForm.setAnswer("muc#roomconfig_persistentroom", true);
//                muc.sendConfigurationForm(submitForm);
//                Log.d(TAG, "submit form");
//                muc.join(nickname);
//                Log.d(TAG, "join");
//
//                return true;
//            } catch (MultiUserChatException.MissingMucCreationAcknowledgeException e) {
//                showToast("Group Name is already Exist");
//                Log.d(TAG, "Group is already there " + Arrays.toString(e.getStackTrace()));
//                return false;
//            } catch (MultiUserChatException.MucAlreadyJoinedException e) {
//                showToast("You are already part of this Group");
//                Log.d(TAG, "Group Error5 : " + e.getMessage());
//                return false;
//            } catch (MultiUserChatException.MucConfigurationNotSupportedException e) {
//                Log.d(TAG, "Group Error7 : " + e.getMessage());
//                return false;
//            }
//
//        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | InterruptedException | XmppStringprepException | MultiUserChatException.NotAMucServiceException e) {
//            Log.d(TAG, "Group Error : " + e.getMessage());
//            return false;
//        } catch (SmackException.NotConnectedException e) {
//            Log.d(TAG, "Group Error2 : " + e.getMessage());
//            return false;
//        }
//    }

    private static void showToast(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * join the Groupchat when someone recieve head chat
     *
     * @param grpName Name of Group
     */
//    public void JoinRoom(String grpName) {
//        try {
//
//            EntityBareJid mucJid = JidCreate.entityBareFrom(grpName + "@" + grp_service);
//            Resourcepart nickname = Resourcepart.from(mUser);
//            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
//            MultiUserChat muc = manager.getMultiUserChat(mucJid);
//
//            MucEnterConfiguration.Builder mucEnterConfiguration
//                    = muc.getEnterConfigurationBuilder(nickname).requestNoHistory();
//
//
//            muc.join(mucEnterConfiguration.build());
//
//            RoomInfo info = manager.getRoomInfo(mucJid);
//            System.out.println("Number of occupants:" + info.getOccupantsCount());
//            System.out.println("Room Subject:" + info.getSubject());
//
//        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | InterruptedException | XmppStringprepException | MultiUserChatException.NotAMucServiceException e) {
//            Log.d(TAG, "Group Error : " + e.getMessage());
//        } catch (SmackException.NotConnectedException e) {
//            Log.d(TAG, "Group Error4 : " + e.getMessage());
//        }
//    }

    /**
     * Send Message in group
     *
     * @param msgText  message
     * @param grp_name group Name
     * @return message send successfully or not
     */
//    public boolean sendGrpMessage(String msgText, String grp_name) {
//        try {
//            Message msg = new Message();
//            msg.setType(Message.Type.groupchat);
//            msg.setSubject("chat");
//            msg.setBody(msgText);
//            EntityBareJid mucJid = JidCreate.entityBareFrom(grp_name + "@" + grp_service);
//            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
//            MultiUserChat muc = manager.getMultiUserChat(mucJid);
//
//            muc.sendMessage(msg);
//            return true;
//        } catch (XmppStringprepException | InterruptedException | SmackException.NotConnectedException e) {
//            Log.d(TAG, "sendGrpMessage() Error = [" + e.getMessage() + "]");
//            return false;
//        }
//
//    }
}
