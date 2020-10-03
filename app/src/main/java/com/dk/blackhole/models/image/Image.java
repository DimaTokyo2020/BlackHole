package com.dk.blackhole.models.image;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.firebase.firestore.FieldValue;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Image implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String albumId;
    private String owner;
    private String size;
    private String height;
    private String width;
    private boolean isSharable;
    private int likes;
    private String imgUrl;
    private String created;
    private String lastModify;
    private boolean isDeleted;


    public Image() { }

    public Image(@NonNull String id, String name, String albumId, String owner,
                 String size, String height, String width, boolean isSharable,
                 int likes, String imgUrl, String created, String lastModify,
                 boolean isDeleted) {

        this.id = id;
        this.name = name;
        this.albumId = albumId;
        this.owner = owner;
        this.size = size;
        this.height = height;
        this.width = width;
        this.isSharable = isSharable;
        this.likes = likes;
        this.imgUrl = imgUrl;
        this.created = created;
        this.lastModify = lastModify;
        this.isDeleted = isDeleted;
    }


    public void setId(@NonNull String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSize(String size) { this.size = size; }
    public void setHeight(String height) { this.height = height; }
    public void setWidth(String width) { this.width = width; }
    public void setAlbumId(String albumId) { this.albumId = albumId; }
    public void setOwner(String owner) { this.owner = owner; }
    public void setSharable(boolean sharable) { isSharable = sharable; }
    public void setLikes(int likes) { this.likes = likes; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }
    public void setCreated(String created) { this.created = created; }
    public void setLastModify(String lastModify) { this.lastModify = lastModify; }
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }



    @NonNull
    public String getId() { return id; }
    public String getName() { return name; }
    public String getSize() { return size; }
    public String getHeight() { return height; }
    public String getWidth() { return width; }
    public String getAlbumId() { return albumId; }
    public String getOwner() { return owner; }
    public boolean isSharable() { return isSharable; }
    public int getLikes() { return likes; }
    public String getImgUrl() { return imgUrl; }
    public String getCreated() { return created; }
    public String getLastModify() { return lastModify; }
    public boolean isDeleted() {
        return isDeleted;
    }


    public Map<String,Object> toMap(){

        HashMap<String,Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("size", size);
        result.put("height", height);
        result.put("width", width);
        result.put("albumId", albumId);
        result.put("owner", owner);
        result.put("isSharable", isSharable);
        result.put("likes", likes);
        result.put("imgUrl", imgUrl);
        result.put("created", created);
        result.put("lastModify", lastModify);
        result.put("isDeleted", isDeleted);

        //result.put("isDeleted", FieldValue.serverTimestamp());
        return result;
    }


}
