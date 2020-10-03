package com.dk.blackhole.models.album;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dk.blackhole.models.image.Image;

import java.util.List;


@Dao
public interface AlbumDAO {

    @Query("SELECT * FROM Album WHERE owner IN (:owner)")
    public LiveData<List<Album>> loadUserAlbums(String owner);

    @Query("SELECT * FROM Album WHERE id IN (:albumsIdes)")
    public LiveData<List<Album>> loadUserAlbums(String[] albumsIdes);

    @Query("SELECT * FROM Album WHERE id = :id")
    public Album getAlbumById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertNewAlbum(Album album);

    @Update
    public void updateAlbum(Album album);

    @Delete
    public void deleteAlbum(Album album);
}
