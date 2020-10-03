package com.dk.blackhole.models.user;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Db Entity in Users Table
 */

@Entity
public class User implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")//This generate this column name in the table for this member
    private String mId;
    @ColumnInfo(name = "name")
    private String mName;
    @ColumnInfo(name = "email")
    private String mEmail;
    @ColumnInfo(name = "last_checked_in")
    private String mLastCheckedIn;
    @ColumnInfo(name = "last_modify")
    private String mLastModify;
    @ColumnInfo(name = "deleted")
    private boolean mIsDeleted;


    public User() {}

    public User(@NonNull String mId, String mName, String mEmail, String mLastCheckedIn, String mLastModify, boolean mIsDeleted) {
        this.mId = mId;
        this.mName = mName;
        this.mEmail = mEmail;
        this.mLastCheckedIn = mLastCheckedIn;
        this.mLastModify = mLastModify;
        this.mIsDeleted = mIsDeleted;
    }

    //Setters
    public void setId(@NonNull String id) { this.mId = id; }
    public void setName(String name) { this.mName = name; }
    public void setEmail(String email) { this.mEmail = email; }
    public void setLastCheckedIn(String lastCheckedIn) { this.mLastCheckedIn = lastCheckedIn; }
    public void setLastModify(String lastModify) { this.mLastModify = lastModify; }
    public void setIsDeleted(boolean isDeleted) { this.mIsDeleted = isDeleted; }

    //Getters
    @NonNull
    public String getId() { return mId; }
    public String getName() { return mName; }
    public String getEmail() { return mEmail; }
    public String getLastCheckedIn() { return mLastCheckedIn; }
    public String getLastModify() { return mLastModify; }
    public boolean isIsDeleted() { return mIsDeleted; }



    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("id", mId);
        result.put("name", mName);
        result.put("email", mEmail);
        result.put("last_checked_in", mLastCheckedIn);
        result.put("last_modify", mLastModify);
        result.put("deleted", mIsDeleted);
        return result;
    }

}

