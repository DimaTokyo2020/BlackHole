package com.dk.blackhole.models.uses_and_albums;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;


@Dao
public interface UsersAndAlbumsDAO {

    @Query("SELECT * FROM UsersAndAlbums WHERE userId = :userId ORDER BY id")
    public LiveData<List<UsersAndAlbums>> getAllUsersAlbums(String userId);

    @Query("SELECT * FROM UsersAndAlbums WHERE userId = :userId")
    public List<UsersAndAlbums> getAllUsersAlbumsList(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsersAndAlbums(UsersAndAlbums usersAndAlbums);

    @Update
    public void updateUsersAndAlbums(UsersAndAlbums usersAndAlbums);

    @Delete
    public void deleteUsersAndAlbums(UsersAndAlbums usersAndAlbums);

}