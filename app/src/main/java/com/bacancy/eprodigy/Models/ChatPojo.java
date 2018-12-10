package com.bacancy.eprodigy.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bacancy on 27/2/18.
 */
@Entity(tableName = "ChatPojo")
public class ChatPojo implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int msgId;

    @ColumnInfo(name = "chatId")
    private String chatId;

    @ColumnInfo(name = "chatText")
    private String chatText;

    @ColumnInfo(name = "chatTimestamp")
    private String chatTimestamp;

    @ColumnInfo(name = "chatSender")
    private String chatSender;

    @ColumnInfo(name = "chatRecv")
    private String chatRecv;

    @ColumnInfo(name = "isShowing")
    private boolean isShowing;

    @ColumnInfo(name = "chatImage")
    private String chatImage;

    @ColumnInfo(name = "sharedContactSenderName")
    private String sharedContactSenderName;

    @ColumnInfo(name = "sharedContactSenderNumber")
    private String sharedContactSenderNumber;

    @ColumnInfo(name = "sharedContactSenderImage")
    private String sharedContactSenderImage;


    @ColumnInfo(name = "sharedContactRecvName")
    private String sharedContactRecvName;

    @ColumnInfo(name = "sharedContactRecvNumber")
    private String sharedContactRecvNumber;

    @ColumnInfo(name = "sharedContactRecvImage")
    private String sharedContactRecvImage;

    @ColumnInfo(name = "msgType")
    private int msgType;

    @ColumnInfo(name = "isMine")
    private boolean isMine=false;

    public ChatPojo(){
    }

    protected ChatPojo(Parcel in) {
        msgId = in.readInt();
        msgType = in.readInt();
        chatId = in.readString();
        chatText = in.readString();
        chatTimestamp = in.readString();
        chatSender = in.readString();
        chatRecv = in.readString();
        isShowing = in.readByte() != 0;
        isMine = in.readByte() != 0;
        chatImage = in.readString();
        sharedContactSenderName = in.readString();
        sharedContactSenderNumber = in.readString();
        sharedContactSenderImage = in.readString();
        sharedContactRecvName = in.readString();
        sharedContactRecvNumber = in.readString();
        sharedContactRecvImage = in.readString();
    }

    public static final Creator<ChatPojo> CREATOR = new Creator<ChatPojo>() {
        @Override
        public ChatPojo createFromParcel(Parcel in) {
            return new ChatPojo(in);
        }

        @Override
        public ChatPojo[] newArray(int size) {
            return new ChatPojo[size];
        }
    };

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatText() {
        return chatText;
    }

    public void setChatText(String chatText) {
        this.chatText = chatText;
    }

    public String getChatTimestamp() {

        return chatTimestamp;
    }

    public void setChatTimestamp(String chatTimestamp) {
        this.chatTimestamp = chatTimestamp;
    }

    public String getChatSender() {
        return chatSender;
    }

    public void setChatSender(String chatSender) {
        this.chatSender = chatSender;
    }

    public String getChatRecv() {
        return chatRecv;
    }

    public void setChatRecv(String chatRecv) {
        this.chatRecv = chatRecv;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String getSharedContactSenderImage() {
        return sharedContactSenderImage;
    }

    public void setSharedContactSenderImage(String sharedContactSenderImage) {
        this.sharedContactSenderImage = sharedContactSenderImage;
    }

    public String getSharedContactRecvName() {
        return sharedContactRecvName;
    }

    public void setSharedContactRecvName(String sharedContactRecvName) {
        this.sharedContactRecvName = sharedContactRecvName;
    }

    public String getSharedContactRecvNumber() {
        return sharedContactRecvNumber;
    }

    public void setSharedContactRecvNumber(String sharedContactRecvNumber) {
        this.sharedContactRecvNumber = sharedContactRecvNumber;
    }

    public String getSharedContactRecvImage() {
        return sharedContactRecvImage;
    }

    public void setSharedContactRecvImage(String sharedContactRecvImage) {
        this.sharedContactRecvImage = sharedContactRecvImage;
    }

    public String getChatImage() {
        return chatImage;
    }

    public void setChatImage(String chatImage) {
        this.chatImage = chatImage;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getSharedContactSenderName() {
        return sharedContactSenderName;
    }

    public void setSharedContactSenderName(String sharedContactSenderName) {
        this.sharedContactSenderName = sharedContactSenderName;
    }

    public String getSharedContactSenderNumber() {
        return sharedContactSenderNumber;
    }

    public void setSharedContactSenderNumber(String sharedContactSenderNumber) {
        this.sharedContactSenderNumber = sharedContactSenderNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(msgId);
        parcel.writeInt(msgType);
        parcel.writeString(chatId);
        parcel.writeString(chatText);
        parcel.writeString(chatTimestamp);
        parcel.writeString(chatSender);
        parcel.writeString(chatRecv);
        parcel.writeByte((byte) (isShowing ? 1 : 0));
        parcel.writeByte((byte) (isMine ? 1 : 0));
        parcel.writeString(chatImage);
        parcel.writeString(sharedContactSenderName);
        parcel.writeString(sharedContactSenderNumber);
        parcel.writeString(sharedContactSenderImage);
        parcel.writeString(sharedContactRecvName);
        parcel.writeString(sharedContactRecvNumber);
        parcel.writeString(sharedContactRecvImage);
    }



}
