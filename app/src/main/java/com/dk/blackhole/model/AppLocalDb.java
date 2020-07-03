package com.dk.blackhole.model;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dk.blackhole.App;


@Database(entities = {Image.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract ImageDao imageDao();
}

public class AppLocalDb {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(App.context,
                    AppLocalDbRepository.class,
                    "BlackHoleDB.db")
                    .fallbackToDestructiveMigration()
                    .build();
}


