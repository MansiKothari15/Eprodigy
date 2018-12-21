package com.bacancy.eprodigy.db;

import android.arch.lifecycle.LiveData;

import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.Models.RecentChatUserPojo;
import com.bacancy.eprodigy.Models.StarredMssagePojo;
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;

import java.util.List;

/**
 * Created by vishal patel on 2/3/2018.
 */

public class DataManager implements IDataManager {

    private DataBaseHelper mDataBaseHelper;
    private static volatile DataManager mDataManager;

    private DataManager() {
        if (mDataManager != null)
            throw new IllegalStateException("Illegal state of DataManager");
        else {
            // initialize all data sources;
            mDataBaseHelper = DataBaseHelper.getInstance();
        }
    }

    public static DataManager getInstance() {
        if (mDataManager == null) {
            synchronized (DataManager.class) {
                if (mDataManager == null) {
                    mDataManager = new DataManager();
                }
            }
        }
        return mDataManager;
    }


    @Override
    public void AddChat(final ChatPojo ChatPojo) {
        mDataBaseHelper.runInTransaction(new Runnable() {
            @Override
            public void run() {
                mDataBaseHelper.getChatDao().Add(ChatPojo);
            }
        });
    }

    @Override
    public LiveData<List<ChatPojo>> getAll(String form) {
//String form, String to
        return mDataBaseHelper.getChatDao().getAll(form);
    }

    @Override
    public void SingleDelete(ChatPojo ChatPojo) {

        mDataBaseHelper.getChatDao().singledelete(ChatPojo);
    }

    @Override
    public void AddUser(final ContactListResponse.ResponseDataBean userPojo) {

        mDataBaseHelper.runInTransaction(new Runnable() {
            @Override
            public void run() {
                mDataBaseHelper.getUserPojo().Add(userPojo);
            }
        });
    }

    @Override
    public LiveData<List<ContactListResponse.ResponseDataBean>> getAllUser() {
        return mDataBaseHelper.getUserPojo().getAll();
    }

    @Override
    public ContactListResponse.ResponseDataBean getUser(String username) {
        return mDataBaseHelper.getUserPojo().getSingle(username);
    }

    @Override
    public void UpdateUser(ContactListResponse.ResponseDataBean userPojo) {
        mDataBaseHelper.getUserPojo().update(userPojo);
    }

    @Override
    public List<String> getChatUserList() {
        return mDataBaseHelper.getChatDao().getRecentChatUserList();
    }

    @Override
    public List<String> getChatUserListNew() {
        return mDataBaseHelper.getChatDao().getChatUserSendList();
    }

    @Override
    public LiveData<List<ContactListResponse.ResponseDataBean>> getChatUserListById(List<String> chatList) {
        return mDataBaseHelper.getUserPojo().getAllbyIds(chatList);
    }

    @Override
    public LiveData<List<ChatPojo>> getRecentChatUserListById(List<String> chatList) {
        return mDataBaseHelper.getChatDao().getRecentChatAll(chatList);
    }

    @Override
    public List<ChatPojo> getSearchUserList(String trim) {
        return mDataBaseHelper.getChatDao().search("%" + trim + "%");
    }


    @Override
    public ChatPojo getLastOne(String username) {
        return mDataBaseHelper.getChatDao().getlastmsg(username);
    }

    @Override
    public void deleteSingleChat(ChatPojo ss) {
        mDataBaseHelper.getChatDao().singledelete(ss);
    }

    @Override
    public void addstarredMsg(StarredMssagePojo starredMssagePojo) {
        mDataBaseHelper.getStarredMessageDao().Add(starredMssagePojo);
    }

    @Override
    public String getUserNameById(String user_name) {
        return mDataBaseHelper.getUserPojo().getSingleById(user_name);
    }

    @Override
    public LiveData<List<StarredMssagePojo>> getStarredMsgList() {
        return mDataBaseHelper.getStarredMessageDao().getAll();
    }

    @Override
    public void SingleStarredMsgDel(StarredMssagePojo starredMssagePojo) {
        mDataBaseHelper.getStarredMessageDao().singledelete(starredMssagePojo);
    }

    @Override
    public StarredMssagePojo getStarredMsgById(int msgId) {
        return mDataBaseHelper.getStarredMessageDao().getStarredById(msgId);
    }

    @Override
    public void deleteAll() {
        mDataBaseHelper.runInTransaction(new Runnable() {
            @Override
            public void run() {
                mDataBaseHelper.getUserPojo().deleteTbl();
                mDataBaseHelper.getChatDao().deleteTbl();
                mDataBaseHelper.getStarredMessageDao().deleteTbl();
                mDataBaseHelper.getRecentChatUserDao().deleteTbl();
            }
        });

    }

    @Override
    public String getuserpic(String to) {
        return mDataBaseHelper.getUserPojo().getUserPic(to);
    }

    @Override
    public void updateTimestamp(String user, String now) {
      //  mDataBaseHelper.getUserPojo().updateTime(user, now);
    }

    @Override
    public String unseenmsgCount(String chatId) {
        return mDataBaseHelper.getChatDao().unseenmsgCount(chatId);
    }

    @Override
    public void showingmsgUser(String chatId) {
        mDataBaseHelper.getChatDao().updateIsShowing(chatId);
    }

    @Override
    public LiveData<ChatPojo> getSingleLast(String s) {
        return mDataBaseHelper.getChatDao().getSingleLast(s);
    }

    @Override
    public List<ContactListResponse.ResponseDataBean> getusersearch(String trim) {

//        return mDataBaseHelper.getUserPojo().search("%" + trim + "%");
        return null;
    }

    @Override
    public void deletChatPojo() {

    }

    @Override
    public void AddRecentChatUser(RecentChatUserPojo recentChatUserPojo) {
        mDataBaseHelper.getRecentChatUserDao().Add(recentChatUserPojo);
    }

    @Override
    public RecentChatUserPojo getRecentChatUserById(String chatUserId) {
        return mDataBaseHelper.getRecentChatUserDao().getSingle(chatUserId);
    }


}
