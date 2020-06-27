package com.DK.blackhole.model;


import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.DK.blackhole.App;


//@Database(entities = {Image.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase {
    //public abstract ImageDao imageDao();
}

//public class AppLocalDb{
class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(App.context,
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}



