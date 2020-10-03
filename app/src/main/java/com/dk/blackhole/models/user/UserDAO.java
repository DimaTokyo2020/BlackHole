package com.dk.blackhole.models.user;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;



@Dao
public interface UserDAO {

    @Query("SELECT * FROM User WHERE email = :email")
    public User getUserByEmail(String email);

    @Insert
    public void insertNewUser(User user);

    @Update
    public void updateUsers(User user);

    @Delete
    public void deleteUsers(User user);

}
