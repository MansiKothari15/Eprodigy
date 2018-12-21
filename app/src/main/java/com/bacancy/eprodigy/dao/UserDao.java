package com.bacancy.eprodigy.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bacancy.eprodigy.ResponseModel.ContactListResponse;

import java.util.List;

/**
 * Created by samir on 27/2/18.
 */
@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long Add(ContactListResponse.ResponseDataBean userPojo);

    @Query("select * from UserPojo")
    LiveData<List<ContactListResponse.ResponseDataBean>> getAll();

    @Delete
    int singledelete(ContactListResponse.ResponseDataBean userPojo);

    @Query("select * from UserPojo WHERE  username = :username ")
    ContactListResponse.ResponseDataBean getSingle(String username);

    @Query("select displayname from UserPojo WHERE  username = :username ")
    String getSingleById(String username);


    @Update
    void update(ContactListResponse.ResponseDataBean userPojo);

    @Query("select * from UserPojo WHERE  username IN (:ids)")
    LiveData<List<ContactListResponse.ResponseDataBean>> getAllbyIds(List<String> ids);

    @Query("DELETE FROM UserPojo")
    void deleteTbl();

    @Query("select profilepicture from UserPojo WHERE  username = :username ")
    String getUserPic(String username);

//    @Query("UPDATE UserPojo SET updated_at = :now where username = :username ")
//    void updateTime(String username, String now);

//    @Query("select * from UserPojo where displayname LIKE :displayname ORDER BY role_id DESC")
//    List<ContactListResponse.ResponseDataBean> search(String displayname);
}
