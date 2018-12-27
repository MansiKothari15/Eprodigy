package com.bacancy.eprodigy.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bacancy.eprodigy.Models.GroupPojo;
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;

import java.util.List;

/**
 * Created by samir on 27/2/18.
 */
@Dao
public interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long AddGroup(GroupPojo userPojo);

    @Query("select * from GroupPojo")
    LiveData<List<GroupPojo>> getAllGroup();

    @Delete
    int singledelete(GroupPojo userPojo);

/*    @Query("select * from GroupPojo WHERE  username = :username ")
    GroupPojo getSingle(String username);*/

  /*  @Query("select displayname from GroupPojo WHERE  username = :username ")
    String getSingleById(String username);*/


    @Update
    void update(GroupPojo userPojo);

    /*@Query("select * from GroupPojo WHERE  username IN (:ids)")
    LiveData<List<GroupPojo>> getAllbyIds(List<String> ids);*/

    @Query("DELETE FROM GroupPojo")
    void deleteTbl();

   /* @Query("select profilepicture from GroupPojo WHERE  username = :username ")
    String getUserPic(String username);*/

//    @Query("UPDATE UserPojo SET updated_at = :now where username = :username ")
//    void updateTime(String username, String now);

//    @Query("select * from UserPojo where displayname LIKE :displayname ORDER BY role_id DESC")
//    List<GroupPojo> search(String displayname);
}
