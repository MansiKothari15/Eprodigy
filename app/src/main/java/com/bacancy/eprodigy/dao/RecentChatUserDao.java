package com.bacancy.eprodigy.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bacancy.eprodigy.Models.RecentChatUserPojo;

import java.util.List;

/**
 * Created by vishal on 29/3/18.
 */
@Dao
public interface RecentChatUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long Add(RecentChatUserPojo recentChatUserPojo);

    @Query("select * from RecentChatUserPojo where userId = :userId")
    RecentChatUserPojo getSingle(String userId);

    @Query("select DISTINCT userId  from RecentChatUserPojo")
    List<String> getChatUserList();

    @Query("DELETE FROM RecentChatUserPojo")
    void deleteTbl();
}
