package com.bacancy.eprodigy.db;

import android.arch.lifecycle.LiveData;

import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.Models.GroupPojo;
import com.bacancy.eprodigy.Models.RecentChatUserPojo;
import com.bacancy.eprodigy.Models.StarredMssagePojo;
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;

import java.util.List;

/**
 * Created by vishal patel on 2/3/2018.
 */

public interface IDataManager {

    // chatpojo
    void AddChat(ChatPojo ChatPojo);

    void AddGroup(GroupPojo groupPojo);

    LiveData<List<ChatPojo>> getAll(String form);

    LiveData<List<GroupPojo>> getAllGroup();

    void SingleDelete(ChatPojo ChatPojo);




    // userList
    void AddUser(ContactListResponse.ResponseDataBean userPojo);
    LiveData<List<ContactListResponse.ResponseDataBean>> getAllUser();

    ContactListResponse.ResponseDataBean getUser(String username);

    void UpdateUser(ContactListResponse.ResponseDataBean userPojo);

    List<String> getChatUserList();
    List<String> getGroupIdList();
    List<String> getChatUserListNew();

    LiveData<List<ContactListResponse.ResponseDataBean>> getChatUserListById(List<String> chatList);

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

    List<ContactListResponse.ResponseDataBean> getusersearch(String trim);


    void deletChatPojo();

    void AddRecentChatUser(RecentChatUserPojo recentChatUserPojo);

    RecentChatUserPojo getRecentChatUserById(String chatUserId);

    LiveData<List<ChatPojo>> getRecentChatUserListById(List<String> chatList);

    LiveData<List<ChatPojo>> getRecentGroupUserListById(List<String> groupIds);

    List<ChatPojo> getSearchUserList(String trim);
}
