package com.dk.blackhole.models.image;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dk.blackhole.App;


@Database(entities = {Image.class}, version = 3)//If there is any changes in the database you need to increase version number
abstract class ImagesLocalDbRepository extends RoomDatabase {
    public abstract ImagesDao imageDao();
}

public class ImagesLocalDb {
    static public ImagesLocalDbRepository db =
            Room.databaseBuilder(App.context,
                    ImagesLocalDbRepository.class,
                    "BlackHoleImagesDB.db")
                    .fallbackToDestructiveMigration()
                    .build();
}



