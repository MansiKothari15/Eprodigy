package com.bacancy.eprodigy.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by samir on 27/2/18.
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

    public String getChatImage() {
        return chatImage;
    }

    public void setChatImage(String chatImage) {
        this.chatImage = chatImage;
    }

    public ChatPojo(){
    }

    protected ChatPojo(Parcel in) {
        msgId = in.readInt();
        chatId = in.readString();
        chatText = in.readString();
        chatTimestamp = in.readString();
        chatSender = in.readString();
        chatRecv = in.readString();
        isShowing = in.readByte() != 0;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(msgId);
        parcel.writeString(chatId);
        parcel.writeString(chatText);
        parcel.writeString(chatTimestamp);
        parcel.writeString(chatSender);
        parcel.writeString(chatRecv);
        parcel.writeByte((byte) (isShowing ? 1 : 0));
    }
}
