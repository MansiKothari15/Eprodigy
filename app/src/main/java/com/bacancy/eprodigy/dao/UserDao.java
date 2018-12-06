package com.bacancy.eprodigy.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bacancy.eprodigy.Models.UserPojo;

import java.util.List;

/**
 * Created by samir on 27/2/18.
 */
@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long Add(UserPojo userPojo);

    @Query("select * from UserPojo ORDER BY role_id DESC ")
    LiveData<List<UserPojo>> getAll();

    @Delete
    int singledelete(UserPojo userPojo);

    @Query("select * from UserPojo WHERE  username = :username ")
    UserPojo getSingle(String username);

    @Query("select displayname from UserPojo WHERE  username = :username ")
    String getSingleById(String username);


    @Update
    void update(UserPojo userPojo);

    @Query("select * from UserPojo WHERE  username IN (:ids) ORDER BY updated_at DESC")
    LiveData<List<UserPojo>> getAllbyIds(List<String> ids);

    @Query("DELETE FROM UserPojo")
    void deleteTbl();

    @Query("select profilepicture from UserPojo WHERE  username = :username ")
    String getUserPic(String username);

    @Query("UPDATE UserPojo SET updated_at = :now where username = :username ")
    void updateTime(String username, String now);

    @Query("select * from UserPojo where displayname LIKE :displayname ORDER BY role_id DESC")
    List<UserPojo> search(String displayname);
}
