package com.bacancy.eprodigy.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by samir on 27/2/18.
 */
@Entity(tableName = "StarredMssagePojo")
public class StarredMssagePojo {

    @PrimaryKey
    @ColumnInfo(name = "msgId")
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
}
