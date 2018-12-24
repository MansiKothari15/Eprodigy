package com.bacancy.eprodigy.xmpp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bacancy.eprodigy.API.AppConfing;
import com.bacancy.eprodigy.Activity.BaseActivity;
import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.Models.ChatStateModel;
import com.bacancy.eprodigy.Models.PresenceModel;
import com.bacancy.eprodigy.MyApplication;
import com.bacancy.eprodigy.R;
import com.bacancy.eprodigy.custom_loader.CustomProgressDialog;
import com.bacancy.eprodigy.utils.Constants;
import com.bacancy.eprodigy.utils.LogM;
import com.bacancy.eprodigy.utils.Pref;
import com.bacancy.eprodigy.utils.SCUtils;
import com.google.gson.Gson;
import com.luck.picture.lib.tools.Constant;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.roster.packet.RosterPacket;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateListener;
import org.jivesoftware.smackx.chatstates.ChatStateManager;
import org.jivesoftware.smackx.iqlast.LastActivityManager;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MucEnterConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatException;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.muc.packet.MUCUser;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.jid.util.JidUtil;
import org.jxmpp.stringprep.XmppStringprepException;
import org.jxmpp.util.XmppStringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class XMPPHandler {

    private static String TAG = "XMPPHandler";
    public static boolean connected = false;
    private static LastActivityManager lastActivityManager;
    public boolean loggedin = false;
    public static boolean isconnecting = false;
    public static boolean isToasted = true; //Show toast for events? set false to just print via Log
    private HashMap<String, Boolean> chat_created_for = new HashMap<>(); //for single chat env
    public static AbstractXMPPConnection connection;
    public static String userId;
    public String userPassword;
    private boolean autoLogin = true;
    Roster roster;

    Gson gson;
    public static XMPPService service;
    public static XMPPHandler instance = null;
    public static boolean instanceCreated = false;

    private static final boolean debug = Constants.XMPP_DEBUG;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    //XMPP Stuffs
    public org.jivesoftware.smack.chat2.ChatManager mChat;
    private XMPPConnectionListener mConnectionListener = new XMPPConnectionListener();
    private MyChatManagerListener mChatManagerListener;
    private MyMessageListener mMessageListener;
    private MyStanzaListener mStanzaListener;
    private MyRosterListener mRoasterListener;
    private org.jivesoftware.smack.chat2.Chat chat;
    private static String mUser;

    private static String grp_service;
    private static String grp_name = Constants.grp_name;


    /*
     * A default constructor which only service instance
     * This allows to connect, without needing to be loggedin
     * This way, we will get instance to XMPPHandler, and can login whenever we want,
     * using .login() method
     */
    public XMPPHandler(XMPPService service) {
        this.service = service;
        this.autoLogin = false;

        if (instance == null) {
            instance = this;
            instanceCreated = true;
        }

        //Prepare the connections and listeners
        init();
    }

    // Get XMPPHandler instance
    public static XMPPHandler getInstance() {
        return instance;
    }

    public void init() {

        if (debug) Log.e(TAG, "starting XMPPHandler");

        gson = new Gson(); //We need GSON to parse chat messages
        mMessageListener = new MyMessageListener(); //Message event Listener
        mChatManagerListener = new MyChatManagerListener(); //Chat Manager
        mStanzaListener = new MyStanzaListener(); // Listen for incoming stanzas (packets)
        mRoasterListener = new MyRosterListener();
        mUser = Pref.getValue(MyApplication.getInstance(), AppConfing.USERNAME, "");
        // Ok, now that events have been attached, we can prepare connection
        // (we will initialize connection by calling ".connect()" method later on.
        initialiseConnection();
    }

    //Pass server address, port to initialize connection
    private void initialiseConnection() {

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
                .builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        try {
            config.setXmppDomain(Constants.XMPP_DOMAIN);
            config.setPort(Constants.XMPP_PORT);
            config.setHost(Constants.XMPP_HOST);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        try {
            InetAddress inetAddress = InetAddress.getByName(Constants.XMPP_HOST);
            config.setHostAddress(inetAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
//        //  config.setPort(Constants.XMPP_PORT);
//        SmackDebuggerFactory smackDebuggerFactory = new SmackDebuggerFactory() {
//            @Override
//            public SmackDebugger create(XMPPConnection connection) throws IllegalArgumentException {
//                return null;
//            }
//        };
        // SASLAuthentication.unBlacklistSASLMechanism("AuthName");
        //SASLAuthentication.blacklistSASLMechanism("AuthName");
        config.setDebuggerEnabled(true);
        config.setSendPresence(true);

//        try {
//            config.setResource(Constants.XMPP_RESOURCE);
//        } catch (XmppStringprepException e) {
//            e.printStackTrace();
//        }

        XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
        XMPPTCPConnection.setUseStreamManagementDefault(true);

//        if (connection == null) {
        connection = new XMPPTCPConnection(config.build());
//        }

//        connection.removeConnectionListener(mConnectionListener);
        connection.addConnectionListener(mConnectionListener);


        //  StanzaFilter filter = MessageTypeFilter.CHAT;

//        connection.removeAsyncStanzaListener(mStanzaListener);
        connection.addAsyncStanzaListener(mStanzaListener, new StanzaFilter() {
            @Override
            public boolean accept(Stanza stanza) {
                return true;
            }
        });
//

        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);

        roster = Roster.getInstanceFor(connection);


        roster.addRosterListener(mRoasterListener);


        //get Entry


        StanzaFilter headFilter = MessageTypeFilter.HEADLINE;
        connection.addAsyncStanzaListener(new StanzaListener() {
            @Override
            public void processStanza(Stanza packet) {
                Message message = (Message) packet;
                if (message.getBody() != null) {
                    String addedBy = XmppStringUtils.parseLocalpart(message.getFrom().toString());
                    LogM.e("HeadLines      ++++++++++++++++++++++++++++++++++++++++++");
                    LogM.e( "from: " + message.getFrom());
                    LogM.e( "xml: " + message.getType().toString());
                    LogM.e("Got text [" + message.getBody() + "] from [" + addedBy + "]");
                    LogM.e( "Type [" + message.getSubject() + "] ");
                    LogM.e( "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    if (message.getSubject().equals("1")) {
                        updateDB(message.getBody(), "You have been added in Group "
                                + message.getBody() + " by " + addedBy, 1, 0, message.getBody(), true);
                        JoinRoom(message.getBody());
                    } else {
                        updateDB(message.getBody(), "You have been Removed from Group "
                                + message.getBody() + " by " + addedBy, 1, 0, message.getBody(), true);

                    }

                }
            }
        }, headFilter);

        MultiUserChatManager.getInstanceFor(connection).addInvitationListener(new InvitationListener() {
            @Override
            public void invitationReceived(XMPPConnection conn, MultiUserChat room, EntityJid inviter, String reason, String password, Message message, MUCUser.Invite invitation) {
              //  Utils.Log(TAG, "invitationReceived() called with: conn = [" + conn + "], room = [" + room + "], inviter = [" + inviter + "], reason = [" + reason + "], password = [" + password + "], message = [" + message + "], invitation = [" + invitation + "]");
                LogM.e("invitationReceived() called with: conn = [" + conn + "], room = [" + room + "], inviter = [" + inviter + "], reason = [" + reason + "], password = [" + password + "], message = [" + message + "], invitation = [" + invitation + "]");


            }
        });

    }


    // Set username and password explicitly for login
    public void setUserPassword(String mUsername, String mPassword) {
        this.userId = mUsername;
        this.userPassword = mPassword;
    }


    public static boolean createRoom(String grp_name) {
        try {

            if (TextUtils.isEmpty(grp_name))
            {
                return false;
            }

            String gName=grp_name+"_"+System.currentTimeMillis() / 1000L;
            // Create the XMPP address (JID) of the MUC.
            EntityBareJid mucJid = JidCreate.entityBareFrom(gName + "@" + Constants.GRP_SERVICE);
            // Create the nickname.
            Resourcepart nickname = Resourcepart.from(userId);
            // Get the MultiUserChatManager
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
            // Get a MultiUserChat using MultiUserChatManager
            MultiUserChat muc = manager.getMultiUserChat(mucJid);
            // Prepare a list of owners of the new room
            try {
                Log.e(TAG,"userId="+userId);
                Set<Jid> owners = JidUtil.jidSetFrom(new String[]{userId + "@" + Constants.XMPP_HOST});
                // Create the room
                muc.create(nickname);
                //  .getConfigFormManager()
                //   .setRoomOwners(owners);
                Form form = muc.getConfigurationForm();
                Form submitForm = form.createAnswerForm();

                for (FormField formField : submitForm.getFields()) {
                    if (!FormField.Type.hidden.equals(formField.getType())
                            && formField.getVariable() != null) {
                        submitForm.setDefaultAnswer(formField.getVariable());
                    }
                }

                submitForm.setAnswer("muc#roomconfig_publicroom", true);
                submitForm.setAnswer("muc#roomconfig_persistentroom", true);
                submitForm.setAnswer("muc#roomconfig_roomname", grp_name);



                muc.sendConfigurationForm(submitForm);
                Log.d(TAG, "submit form");
                muc.join(nickname);
                Log.d(TAG, "join");

                return true;
            } catch (MultiUserChatException.MissingMucCreationAcknowledgeException e) {
//                showToast("Group Name is already Exist");
                Log.d(TAG, "Group is already there " + Arrays.toString(e.getStackTrace()));
                return false;
            } catch (MultiUserChatException.MucAlreadyJoinedException e) {
//                showToast("You are already part of this Group");
                Log.d(TAG, "Group Error5 : " + e.getMessage());
                return false;
            } /*catch (MultiUserChatException.MucConfigurationNotSupportedException e) {
                Log.d(TAG, "Group Error7 : " + e.getMessage());
                return false;
            }*/

        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | InterruptedException | XmppStringprepException | MultiUserChatException.NotAMucServiceException e) {
            Log.d(TAG, "Group Error : " + e.getMessage());
            return false;
        } catch (SmackException.NotConnectedException e) {
            Log.d(TAG, "Group Error2 : " + e.getMessage());
            return false;
        }
    }


    /**
     * Add users to group
     *
     * @param inviteuser userID
     * @param groupName  group Name
     */
    public static void inviteToGroup(String inviteuser, String groupName) {
        try {

            Message message = new Message(JidCreate.entityBareFrom(makeSmackId(inviteuser)));
            message.setBody(groupName);
            message.setType(Type.headline);
            message.setSubject("1");
//            message.addExtension(new GroupChatInvitation(grp_name + "@" + grp_service));
            connection.sendStanza(message);
        } catch (XmppStringprepException | InterruptedException | SmackException.NotConnectedException e) {

        }
    }

    public static String makeSmackId(String user) {
        return user + "@" + Constants.XMPP_HOST + Constants.SMACk;
    }

    /**
     * join the Groupchat when someone recieve head chat
     *
     * @param grpName Name of Group
     */
    public static void JoinRoom(String grpName) {
        try {

            EntityBareJid mucJid = JidCreate.entityBareFrom(grpName + "@" + Constants.GRP_SERVICE);
            Resourcepart nickname = Resourcepart.from(mUser);
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
            MultiUserChat muc = manager.getMultiUserChat(mucJid);

            MucEnterConfiguration.Builder mucEnterConfiguration
                    = muc.getEnterConfigurationBuilder(nickname).requestNoHistory();


            muc.join(mucEnterConfiguration.build());

            RoomInfo info = manager.getRoomInfo(mucJid);
            System.out.println("Number of occupants:" + info.getOccupantsCount());
            System.out.println("Room Subject:" + info.getSubject());

        } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | InterruptedException | XmppStringprepException | MultiUserChatException.NotAMucServiceException e) {
            Log.d(TAG, "Group Error : " + e.getMessage());
        } catch (SmackException.NotConnectedException e) {
            Log.d(TAG, "Group Error4 : " + e.getMessage());
        }
    }

    /**
     * Send Message in group
     *
     * @param msgText  message
     * @param grp_name group Name
     * @return message send successfully or not
     */
    public boolean sendGrpMessage(String msgText, String grp_name) {
        try {
            Message msg = new Message();
            msg.setType(Type.groupchat);
            msg.setSubject("chat");
            msg.setBody(msgText);
            EntityBareJid mucJid = JidCreate.entityBareFrom(grp_name + "@" + grp_service);
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
            MultiUserChat muc = manager.getMultiUserChat(mucJid);

            muc.sendMessage(msg);
            return true;
        } catch (XmppStringprepException | InterruptedException | SmackException.NotConnectedException e) {
            Log.d(TAG, "sendGrpMessage() Error = [" + e.getMessage() + "]");
            return false;
        }

    }


    //This method sets every chat instances to false (in situations where connection closes, or error happens)
    public static void chatInstanceIterator(Map<String, Boolean> mp) {

        for (Map.Entry<String, Boolean> entry : mp.entrySet()) {
            entry.setValue(false);
        }
    }

    //check if a connection is already established
    public boolean isConnected() {
        return connected;
    }


    //Explicitly start a connection


    public static class ConnectXMPP extends AsyncTask<Void, Void, Boolean> {
        /* Activity mActivity;

         public ConnectXMPP(final Activity mActivity) {
             this.mActivity = mActivity;
         }*/
        Activity mActivity;
        private CustomProgressDialog customProgressDialog;

        public ConnectXMPP() {

            customProgressDialog = new CustomProgressDialog();
            Toast.makeText(service, "connecting....", Toast.LENGTH_LONG).show();
        }

        public ConnectXMPP(Activity mActivity) {
            this.mActivity = mActivity;
            customProgressDialog = new CustomProgressDialog();


                customProgressDialog.showCustomDialog(mActivity);
        }

        @Override
        protected void onPreExecute() {


            Log.e("ad", "------------------onPreExecute");
            // Toast.makeText(service, "connecting....", Toast.LENGTH_LONG).show();


        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.e("ad", "------------------doInBackground");
            if (connection.isConnected())
                return false;


            isconnecting = true;

           /* if (isToasted)
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(service, "connecting....", Toast.LENGTH_LONG).show();
                    }
                });*/

            if (debug) Log.d(TAG, "connecting....");

            try {

                connection.connect();


                ReconnectionManager.getInstanceFor(connection).enableAutomaticReconnection();

                connection.setReplyTimeout(100000);

//                    PingManager pm =  PingManager.getInstanceFor(connection) ;
//                    pm.setPingInterval(5) ;  // 5 sec
//                    pm.pingMyServer() ;
//                    pm.registerPingFailedListener(new PingFailedListener() {
//
//                        @Override
//                        public void pingFailed() {
//                            Log.e(TAG , "pingFailed") ;
//                        }
//
//                    });


                try {


                    DeliveryReceiptManager
                            dm = DeliveryReceiptManager.getInstanceFor(connection);
                    dm.setAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);
                    dm.autoAddDeliveryReceiptRequests();
                    ProviderManager.addExtensionProvider(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
                    ProviderManager.addExtensionProvider(DeliveryReceiptRequest.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceiptRequest.Provider());
                    dm.addReceiptReceivedListener(new ReceiptReceivedListener() {
                        @Override
                        public void onReceiptReceived(Jid fromJid, Jid toJid, String receiptId, Stanza receipt) {

                            LogM.e("fromJid" + fromJid.toString());
                            LogM.e("toJid" + toJid.toString());
                            LogM.e("receiptId" + receiptId.toString());
                            LogM.e("" + receipt.toString());
                        }
                    });

                } catch (Exception e) {
                    LogM.e("16" + e.getMessage());

                }
                connected = true;

            } catch (IOException e) {
                LogM.e("17" + e.getMessage());

                service.onConnectionClosed();
                if (isToasted)
                    new Handler(Looper.getMainLooper())
                            .post(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(service, "IOException: ", Toast.LENGTH_SHORT).show();
                                }
                            });
                if (debug) Log.e(TAG, "IOException: " + e.getMessage());
            } catch (SmackException e) {
                service.onConnectionClosed();
                LogM.e("16" + e.getMessage());

                if (isToasted)
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(service, "SMACKException: ", Toast.LENGTH_SHORT).show();
                        }
                    });
                if (debug) Log.e(TAG, "SMACKException: " + e.getMessage());
            } catch (XMPPException e) {
                service.onConnectionClosed();
                LogM.e("15" + e.getMessage());

                if (isToasted)
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(service, "XMPPException: ", Toast.LENGTH_SHORT).show();
                        }
                    });
                if (debug) Log.e(TAG, "XMPPException: " + e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
                LogM.e("14" + e.getMessage());

            }

            //Our "connection" phase is now complete. We can tell others to make requests from now on.
            return isconnecting = false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            customProgressDialog.dismissCustomDialog();
            Toast.makeText(service, "Connected", Toast.LENGTH_SHORT).show();
            Log.e("ad", "------------------Connected");
