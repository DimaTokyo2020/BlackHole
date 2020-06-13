package com.DK.blackhole.model;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.DK.blackhole.MyApplication;


@Database(entities = {Image.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract ImageDao ImageDao();
}

//public class AppLocalDb{
class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.context,
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}



