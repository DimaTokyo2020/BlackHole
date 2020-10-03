package com.dk.blackhole.models.album;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dk.blackhole.App;


@Database(entities = {Album.class}, version = 2)//If there is any changes in the database you need to increase version number
abstract  class AlbumLocalDbRepository extends RoomDatabase {
    public abstract AlbumDAO albumDAO();
}

public class AlbumLocalDb {
    static public AlbumLocalDbRepository db =
            Room.databaseBuilder(App.context,
                    AlbumLocalDbRepository.class,
                    "BlackHoleAlbumDB.db")
                    .fallbackToDestructiveMigration()
                    .build();
}