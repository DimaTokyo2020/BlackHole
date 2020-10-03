package com.dk.blackhole.models.album;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dk.blackhole.models.image.Image;
import com.dk.blackhole.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.internal.Util;

/**
 * Db Entity in Album Table
 */

@Entity
public class Album implements Serializable {



    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String owner;
    @ColumnInfo(name = "was_created")//This generate this column name in the table for this member
    private String wasCreated;
    @ColumnInfo(name = "images_number")
    private int imagesNumber;
    @ColumnInfo(name = "is_deleted")
    boolean isDeleted;
    @ColumnInfo(name = "last_modify")
    String lastModify;
    @ColumnInfo(name = "is_sharable")
    boolean isSharable;
    @ColumnInfo(name = "likes")
    int likesNumber;

    public Album() { }

    public Album(String albumName, String userId){
        id  = Utils.getNewUniqueId();
        name = albumName;
        owner = userId;
        wasCreated = Utils.getCurrentTime();
        imagesNumber = 0;
        isSharable = true;
        lastModify = wasCreated;
        isDeleted = false;
        likesNumber = 0;

    }

    public Album(@NonNull String id, String name, String owner, String wasCreated, int imagesNumber, boolean isDeleted, String lastModify, boolean isSharable, int likesNumber) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.wasCreated = wasCreated;
        this.imagesNumber = imagesNumber;
        this.isDeleted = isDeleted;
        this.lastModify = lastModify;
        this.isSharable = isSharable;
        this.likesNumber = likesNumber;
    }

    //Setters
    public void setId(@NonNull String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setOwner(String owner) { this.owner = owner; }
    public void setWasCreated(String wasCreated) { this.wasCreated = wasCreated; }
    public void setImagesNumber(int imagesNumber) { this.imagesNumber = imagesNumber; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
    public void setLastModify(String lastModify) { this.lastModify = lastModify; }
    public void setSharable(boolean sharable) { isSharable = sharable; }
    public void setLikesNumber(int likesNumber) { this.likesNumber = likesNumber; }

    //Getters
    @NonNull
    public String getId() { return id; }
    public String getName() { return name; }
    public String getOwner() { return owner; }
    public String getWasCreated() { return wasCreated; }
    public int getImagesNumber() { return imagesNumber; }
    public boolean isDeleted() { return isDeleted; }
    public String getLastModify() { return lastModify; }
    public boolean isSharable() { return isSharable; }
    public int getLikesNumber() { return likesNumber; }

    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("owner", owner);
        result.put("was_created", wasCreated);
        result.put("images_number", imagesNumber);
        result.put("is_deleted", isDeleted);
        result.put("last_modify", lastModify);
        result.put("is_sharable", isSharable);
        result.put("likes", likesNumber);
        return result;
    }
}
