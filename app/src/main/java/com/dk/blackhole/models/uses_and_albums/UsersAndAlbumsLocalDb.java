package com.dk.blackhole.models.uses_and_albums;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dk.blackhole.App;






@Database(entities = {UsersAndAlbums.class}, version = 2)//If there is any changes in the database you need to increase version number
abstract  class UsersAndAlbumsLocalDbRepository extends RoomDatabase {
    public abstract UsersAndAlbumsDAO usersAndAlbumsDAO();
}

public class UsersAndAlbumsLocalDb {
    static public com.dk.blackhole.models.uses_and_albums.UsersAndAlbumsLocalDbRepository db =
            Room.databaseBuilder(App.context,
                    com.dk.blackhole.models.uses_and_albums.UsersAndAlbumsLocalDbRepository.class,
                    "BlackHoleUsersAndAlbumsDB.db")
                    .fallbackToDestructiveMigration()
                    .build();
}