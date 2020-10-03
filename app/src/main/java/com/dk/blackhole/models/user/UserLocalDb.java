package com.dk.blackhole.models.user;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dk.blackhole.App;



@Database(entities = {User.class}, version = 2)//If there is any changes in the database you need to increase version number
abstract  class UserLocalDbRepository extends RoomDatabase {
    public abstract UserDAO userDAO();
}

public class UserLocalDb {
    static public UserLocalDbRepository db =
            Room.databaseBuilder(App.context,
                    UserLocalDbRepository.class,
                    "BlackHoleUserDB.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
