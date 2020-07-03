package com.dk.blackhole.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 *  This class wrap the SQLite functionality
 *
 *  AlL the DB request are passing through this class
 */



@Dao
public interface ImageDao {
    @Query("select * from Image")
    List<Image> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Image... Images);

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