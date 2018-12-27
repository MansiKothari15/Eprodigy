package com.bacancy.eprodigy.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.bacancy.eprodigy.Models.ChatPojo;
import com.bacancy.eprodigy.Models.GroupPojo;
import com.bacancy.eprodigy.Models.RecentChatUserPojo;
import com.bacancy.eprodigy.Models.StarredMssagePojo;
import com.bacancy.eprodigy.MyApplication;
import com.bacancy.eprodigy.ResponseModel.ContactListResponse;
import com.bacancy.eprodigy.dao.ChatDao;
import com.bacancy.eprodigy.dao.GroupDao;
import com.bacancy.eprodigy.dao.RecentChatUserDao;
import com.bacancy.eprodigy.dao.StarredMessageDao;
import com.bacancy.eprodigy.dao.UserDao;

/**
 * Created by vishal patel on 2/3/2018.
 */
@Database(entities = {ChatPojo.class, ContactListResponse.ResponseDataBean.class,
        StarredMssagePojo.class, RecentChatUserPojo.class,GroupPojo.class}, version = 1)
abstract class DataBaseHelper extends RoomDatabase {


    private static volatile DataBaseHelper instance;

    static DataBaseHelper getInstance() {
        if (instance == null) {
            synchronized (DataBaseHelper.class) {
                if (instance == null)
                    instance = Room.databaseBuilder(MyApplication.getInstance(),
                            DataBaseHelper.class, "chat_db")
                            .allowMainThreadQueries()
                            .build();
            }
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    public abstract ChatDao getChatDao();
    public abstract UserDao getUserPojo();
    public abstract GroupDao getGroupDao();
    public abstract StarredMessageDao getStarredMessageDao();
    public abstract RecentChatUserDao getRecentChatUserDao();

}
