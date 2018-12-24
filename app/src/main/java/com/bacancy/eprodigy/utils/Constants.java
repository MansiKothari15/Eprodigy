package com.bacancy.eprodigy.utils;


public class Constants {

    /*Message Types*/


    public static final int PICK_CAMERA_IMAGE = 10;
    public static final int PICK_SINGLE_GALLERY_IMAGE = 20;
    public static final int PICK_MULTIPLE_GALLERY_IMAGE = 30;

    public static final int SUCCESS_STATUS = 101;
    public static final int SESSION_TIME_OUT_STATUS = 401;
    public static final int ERRO_STATUS = 400;
    public static final int RESPONSE_SUCCESS_STATUS = 200;
    public static final int OUR_USERS_STATUS = 1;
    public static final String CONTACT_SPLIT_KEY = ",";

    public static String grp_name = "latestgrp4";
    public static final String SMACk = "/Smack";

    public static final int TYPE_MESSAGE = 111;
    public static final int TYPE_CONTACT = 112;
    public static final int TYPE_IMAGE =113;
    public static final int TYPE_LOCATION =114;
    public static final int TYPE_AUDIO =115;
    public static final int TYPE_VIDEO =116;


/*    public static final String TYPE_MESSAGE = "chat";
    public static final String TYPE_CONTACT = "contact";
    public static final String TYPE_IMAGE ="attachement";
    public static final String TYPE_LOCATION ="location";
    public static final String TYPE_AUDIO ="audio";
    public static final String TYPE_VIDEO ="video";*/



    //XMPP Connection specific constants
    public static final String XMPP_DOMAIN = "localhost";
    public static final String XMPP_HOST = "158.69.205.234";
    public static final String GRP_SERVICE = "conference."+XMPP_DOMAIN;
    //    public static final String XMPP_HOST = "192.168.1.82";
    public static final int XMPP_PORT = 5222;
    public static final String XMPP_RESOURCE = "xmppdemo";
    public static final boolean XMPP_DEBUG = true;

    // Event specific constants
    public static final String EVT_SIGNUP_SUC = "xmpp_signup_success";
    public static final String EVT_SIGNUP_ERR = "xmpp_signup_error";
    public static final String EVT_LOGGED_IN = "xmpp_logged_in";
    public static final String EVT_NEW_MSG = "xmpp_new_msg";
    public static final String EVT_AUTH_SUC = "xmpp_auth_success";
    public static final String EVT_RECONN_ERR = "xmpp_recon_error";
    public static final String EVT_RECONN_WAIT = "xmpp_recon_wait";
    public static final String EVT_RECONN_SUC = "xmpp_recon_success";
    public static final String EVT_CONN_SUC = "xmpp_conn_success";
    public static final String EVT_CONN_CLOSE = "xmpp_conn_close";
    public static final String EVT_LOGIN_ERR = "xmpp_login_error";
    public static final String EVT_PRESENCE_CHG = "xmpp_presence_change";
    public static final String EVT_CHATSTATE_CHG = "xmpp_chatstate_change";
    public static final String EVT_REQUEST_SUBSCRIBE = "xmpp_request_subscribe";

    //Intent Keys constants
    public static final String INTENT_KEY_NEWMSG = "newmsg";
    public static final String INTENT_KEY_PRESENCE = "presence";
    public static final String INTENT_KEY_CHATSTATE = "chatstate";
    public static final String INTENT_KEY_USER = "chat state user";
    public static final String INTENT_KEY_NEWREQUEST = "newrequest";


    public static final String INTENT_KEY_SIGNUP_ERR = "signuperror";
    //Presence States (Strings)
    public static final String PRESENCE_MODE_AVAILABLE = "Online";
    public static final String PRESENCE_MODE_AWAY = "Away";
    public static final String PRESENCE_MODE_CHAT = "Available for chat";
    public static final String PRESENCE_MODE_DND = "Do not disturb";

    public static final String PRESENCE_MODE_XA = "Offline";
    //Presence States (int)
    public static final int PRESENCE_MODE_AVAILABLE_INT = 1;
    public static final int PRESENCE_MODE_AWAY_INT = 2;
    public static final int PRESENCE_MODE_DND_INT = 3;

    public static final int PRESENCE_MODE_OFFLINE_INT = 0;
    //Chat states Strings
    public static final String CHAT_MODE_TYPING = "typing...";
    public static final String CHAT_MODE_PAUSED = "paused";
    public static final String CHAT_MODE_LEFT = "left conversation";

    public static final String CHAT_MODE_ACTIVE = "active";

    //For now long, after user has finished typing, should we wait to mark his typing status "Paused"?
    public static final int PAUSE_THRESHOLD = 2; //in seconds
    //Signup Error messages
    public static final String SIGNUP_ERR_INVALIDPASS = "Password invalid";
    public static final String SIGNUP_ERR_FIELDERR = "Some Fields are empty";
    public static final String SIGNUP_ERR_CONFLICT = "User already exist";
    public static final String SIGNUP_ERR_SERVER_ERR = "Internal server error";
    public static final String SIGNUP_ERR_NOT_SUPPORTED = "Account creation is not supported by server";


}
