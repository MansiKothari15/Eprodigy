package com.bacancy.eprodigy.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bacancy.eprodigy.Models.StarredMssagePojo;

import java.util.List;

/**
 * Created by samir on 27/2/18.
 */
@Dao
public interface StarredMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long Add(StarredMssagePojo starredMssagePojo);

    @Query("select * from StarredMssagePojo order by chatTimestamp desc")
    LiveData<List<StarredMssagePojo>> getAll();

    @Delete
    int singledelete(StarredMssagePojo starredMssagePojo);

    @Query("select * from StarredMssagePojo where msgId = :msgId")
    StarredMssagePojo getStarredById(int msgId);

    @Query("DELETE FROM StarredMssagePojo")
    void deleteTbl();
}
