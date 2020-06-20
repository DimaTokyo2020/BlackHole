package com.DK.blackhole.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

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