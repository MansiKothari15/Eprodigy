package com.bacancy.eprodigy.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bacancy.eprodigy.Models.GroupUserPojo;

import java.util.List;

@Dao
public interface GroupUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long AddGroupUser(GroupUserPojo groupUserPojo);

    @Query("select * from GroupUserPojo")
    LiveData<List<GroupUserPojo>> getAllGroupUsers();

    @Delete
    int singledelete(GroupUserPojo groupUserPojo);

    @Query("select * from GroupUserPojo WHERE  groupId = :groupId ")
//    GroupUserPojo getSingleGroupUser(String groupId);
    LiveData<GroupUserPojo> getSingleGroupUser(String groupId);

}
