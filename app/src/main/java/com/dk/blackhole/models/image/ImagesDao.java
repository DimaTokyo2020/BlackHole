package com.dk.blackhole.models.image;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dk.blackhole.models.user.User;

import java.util.List;

import javax.annotation.meta.When;

/**
 * DAO - Data Access Object
 *
 *  This class wrap the SQLite functionality
 *  AlL the DB request are passing through this class
 */



@Dao
public interface ImagesDao {
    /*When we are using LiveData we automatically get update that a new data arrive.
    So when we refresh the data by new request to firebase we just update the local db and the local db update as. */
    @Query("select * from Image")
    LiveData<List<Image>> getAll();

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertAll(Image... Images);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertNewImage(Image image);

    @Query("SELECT * FROM Image WHERE albumId IN (:albumId)")
    LiveData<List<Image>> getImagesByAlbumsIdes(String[] albumId);

    @Query("SELECT * FROM Image WHERE owner == :userId")
    LiveData<List<Image>> getImagesByUserId(String userId);

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