//            JoinRoom(grp_name);


        }
    }


    public static void connect() {


        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected synchronized Boolean doInBackground(Void... arg0) {
                //There is no point in reconnecting an already established connection. So abort, if we do
                if (connection.isConnected())
                    return false;

                //We are currently in "connection" phase, so no requests should be made while we are connecting.
                isconnecting = true;

                if (isToasted)
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(service, "connecting....", Toast.LENGTH_LONG).show();
                        }
                    });

                if (debug) Log.d(TAG, "connecting....");

                try {

                    connection.connect();


                    ReconnectionManager.getInstanceFor(connection).enableAutomaticReconnection();

                    connection.setReplyTimeout(100000);

//                    PingManager pm =  PingManager.getInstanceFor(connection) ;
//                    pm.setPingInterval(5) ;  // 5 sec
//                    pm.pingMyServer() ;
//                    pm.registerPingFailedListener(new PingFailedListener() {
//
//                        @Override
//                        public void pingFailed() {
//                            Log.e(TAG , "pingFailed") ;
//                        }
//
//                    });


                    /**
                     * Set delivery receipt for every Message, so that we can confirm if message
                     * has been received on other end.
                     *
                     * @NOTE: This feature is not yet implemented in this example. Maybe, I'll add it later on.
                     * Feel free to pull request to add one.
                     *
                     * Read more about this: http://xmpp.org/extensions/xep-0184.html
                     **/


                    try {


                        DeliveryReceiptManager
                                dm = DeliveryReceiptManager.getInstanceFor(connection);
                        dm.setAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);
                        dm.autoAddDeliveryReceiptRequests();
                        ProviderManager.addExtensionProvider(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
                        ProviderManager.addExtensionProvider(DeliveryReceiptRequest.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceiptRequest.Provider());
                        dm.addReceiptReceivedListener(new ReceiptReceivedListener() {
                            @Override
                            public void onReceiptReceived(Jid fromJid, Jid toJid, String receiptId, Stanza receipt) {

                                LogM.e("fromJid" + fromJid.toString());
                                LogM.e("toJid" + toJid.toString());
                                LogM.e("receiptId" + receiptId.toString());
                                LogM.e("" + receipt.toString());
                            }
                        });

                    } catch (Exception e) {
                        LogM.e("16" + e.getMessage());

                    }
                    connected = true;

                } catch (IOException e) {
                    LogM.e("17" + e.getMessage());

                    service.onConnectionClosed();
                    if (isToasted)
                        new Handler(Looper.getMainLooper())
                                .post(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(service, "IOException: ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    if (debug) Log.e(TAG, "IOException: " + e.getMessage());
                } catch (SmackException e) {
                    service.onConnectionClosed();
                    LogM.e("16" + e.getMessage());

                    if (isToasted)
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(service, "SMACKException: ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    if (debug) Log.e(TAG, "SMACKException: " + e.getMessage());
                } catch (XMPPException e) {
                    service.onConnectionClosed();
                    LogM.e("15" + e.getMessage());

                    if (isToasted)
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(service, "XMPPException: ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    if (debug) Log.e(TAG, "XMPPException: " + e.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LogM.e("14" + e.getMessage());

                }

                //Our "connection" phase is now complete. We can tell others to make requests from now on.
                return isconnecting = false;
            }
        };
        connectionThread.execute();
    }


    //Explicitly Disconnect a connection
    public void disconnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connection.disconnect();
            }
        }).start();
    }

    public String getCurrentUserDetails() {
        VCardManager vCardManager = VCardManager.getInstanceFor(connection);
        try {
            VCard mCard = vCardManager.loadVCard();
            return String.valueOf(mCard.getTo());
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

//    //Dummy method. Checks if roster belongs to one of our user
//    public Boolean checkSender(RoasterModel roasterModel, String user) {
//        EntityBareJid jid = null;
//        Presence presence = null;
//        try {
//            jid = JidCreate.entityBareFrom(user);
//            presence = roster.getPresence(jid);
//
//
//        } catch (XmppStringprepException e) {
//            e.printStackTrace();
//        }
//        assert presence != null;
//        return (presence.getFrom().toString().contains(roasterModel.getRoasterPresenceFrom()));
//
//    }

    // To track other users availability
    ///////////////////////////////////////////////////////////////////////////
    public int checkUserAvail(String chatUser) {
        Presence availability = null;

        try {

            EntityBareJid jid1;
            if (!chatUser.contains("@")) {

                chatUser = chatUser + "@" + Constants.XMPP_DOMAIN;
            }

            jid1 = JidCreate.entityBareFrom(chatUser);

//            RosterEntry userEntry = roster.getEntry(jid1);

            Presence presence = roster.getPresence(jid1);

//            availability = roster.getPresence(jid1);

            Presence.Mode mode = presence.getMode();

//            int status = retreiveState(mode, presence.isAvailable());
//            ProviderManager.addIQProvider("vCard", "vcard-temp", new VCardProvider());
//            Presence.Mode userMode = availability.getMode();

            LogM.e(TAG + "online status: " + mode + presence.isAvailable());
            return retreiveState(mode, presence.isAvailable());
        } catch (XmppStringprepException e) {
            e.printStackTrace();
            LogM.e("13" + e.getMessage());

            return 0;
        }

    }

    //Sends a subscription request to particular user (JID)
    public void sendRequestTo(String jid) throws XmppStringprepException {


        EntityBareJid jid1 = null;
        //Making the Full JID
        if (!jid.contains("@")) {
            jid = jid + "@" + Constants.XMPP_DOMAIN;
        }

        jid1 = JidCreate.entityBareFrom(jid);
        //get Entry
        RosterEntry userEntry = roster.getEntry(jid1);

        String nickname = XmppStringUtils.parseLocalpart(jid);

        boolean isSubscribed = true;
        if (userEntry != null) {
            isSubscribed = userEntry.getGroups().size() == 0;
        }

        if (isSubscribed) {
            try {
                roster.createEntry(jid1, nickname, null);
            } catch (XMPPException | SmackException e) {
                if (debug) Log.e(TAG, "Unable to add new entry " + jid, e);
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            roster.getEntry(jid1);
        }
    }

    //Get subscription requests which came "from" other users
    public ArrayList<String> getPendingRequests() {
        Collection<RosterEntry> entries = roster.getEntries();

        ArrayList<String> pendingRequestList = new ArrayList<>();
        for (RosterEntry entry : entries) {
            Presence presence = roster.getPresence(entry.getJid());
            if (entry.getType() == RosterPacket.ItemType.from) {
                pendingRequestList.add(presence.getFrom().toString());
            }
        }

        return pendingRequestList;
    }

    //Get only online users, i.e. users having subscription mode "BOTH" with you
    public void getOnlineUsers() {

        Collection<RosterEntry> entries = roster.getEntries();

//        ArrayList<RoasterModel> roasterModelArrayList = new ArrayList<>();
        for (RosterEntry entry : entries) {

            Presence presence = roster.getPresence(entry.getJid());

//            if (entry.getType() == RosterPacket.ItemType.none) {
            Presence.Mode mode = presence.getMode();

//            int status = retreiveState(mode, presence.isAvailable());
//


            int status = retreiveState(mode, presence.isAvailable());
            LogM.e("getOnlineUsers status" + status);
            service.onPresenceChange(new PresenceModel(presence.getFrom().toString(), presence.getStatus(), mode, status));


//                roasterModelArrayList.add(
//                        new RoasterModel(entry.getJid().toString(), presence.getFrom().toString(), presence.getStatus(), mode, status));
//            }

            if (debug) {
                Log.e(TAG, "" + entry.getJid());
                Log.e(TAG, "" + entry.getName());
                Log.e(TAG, "" + presence.getType().name());
                Log.e(TAG, "" + presence.getStatus());
                Log.e(TAG, "" + presence.getMode());
                Log.e(TAG, "" + entry.getType());

                String isSubscribePending = (entry.getType() == RosterPacket.ItemType.both) ? "Yes" : "No";
                Log.e(TAG, "sub: " + isSubscribePending);
            }
        }

//        return roasterModelArrayList;
    }

    //Getting presence mode, to check user status
    private int retreiveState(Presence.Mode usermode, boolean isOnline) {

        int userState = Constants.PRESENCE_MODE_OFFLINE_INT;

        if (usermode == Presence.Mode.dnd) {
            userState = Constants.PRESENCE_MODE_DND_INT;
        } else if (usermode == Presence.Mode.away || usermode == Presence.Mode.xa) {
            userState = Constants.PRESENCE_MODE_AWAY_INT;
        } else if (isOnline) {
            userState = Constants.PRESENCE_MODE_AVAILABLE_INT;
        }

        return userState;

    }

//    //Signup to server
//    public void Signup(SignupModel signupModel) {
//        XMPPError.Condition condition = null;
//        boolean errors = false;
//        String errorMessage = "";
//
//        String mUsername = signupModel.getUsername();
//        String mPassword = signupModel.getPassword();
//
//        boolean isPasswordValid = signupModel.checkPassword();
//        boolean areFieldsValid = signupModel.validateFields();
//
//        if (!isPasswordValid) {
//            errors = true;
//            errorMessage = Constants.SIGNUP_ERR_INVALIDPASS;
//        }
//
//        if (!areFieldsValid) {
//            errors = true;
//            errorMessage = Constants.SIGNUP_ERR_FIELDERR;
//        }
//
//        if (errors) {
//            service.onSignupFailed(errorMessage);
//            return;
//        }
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (!connected && !isconnecting) connect();
//            }
//        }).start();
//
//        try {
//            final AccountManager accountManager = AccountManager.getInstance(connection);
//            accountManager.createAccount(Localpart.from(mUsername), mPassword);
//        } catch (XMPPException | SmackException e) {
//
//            e.printStackTrace();
//            if (debug) Log.e(TAG, "Username: " + mUsername + ",Password: " + mPassword);
//
//            if (e instanceof XMPPException.XMPPErrorException) {
//                condition = ((XMPPException.XMPPErrorException) e).getXMPPError().getCondition();
//            }
//
//            if (condition == null) {
//                condition = XMPPError.Condition.internal_server_error;
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (XmppStringprepException e) {
//            e.printStackTrace();
//        }
//
//        if (condition == null) {
//            service.onSignupSuccess();
//        } else {
//            switch (condition) {
//                case conflict:
//                    errorMessage = Constants.SIGNUP_ERR_CONFLICT;
//                    break;
//                case internal_server_error:
//                    errorMessage = Constants.SIGNUP_ERR_SERVER_ERR;
//                    break;
//                default:
//                    errorMessage = condition.toString();
//                    break;
//
//            }
//
//            service.onSignupFailed(errorMessage);
//        }
//    }

    //Login to server
    public void login2() {
        try {

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
            if (!connected && !isconnecting) {
                new XMPPHandler.ConnectXMPP().execute();
            }
//                }
//            }).start();

            if (debug) Log.i(TAG, "User " + userId + userPassword);

//            if (connection.isAuthenticated() && connection.isConnected()) {
//            connection.login(userId+"@" +AppConfing.SERVICE, userPassword+"@" +AppConfing.SERVICE);
//            connection.login("eprodigyo6pm0w45f91542272066", "eprodigyo6pm0w45f91542272066");
            connection.login(userId, userPassword);


//            if (!roster.isLoaded())
//                try {
//                    roster.reloadAndWait();
//                } catch (SmackException.NotLoggedInException e) {
//                    Log.i(TAG, "NotLoggedInException");
//                    e.printStackTrace();
//                } catch (SmackException.NotConnectedException e) {
//                    Log.i(TAG, "NotConnectedException");
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

//            String jid = null;
//            if (!userId.contains("@")) {
//                jid = userId + "@" + Constants.XMPP_DOMAIN;
//            }
//
//            EntityBareJid jid1 = null;
//            try {
//                jid1 = JidCreate.entityBareFrom(jid);
//                String nickname = XmppStringUtils.parseLocalpart(jid);
//                roster.createEntry(jid1, nickname, null);
//            } catch (XmppStringprepException | SmackException.NoResponseException | SmackException.NotConnectedException | InterruptedException | SmackException.NotLoggedInException | XMPPException.XMPPErrorException e) {
//                e.printStackTrace();
//            }

//            ProviderManager.addExtensionProvider("received", DeliveryReceipt.NAMESPACE, new DeliveryReceiptProvider());

//            ProviderManager.addIQProvider("query", "http://jabber.org/protocol/bytestreams", new BytestreamsProvider());
//            ProviderManager.addIQProvider("query", "http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());
//            ProviderManager.addIQProvider("query", "http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());
//            ProviderManager.addIQProvider("element", "namespace", new BytestreamsProvider());
//
//            IQ iq111 = new IQ("register") {
//                @Override
//                protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
//
//                    xml.rightAngleBracket();
////                    xml.halfOpenElement("key").rightAngleBracket();
////                    xml.append("cB2BH_v8r-Y:APA91bFxTM2EMlU5hSt__mdyz_mK2zRyzsUwJzdL8SnOGMN-iRoTWlfAmsw77AZRz5__4a_7YjE7qJszgWZDicf3g62Sv2hQhBOl9PLwc2XvK_Qksy8_OVPkPZDgcfaPrywvzT31dKaN"");
////                    xml.closeElement("key");
//
//                    xml.element("token", "cB2BH_v8r-Y:APA91bFxTM2EMlU5hSt__mdyz_mK2zRyzsUwJzdL8SnOGMN-iRoTWlfAmsw77AZRz5__4a_7YjE7qJszgWZDicf3g62Sv2hQhBOl9PLwc2XvK_Qksy8_OVPkPZDgcfaPrywvzT31dKaN");
//
//                    return xml;
//                }
//            };
////
//            iq111.setTo("localhost");
////            iq111.setFrom("coinasontmzrpdiplq1522665402@localhost");
//            iq111.setType(IQ.Type.set);
////
//            try {
//                LogM.e("toXML111" + iq111.toXML());
//
//                connection.sendStanza(iq111);
//            } catch (SmackException.NotConnectedException | InterruptedException e) {
//                e.printStackTrace();
//            }
////            String deviceToken = FirebaseInstanceId.getInstance().getToken();
//            HashMap<String, String> publishOptions = new HashMap<>();
//            publishOptions.put("secret", deviceToken);

//
//            PushNotificationsManager pushNotificationsManager = PushNotificationsManager.getInstanceFor(connection);
//
//            boolean isSupported = pushNotificationsManager.isSupportedByServer();
//
//
//            Jid jid = null;
//            try {
//                jid = JidCreate.entityBareFrom("coinasontmzrpdiplq1522665402@localhost");
//
//            } catch (Exception e) {
//
//            }
////
////            pushNotificationsManager.enable(jid, "@localhost", publishOptions);
//

//            IQ iq10 = new IQ("register", "https://fcm.googleapis.com/fcm") {
//                @Override
//                protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
//
//                    Element element = new Element() {
//                        @Override
//                        public CharSequence toXML() {
//
//                            String str =
//                                    "<token>" + FirebaseInstanceId.getInstance().getToken() + "</token>" +
//                                            "</register>";
//
//                            return str;
//                        }
//                    };
//
//                    xml.element(element);
//
//                    return xml;
//                }
//            };

//            Stanza iq = new Stanza() {
//                @Override
//                public String toString() {
//                    return null;
//                }
//
//                @Override
//                public String toXML() {
//
//
//                    String str = "<iq id='595873930'  to='localhost' type='set'>" +
//                            "<register xmlns='https://fcm.googleapis.com/fcm'>" +
//                            "<token>" + FirebaseInstanceId.getInstance().getToken() + "</token>" +
//                            "</register>" +
//                            "</iq>";
//                    Log.e("TAG", str);
//
////                    String str = "<iq type='set' >" +
////                            "<enable xmlns='urn:xmpp:push:0' jid='coinasontmzrpdiplq1522665402@localhost' node='yxs32uqsflafdk3iuqo'>" +
////                            "<x xmlns='jabber:x:data' type='submit'>" +
////                            "<field var='token'>" +
////                            "<value>http://jabber.org/protocol/pubsub#publish-options</value></field>" +
////                            "<value>" + FirebaseInstanceId.getInstance().getToken() + "</value>" +
////                            " </field>" +
////                            "</x>" +
////                            "</enable>" +
////                            "</iq>";
////                    Log.e("TAG", str);
//                    return str;
//                }
////
////
//            };
////
//
//            MyIQ myIQ = new MyIQ();

//
//            try {
//                iq10.setStanzaId();
//                LogM.e("toXML" + iq10.toXML());
//                connection.sendStanza(iq10);
//            } catch (SmackException.NotConnectedException | InterruptedException e) {
//                e.printStackTrace();
//            }

//            if (connection != null) {
//                OfflineMessageManager offlineMessageManager = new OfflineMessageManager(connection);
//                if (offlineMessageManager.supportsFlexibleRetrieval() &&
//                        offlineMessageManager.getMessageCount() > 0) {
//
//                    List<Message> mList = offlineMessageManager.getMessages();
//                    if (mList.size() > 0) {
//                        for (Message message : mList) {
//
//                            LogM.e("offlineMessageManager" + message.getType());
//                            LogM.e("offlineMessageManager" + message.getBody());
//                            LogM.e("offlineMessageManager" + message.getFrom());
//                            LogM.e("offlineMessageManager" + message.getTo());
//                        }
//                    } else {
//                        LogM.e("offlineMessageManager size 0");
//                    }
//                } else {
//                    LogM.e("offlineMessageManager else");
//                }
//            }
            setUserStatus(Presence.Mode.available);


            if (debug) Log.i(TAG, "Yey! We're logged in to the Xmpp server!");

            service.onLoggedIn();
        } catch (XMPPException | IOException e) {
            LogM.e("13" + e.getMessage());
            service.onLoginFailed();
            if (debug) e.printStackTrace();
        } catch (SmackException e) {
            if (debug) e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            LogM.e("12" + e.getMessage());

        }
    }

    public void login() {
        try {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!connected && !isconnecting) connect();
                }
            }).start();

            if (debug) Log.i(TAG, "User " + userId + userPassword);

            try {
                connection.login(userId, userPassword);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (debug) Log.i(TAG, "Yey! We're logged in to the Xmpp server!");

            service.onLoggedIn();
        } catch (XMPPException | IOException e) {

            service.onLoginFailed();
            if (debug) e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        }
    }

    public static class LoginTask extends AsyncTask<Void, Void, Boolean> {
        private String username;
        private String password;
        private CustomProgressDialog customProgressDialog;

        public LoginTask(Activity mActivity, String username, String password) {

            this.username = username;
            this.password = password;

            //customProgressDialog = new CustomProgressDialog();
           // customProgressDialog.showCustomDialog(mActivity);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!connected && !isconnecting) connect();
                    }
                }).start();

                if (debug) Log.i(TAG, "User " + username + password);

                try {
                    connection.login(username, password);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (debug) Log.i(TAG, "Yey! We're logged in to the Xmpp server!");

                service.onLoggedIn();
            } catch (XMPPException | IOException e) {

                service.onLoginFailed();
                if (debug) e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            }


            return isconnecting = false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            //customProgressDialog.dismissCustomDialog();

        }
    }

    //Update which chat instance is running currently. Set it to true.
    public void updateChatEntryMap(String key) {
        for (Map.Entry<String, Boolean> entry : chat_created_for.entrySet()) {
            entry.setValue(entry.getKey().equals(key));
        }
    }

    // Send Message (you can call this method from other activity)
    // This method will try to create connection (if not established already),
    // will open up a TCP connection to another user (usually a roaster in jabber language),
    // will throw exception if there is an error
    public boolean sendMessage(final ChatPojo chatMessage) throws SmackException {
        final String body = gson.toJson(chatMessage);
        // String body = chatMessage.getChatText();

        if (chat_created_for.get(chatMessage.getChatRecv()) == null)
            chat_created_for.put(chatMessage.getChatRecv(), false);

        if (!chat_created_for.get(chatMessage.getChatRecv())) {

            Log.e(TAG, "jusabtsend:" + chatMessage.getChatRecv());


            mChat = org.jivesoftware.smack.chat2.ChatManager.getInstanceFor(connection);

            EntityBareJid jid;
            try {
                jid = JidCreate.entityBareFrom(chatMessage.getChatRecv() + "@localhost");
                chat = mChat.chatWith(jid);

            } catch (XmppStringprepException e) {
                e.printStackTrace();
                LogM.e("11" + e.getMessage());

                return false;
            }

            updateChatEntryMap(chatMessage.getChatRecv());
            chat_created_for.put(chatMessage.getChatRecv(), true);
        }

        final Message message = new Message();
//        Log.e(TAG, "Message");
        message.setBody(body);
        message.setType(Type.chat);

//        //mediaType
//        message.addExtension(new ExtensionElement() {
//            @Override
//            public String getNamespace() {
//                return "urn:xmpp:mediatype";
//            }
//
//            @Override
//            public String getElementName() {
//                return "mediaType";
//            }
//
//            @Override
//            public CharSequence toXML() {
//                return "<mediaType>" + "chat" + "</mediaType>";
//            }
//
//
//        });


        /*Stanza msgStanza = new Stanza() {
            @Override
            public String toString() {
                return null;
            }

            @Override
            public String toXML() {
//
                String str = "<message to='" + chatMessage.getChatRecv() + "' type='chat' id='" + chatMessage.getChatId() + "'>" +
                        "<messageId>" + chatMessage.getMsgId() + "</messageId>" +
                        "<mediaType>chat</mediaType>" +
                        "<messageAction>New</messageAction>" +
                        "<body>" + chatMessage.getChatText() + "</body></message>";




                String new1 = "<message to='eprodigy8xs3hgu1iw1543554629@localhost/36459054677700487981780830' id='2zZ6z-47' type='chat'>" +
                        "<body>=========================</body>" +
                        "<mediaType>chat</mediaType>" +
                        "<request" +
                        "xmlns='urn:xmpp:receipts'/>" +
                        "<request" +
                        "xmlns='urn:xmpp:receipts'/>" +
                        "</message>" +
                        "<r" +
                        "xmlns='urn:xmpp:sm:3'/>"
                       ;

                String tst = "<message to=eprodigyiqsv6pvxc61543822451@localhost/27684856509976271501780664 id=NqP07-41>"+
   "<mediaType>chat</mediaType>"+
   "<request xmlns=urn:xmpp:receipts />"+
   "<request xmlns=urn:xmpp:receipts /></message>";
                Log.e("TAG", str);



                return new1;
            }
        };*/

        //    message.addBody("type","attachment");
        //  message.addBody("thumb","thumb url");


        try {
            if (connection.isAuthenticated() && connection.isConnected()) {

                DeliveryReceiptManager.receiptMessageFor(message);
                final String msgReceipt = DeliveryReceiptRequest.addTo(message);
                LogM.e("msgReceipt" + msgReceipt);
                chat.send(message);
            } else {
                login();
                return false;
            }
        } catch (SmackException.NotConnectedException e) {
            LogM.e("10" + e.getMessage());

            if (debug) Log.e(TAG, "msg Not sent!-Not Connected!");
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            LogM.e("9" + e.getMessage());

            if (debug) Log.e(TAG, "msg Not sent!" + e.getMessage());
            return false;
        }


        return true;
    }


    // Our own connection Listener
    // Here, you can handle several connection events in our own way
    public class XMPPConnectionListener implements ConnectionListener {

        //We are connected, now we can login
        @Override
        public void connected(final XMPPConnection connection) {

            if (debug) Log.d(TAG, "Connected!");

            service.onConnected();
            connected = true;

            if (!connection.isAuthenticated() && autoLogin) {
                login();
            }
        }

        //Our connection has closed. reset everything here and alert user.
        @Override
        public void connectionClosed() {

            service.onConnectionClosed();

            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

//                        Toast.makeText(service, "ConnectionCLosed!",
//                                Toast.LENGTH_SHORT).show();

                    }
                });

            if (debug) Log.d(TAG, "ConnectionCLosed!");

            connected = false;
            //    chat_created = false;
            chatInstanceIterator(chat_created_for);
            loggedin = false;
        }

        //Our connection has closed, due to error. Still, it is same thing as above. Reset everything
        @Override
        public void connectionClosedOnError(Exception arg0) {

            service.onConnectionClosed();

            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
//                        Toast.makeText(service, "ConnectionClosedOn Error!!",
//                                Toast.LENGTH_SHORT).show();

                    }
                });

            if (debug) Log.d(TAG, "ConnectionClosedOn Error!");

            connected = false;
            chatInstanceIterator(chat_created_for);
            loggedin = false;
        }

        @Override
        public void reconnectingIn(int arg0) {

            service.onReConnection();
            if (debug) Log.d(TAG, "Reconnectingin " + arg0);
            loggedin = false;
        }

        // Our reconnection attemp failed. Reset everything. Basically, we reset whenever our connection failed,
        // no matter whatever the cause is
        @Override
        public void reconnectionFailed(Exception arg0) {

            service.onReConnectionError();

            if (debug) Log.d(TAG, "ReconnectionFailed!");

            //Reset the variables
            connected = false;
            chatInstanceIterator(chat_created_for);
            loggedin = false;
        }

        //Below two methods are quite useful. These handles a successfull connection attempt.
        @Override
        public void reconnectionSuccessful() {

            service.onReConnected();

            if (debug) Log.d(TAG, "ReconnectionSuccessful");

            //We are only connected, not authenticated yet. See the next method
            connected = true;
            chatInstanceIterator(chat_created_for);
            loggedin = false;
        }

        //This is main method, we authentication stuff happens
        @Override
        public void authenticated(XMPPConnection connectionNew, boolean resumed) {

            chatInstanceIterator(chat_created_for);
            loggedin = true;

            setUserStatus(Presence.Mode.available);


            org.jivesoftware.smack.chat2.ChatManager.getInstanceFor(connection)
                    .addIncomingListener(mChatManagerListener);
//
//            Stanza iq = new Stanza() {
//                @Override
//                public String toString() {
//                    return null;
//                }
//
//                @Override
//                public String toXML() {
//                    String str = "<iq to='localhost' type='set'>" +
//                            "<register xmlns='https://fcm.googleapis.com/fcm'>" +
//                            "<key>" + FirebaseInstanceId.getInstance().getToken() + "</key>" +
//                            " </register>" +
//                            "</iq>";
//                    Log.e("TAG", str);
//                    return str;
//                }
//            };
//
//            try {
//                LogM.e("toXML" + iq.toXML());
//                connection.sendStanza(iq);
//            } catch (SmackException.NotConnectedException | InterruptedException e) {
//                e.printStackTrace();
//            }


            //Wait for 500ms before showing we are authenticated
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        LogM.e("8" + e.getMessage());

                    }

                }
            }).start();

            if (debug) Log.d(TAG, "Yay!! We are now authenticated!!");

            service.onAuthenticated();
        }

    }

    //Dummy method. Checks if roster belongs to one of our user
    public Boolean checkSender(String roasterModel, String user) {

        BareJid jid;
        try {
            jid = JidCreate.entityBareFrom(user);
            Presence presence = roster.getPresence(jid);
            return (presence.getFrom().toString().contains(roasterModel));
        } catch (XmppStringprepException e) {
            e.printStackTrace();
            LogM.e("7" + e.getMessage());

            return false;
        }


    }
