package com.bacancy.eprodigy.xmpp;


import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.Models.ChatStateModel;
import com.bacancy.eprodigy.Models.PresenceModel;

public class XmppCustomEventListener implements XMPPEventReceiver.XmppCustomEventListenerBase {

    public void onNewMessageReceived(ChatPojo chatItem) {}
    public void onLoggedIn() {}
    public void onSignupSuccess() {}
    public void onSignupFailed(String error) {}
    public void onAuthenticated() {}
    public void onReConnectionError() {}
    public void onConnected() {}
    public void onReConnected() {}
    public void onConnectionClosed() {}
    public void onReConnection() {}
    public void onLoginFailed() {}
    public void onPresenceChanged(int presenceModel) {}
    public void onPresenceChanged(PresenceModel presenceModel) {}
    public void onChatStateChanged(ChatStateModel chatStateModel){}
    public void onSubscriptionRequest(String fromuser){}
}
