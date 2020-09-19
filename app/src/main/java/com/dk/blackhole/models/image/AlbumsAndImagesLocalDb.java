package com.dk.blackhole.models.image;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dk.blackhole.App;


@Database(entities = {Image.class}, version = 2)//If there is any changes in the database you need to increase version number
abstract class AlbumsAndImagesLocalDbRepository extends RoomDatabase {
    public abstract AlbumsAndImagesDao imageDao();
}

public class AlbumsAndImagesLocalDb {
    static public AlbumsAndImagesLocalDbRepository db =
            Room.databaseBuilder(App.context,
                    AlbumsAndImagesLocalDbRepository.class,
                    "BlackHoleImagesDB.db")
                    .fallbackToDestructiveMigration()
                    .build();
}