/*    private class GroupChatListener implements  InvitationListener{
        String nickname;
        public GroupChatListener(String nick)
        {
            nickname = nick;
        }

        @Override
        public void invitationReceived(XMPPConnection conn, MultiUserChat room, EntityJid inviter, String reason, String password, Message message, MUCUser.Invite invitation) {
            System.out.println(" Entered invitation handler... ");
            try
            {
                MultiUserChat chatRoom = new MultiUserChat(connection, room);
                chatRoom.join(nickname);
            }

            catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException e)
            {
                e.printStackTrace();
            } catch (SmackException e)
            {
                e.printStackTrace();
            }
            System.out.println(" Invitation Accepted... ");
        }
    }*/
    //Your own Chat Manager. We attach the message events here
    private class MyChatManagerListener implements IncomingChatMessageListener {

        @Override
        public void newIncomingMessage(EntityBareJid from, Message message, org.jivesoftware.smack.chat2.Chat chat) {

//            Proceed only if we have a message and its a chat
            try {

                System.out.println("getType" + message.getType());
                System.out.println("getBody" + message.getBody());

                ExtensionElement mediaTypeExt = message.getExtension("urn:xmpp:mediatype");

                if (message.getType() == Type.chat && message.getBody() != null) {


                    LogM.e("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    LogM.e("from: " + message.getFrom());
                    LogM.e("xml: " + message.getType().toString());
                    LogM.e("Got text [" + message.getBody() + "] from [" + message.getFrom() + "]");
                    LogM.e("newIncomingMessage: " + from);

                    LogM.e("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                    String username = from.toString().replace("@", "").replace(AppConfing.SERVICE, "");
                    //updateDB(username, message.getBody(), username);

                    Gson gson = new Gson();
                    ChatPojo chatMessage = gson.fromJson(message.getBody(), ChatPojo.class);

                    updateDB(username, chatMessage, username);
                    //Now our message is in our representation, we can send it to our list to add newly received message

                } else if (message.getType() == Type.error) {
                    Toast.makeText(service, "error type", Toast.LENGTH_SHORT).show();

                } else if (message.getType() == Type.groupchat) {
                    Toast.makeText(service, "groupchat type", Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
                LogM.e("1111" + e.getMessage());
            }
        }


    }

    public  void updateDB(String username, String message, int chatType, int header, String chatheader, boolean isGroup) {
        ChatPojo chatPojo = new ChatPojo();


        if (isGroup) chatPojo.setChatId(AppConfing.grpID + chatheader);
        else chatPojo.setChatId(AppConfing.chatID + mUser + "_" + chatheader);



        chatPojo.setChatSender(username);//from
        chatPojo.setChatRecv(mUser);
        chatPojo.setChatText(message);
        chatPojo.setShowing(false);
        //chatPojo.setChatType(header);
        chatPojo.setChatTimestamp(SCUtils.getNow());
        addMessage(chatPojo);

    }
    private void updateDB(String username, ChatPojo chatPojo, String s) {

        /*ChatPojo chatPojo = new ChatPojo();
//        chatPojo.setChatId(AppConfing.chatID + mUser + "_" + s);//to
        chatPojo.setChatId(s);//to
        chatPojo.setChatSender(username);//from
        chatPojo.setChatRecv(mUser);
        chatPojo.setChatText(msg);
        chatPojo.setShowing(false);
        chatPojo.setChatTimestamp(SCUtils.getNow());*/


//        DataManager.getInstance().AddChat(chatPojo);
//
//        DataManager.getInstance().updateTimestamp(username, SCUtils.getNo
        Log.e(TAG, "updateDB >" + chatPojo.getMsgType() + "==" + chatPojo.getChatText());


        chatPojo.setChatId(AppConfing.chatID + mUser + "_" + s);
        chatPojo.setChatRecv(mUser);//to
        chatPojo.setChatSender(username);//from
        chatPojo.setMine(false);
        chatPojo.setChatTimestamp(SCUtils.getNow());
        addMessage(chatPojo);

    }
  /*  private void updateDB(String username, String msg, String s) {

        ChatPojo chatPojo = new ChatPojo();
//        chatPojo.setChatId(AppConfing.chatID + mUser + "_" + s);//to
        chatPojo.setChatId(s);//to
        chatPojo.setChatSender(username);//from
        chatPojo.setChatRecv(mUser);
        chatPojo.setChatText(msg);
        chatPojo.setShowing(false);
        chatPojo.setChatTimestamp(SCUtils.getNow());
//        DataManager.getInstance().AddChat(chatPojo);
//
//        DataManager.getInstance().updateTimestamp(username, SCUtils.getNow());

        addMessage(chatPojo);


    }*/

    private void addMessage(final ChatPojo chatMessage) {
        service.onNewMessage(chatMessage);
    }


    public static String lastSeen(String chatUser) {

        BareJid jid = null;

        try {

            if (!chatUser.contains("@")) {

                chatUser = chatUser + "@" + Constants.XMPP_DOMAIN;
            }

            jid = JidCreate.entityBareFrom(chatUser);


            if (lastActivityManager == null) {
                lastActivityManager = LastActivityManager.getInstanceFor(connection);
            }
            Long time;
            try {
                time = lastActivityManager.getLastActivity(jid).getIdleTime();
                return time.toString();
            } catch (SmackException.NoResponseException e) {

                return "";

            } catch (InterruptedException | SmackException.NotConnectedException | XMPPException.XMPPErrorException e) {
                e.printStackTrace();
                return "";
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
            return "";

        }
    }

    //Now our message Listener. We can now see how to parse XMPP messages received from other users or server.
    private class MyMessageListener implements MessageListener, ChatStateListener {

        private String TAG = getClass().getSimpleName();

        public MyMessageListener() {

        }


        @Override
        public void stateChanged(Chat chat, ChatState state, Message message) {
            if (debug)
                Log.e(TAG, "Chat State local: " + chat.getParticipant() + ": " + state.name());

            if (state.toString().equals(ChatState.composing.toString())) {
                if (debug) Log.e(TAG, "User is typing");
            } else if (state.toString().equals(ChatState.paused.toString())) {
                if (debug) Log.e(TAG, "User is paused");
            } else if (state.toString().equals(ChatState.active.toString())) {
                if (debug) Log.e(TAG, "User is active");
            } else if (state.toString().equals(ChatState.gone.toString())) {
                if (debug) Log.e(TAG, "User is away");
            } else if (state.toString().equals(ChatState.inactive.toString())) {
                if (debug) Log.e(TAG, "User is inactive");
            } else {
                if (debug) Log.e(TAG, "User is nothing");
            }

            service.onChatStateChange(new ChatStateModel(chat.getParticipant().asEntityBareJidString(), state));
        }


        @Override
        public void processMessage(Message message) {

        }

        @Override
        public void processMessage(Chat chat, Message message) {

        }
    }


    /**
     * set User's status
     *
     * @param status online, away, DND
     */

    public static void setUserStatus(Presence.Mode status) {
        if (connection != null) {
            try {
                Presence presence = new Presence(Presence.Type.available);
                presence.setStatus("Online");
                presence.setPriority(24);
                presence.setMode(Presence.Mode.available);
                connection.sendStanza(presence);


            } catch (SmackException.NotConnectedException | InterruptedException e) {
                e.printStackTrace();
                LogM.e("6" + e.getMessage());

            }
        }
    }


    private class MyStanzaListener implements StanzaListener {

        @Override
        public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {

            //only filter Presence packets
            try {


//                connection.sendStanza(new Ping((Jid) packet));

                // message successfully send or not check here
                if (packet instanceof Message) {
                    Message message = (Message) packet;
                    if (message.getType().equals(Type.error)) {
                        LogM.e("Error sending message");
                    }
                }

                LogM.e("processStanza" + packet.toXML());
                if (packet instanceof Presence) {
                    Presence presence = (Presence) packet;

                    if (presence.getMode() != null) {
                        LogM.e("" + presence.getMode());
                    }
                    final String fromJID = presence.getFrom().asBareJid().toString();

                    /* We got a request (subscription req). We need to send back "subscribe/subscribed"or "unsubscribe" based on
                     * user choice. We will show user asking him to "accept" or "reject".
                     */
                    //@see: http://xmpp.org/rfcs/rfc6121.html#sub-request
                    if (((Presence) packet).getType() == Presence.Type.available) {
                        Log.e(TAG, "----------------------------------------------");
                    }

                    if (presence.getType() == Presence.Type.subscribe) {

                        if (debug) Log.e(TAG, "subscription request from - " + fromJID);
                        service.onRequestSubscribe(fromJID);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogM.e("123" + e.getMessage());
            }
        }
    }

    private class MyRosterListener implements RosterListener {

        @Override
        public void entriesAdded(Collection<Jid> addresses) {

            if (debug) Log.e(TAG, "Entry added! ");

            Iterator addressIter = addresses.iterator();
            while (addressIter.hasNext()) {
                if (debug) Log.e(TAG, "Entry added: " + addressIter.next());
            }
        }

        @Override
        public void entriesUpdated(Collection<Jid> addresses) {

            if (debug) Log.e(TAG, "Entry updated! ");

            Iterator addressIter = addresses.iterator();
            while (addressIter.hasNext()) {
                if (debug) Log.e(TAG, "Entry updated: " + addressIter.next());
            }
        }

        @Override
        public void entriesDeleted(Collection<Jid> addresses) {

            if (debug) Log.e(TAG, "Entry deleted! ");

            Iterator addressIter = addresses.iterator();
            while (addressIter.hasNext()) {
                if (debug) Log.e(TAG, "Entry deleted: " + addressIter.next());
            }
        }

        /* This is a good place to know whenever a user went online/offline. Use this method
         * to call any of your singleton, pub-subs etc to let know your UI to change user presence
         */
        @Override
        public void presenceChanged(Presence presence) {
            if (debug)
                Log.e(TAG, "Presence changed: " + presence.getFrom() + " " + presence.getStatus());
            Presence.Mode mode = presence.getMode();
            int status = retreiveState(mode, presence.isAvailable());
            service.onPresenceChange(new PresenceModel(presence.getFrom().toString(), presence.getStatus(), mode, status));
        }

    }

    /*
     * Whenever a user sends a subscription request, you have to send him back two subscription requests:
     * 1. Presence.Type.subscribe
     * 2. Presence.Type.subscribed (you are now subscribed to user, at this point user can see your presence,
     *    but you can not see his presence)
     *
     */
    public void confirmSubscription(String fromJID, boolean shouldSubscribe) {

        BareJid jid = null;
        try {
            jid = JidCreate.entityBareFrom(fromJID);

            final RosterEntry newEntry = roster.getEntry(jid);
            //Prepare "subscribe" precense
            Presence subscribe = new Presence(Presence.Type.subscribe);
            subscribe.setTo(jid);

            //Prepare "subscribed" precense
            Presence subscribed = new Presence(Presence.Type.subscribed);
            subscribed.setTo(jid);

            //Prepare Unsubscribe
            Presence unsubscribe = new Presence(Presence.Type.unsubscribe);
            unsubscribe.setTo(jid);

            //Send both (or only subscribed, if user has already sent request)
            try {
                if (shouldSubscribe) {
                    if (newEntry == null || newEntry.getType() == RosterPacket.ItemType.from) {
                        connection.sendStanza(subscribed);
                        connection.sendStanza(subscribe);
                    } else {
                        connection.sendStanza(subscribed);
                    }
                } else {
                    connection.sendStanza(unsubscribe);
                }
            } catch (SmackException e) {
                e.printStackTrace();
                LogM.e("1" + e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
                LogM.e("2" + e.getMessage());

            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
            LogM.e("5" + e.getMessage());

        }
    }

    //Chat State events

    public void updateChatStatus(String receiver, ChatState chatState) {

        Chat chat1 = null;

        //if (chat_created_for.get(receiver) == null)
        chat_created_for.put(receiver, false);

        if (!chat_created_for.get(receiver)) {

            EntityBareJid jid;
            try {
                jid = JidCreate.entityBareFrom(receiver + "@" + Constants.XMPP_DOMAIN);
                chat1 = ChatManager.getInstanceFor(connection)
                        .createChat(
                                jid, mMessageListener);
                updateChatEntryMap(receiver);
                chat_created_for.put(receiver, true);
            } catch (XmppStringprepException e) {
                e.printStackTrace();
                LogM.e("3" + e.getMessage());

            }


            try {
                if (connection.isAuthenticated()) {
                    assert chat1 != null;
                    ChatStateManager.getInstance(connection).setCurrentState(chatState, chat1);

                }
            } catch (SmackException.NotConnectedException e) {
                if (debug) Log.e(TAG, "status Not sent!-Not Connected!");
                LogM.e("4" + e.getMessage());

            } catch (Exception e) {
                e.printStackTrace();
                LogM.e("41" + e.getMessage());
                if (debug) Log.e(TAG, "status Not sent!" + e.getMessage());
            }
        }
    }
}
