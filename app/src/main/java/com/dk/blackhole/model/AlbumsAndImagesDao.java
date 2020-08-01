package com.dk.blackhole.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * DAO - Data Access Object
 *
 *  This class wrap the SQLite functionality
 *  AlL the DB request are passing through this class
 */



@Dao
public interface AlbumsAndImagesDao {
    @Query("select * from Image")
    LiveData<List<Image>> getAll();//When we are using LiveData we automatically get update that a new data arrive.
                                  // So when we refresh the data by new request to firebase we just update the local db and the local db update as.

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Image... Images);

    @Query("SELECT * FROM Image WHERE name IN (:userIds)")
    List<Image> loadAllByIds(int[] userIds);

    @Delete
    void delete(Image Image);
}

//liveDate
/*
@Dao
public interface ImageDao {
    @Query("select * from Image")
    LiveData<List<Image>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Image... Images);

    @Delete
    void delete(Image Image);
}



 */