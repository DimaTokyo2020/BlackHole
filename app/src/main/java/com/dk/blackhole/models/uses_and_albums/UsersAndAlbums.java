package com.dk.blackhole.models.uses_and_albums;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Entity
public class UsersAndAlbums  implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;
    private String userId;
    private String albumId;
    private boolean isLiked;
    private String created;
    private String modify;

    public UsersAndAlbums() { }

    public UsersAndAlbums(String id, String userId, String albumId, boolean isLiked, String created, String modify) {
        this.id = id;
        this.userId = userId;
        this.albumId = albumId;
        this.isLiked = isLiked;
        this.created = created;
        this.modify = modify;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setModify(String modify) {
        this.modify = modify;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public String getCreated() {
        return created;
    }

    public String getModify() {
        return modify;
    }


    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("id", id);
        result.put("userId", userId);
        result.put("albumId", albumId);
        result.put("isLiked", isLiked);
        result.put("created", created);
        result.put("modify", modify);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersAndAlbums that = (UsersAndAlbums) o;
        return isLiked == that.isLiked &&
                id.equals(that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(albumId, that.albumId) &&
                Objects.equals(created, that.created) &&
                Objects.equals(modify, that.modify);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, albumId, isLiked, created, modify);
    }
}
