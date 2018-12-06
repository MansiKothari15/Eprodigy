package com.bacancy.eprodigy.db;

import android.arch.lifecycle.LiveData;

import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.Models.RecentChatUserPojo;
import com.bacancy.eprodigy.Models.StarredMssagePojo;
import com.bacancy.eprodigy.Models.UserPojo;

import java.util.List;

/**
 * Created by vishal patel on 2/3/2018.
 */

public interface IDataManager {

    // chatpojo
    void AddChat(ChatPojo ChatPojo);

    LiveData<List<ChatPojo>> getAll(String form);

    void SingleDelete(ChatPojo ChatPojo);




    // userList
    void AddUser(UserPojo userPojo);
    LiveData<List<UserPojo>> getAllUser();

    UserPojo getUser(String username);

    void UpdateUser(UserPojo userPojo);

    List<String> getChatUserList();
    List<String> getChatUserListNew();

    LiveData<List<UserPojo>> getChatUserListById(List<String> chatList);

    ChatPojo getLastOne(String username);

    void deleteSingleChat(ChatPojo ss);

    void addstarredMsg(StarredMssagePojo starredMssagePojo);

    String getUserNameById(String user_name);

    LiveData<List<StarredMssagePojo>> getStarredMsgList();

    void SingleStarredMsgDel(StarredMssagePojo starredMssagePojo);

    StarredMssagePojo getStarredMsgById(int msgId);

    void deleteAll();

    String getuserpic(String to);

    void updateTimestamp(String s, String now);

    String unseenmsgCount(String s);

    void showingmsgUser(String s);

    LiveData<ChatPojo> getSingleLast(String s);

    List<UserPojo> getusersearch(String trim);


    void deletChatPojo();

    void AddRecentChatUser(RecentChatUserPojo recentChatUserPojo);

    RecentChatUserPojo getRecentChatUserById(String chatUserId);

    LiveData<List<ChatPojo>> getRecentChatUserListById(List<String> chatList);

    List<ChatPojo> getSearchUserList(String trim);
}
