package com.bacancy.eprodigy.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;

import java.util.List;

/**
 * Created by samir on 27/2/18.
 */
@Dao
public interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long Add(ChatPojo chatPojo);

    @Query("select * from ChatPojo where chatId = :chatId")
    LiveData<List<ChatPojo>> getAll(String chatId);

    @Query("select * from ChatPojo WHERE  groupId IN (:groupIds)  GROUP BY groupId ORDER BY chatTimestamp DESC")
    LiveData<List<ChatPojo>> getRecentGroupChatAll(List<String> groupIds);


    @Query("select * from ChatPojo WHERE  chatId IN (:ids)  GROUP BY chatId ORDER BY chatTimestamp DESC")
    LiveData<List<ChatPojo>> getRecentChatAll(List<String> ids);

    @Query("select DISTINCT chatId  from ChatPojo")
    List<String> getRecentChatUserList();

    @Query("select DISTINCT groupId  from GroupPojo")
    List<String> getGroupIdList();

    @Query("select * from ChatPojo where chatId = :chatId and isShowing = 0 ORDER BY chatTimestamp DESC limit 1")
    LiveData<ChatPojo> getSingleLast(String chatId);

//    @Query("select * from ChatPojo ")
//    LiveData<List<ChatPojo>> getAll();
    /*@Query("select * from ChatPojo WHERE  groupId = :groupId ")
ContactListResponse.ResponseDataBean getSingle(String groupId);*/

    @Delete
    int singledelete(ChatPojo chatPojo);

    @Query("select DISTINCT chatRecv  from ChatPojo")
    List<String> getChatUserList();

    @Query("select DISTINCT chatRecv  from ChatPojo")
    List<String> getChatUserSendList();

    @Query("select * from ChatPojo where chatId = :username ORDER BY chatTimestamp DESC")
    ChatPojo getlastmsg(String username);

    @Query("select COUNT(isShowing) from ChatPojo where chatId = :chatId and isShowing = 0")
    String unseenmsgCount(String chatId);

    @Query("UPDATE  ChatPojo SET isShowing = 1 where chatId = :chatId")
    void updateIsShowing(String chatId);


    @Query("DELETE FROM ChatPojo")
    void deleteTbl();

    @Query("select * from ChatPojo " +
            " INNER JOIN UserPojo ON ChatPojo.chatId == UserPojo.username" +
            " where UserPojo.displayname LIKE :message OR ChatPojo.chatText LIKE :message GROUP BY chatId ORDER BY chatTimestamp DESC")
    List<ChatPojo> search(String message);

}
