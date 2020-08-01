package com.dk.blackhole.model;

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
//    @ColumnInfo(name = "name")
    private String name;
//    @ColumnInfo(name = "size")
    private String size;
    private String height;
    private String width;
    private boolean isChecked;
    private double lastUpdated;
    private boolean isDeleted;
    /*
    public String time_stamp;
    public String user_uploaded;
    public String event;
    public String comments;
    long lastUpdated;
    public String imgUrl;
     */



    public Image() { }

//
//    public Image(@NonNull String id, String name, String size, boolean isChecked) {
//        this.id = id;
//        this.name = name;
//        if(size != null) {
//            this.size = size;
//        }else{
//            this.size = "none";
//        }
//        this.isChecked = isChecked;
//
//    }
    public Image(@NonNull String id, String name, String height, String width, String size,
                 boolean isChecked, double lastUpdated, boolean isDeleted) {
        this.id = id;
        this.name = name;
        if(size != null) {
            this.size = size;
        }else{
            this.size = "none";
        }
        this.isChecked = isChecked;

    }
//
//    public Image(String id, String name, String size, String height, String width, String time_stamp, String user_uploaded
//            , String event, String comments) {
//
//        this.id = id;
//        this.name = name;
//        this.size = size;
//        this.height = height;
//        this.width = width;
//        this.time_stamp = time_stamp;
//        this.user_uploaded  =user_uploaded;
//        this.event = event;
//        this.comments = comments;
//        //this.imgUrl = imgUrl;
//        //this.isChecked = isChecked;
//    }
    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(String size) { this.size = size; }

    public void setHeight(String height) { this.height = height; }

    public void setWidth(String width) { this.width = width; }

    public void setIsChecked(Boolean checked) { isChecked = checked; }



    public void setLastUpdated(double lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
    //    public void setTime_stamp(String time_stamp) { this.time_stamp = time_stamp; }
//
//    public void setUser_uploaded(String user_uploaded) { this.user_uploaded = user_uploaded; }
//
//    public void setEvent(String event) { this.event = event;}
//
//    public void setComments(String comments) { this.comments = comments; }

    //public void setImgUrl(String imgUrl) {this.imgUrl = imgUrl;}

    //public void setChecked(Boolean checked) {isChecked = checked;}


    @NonNull
    public String getId() { return id; }

    public String getName() { return name; }

    public String getSize() { return size; }

    public String getHeight() { return height; }

    public String getWidth() { return width; }

    public Boolean getIsChecked() { return isChecked; }



    public double getLastUpdated() {
        return lastUpdated;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
    //    public String getTime_stamp() { return time_stamp; }
//
//    public String getUser_uploaded() { return user_uploaded; }
//
//    public String getEvent() { return event; }
//
//    public String getComments() { return comments; }
//
//    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }
//
//    public long getLastUpdated() { return lastUpdated; }

    //public String getImgUrl() { return imgUrl; }

    //public Boolean getChecked() { return isChecked; }

    public Map<String,Object> toMap(){

        HashMap<String,Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("size", size);
        result.put("height", height);
        result.put("width", width);
        result.put("isChecked", isChecked);
        result.put("lastUpdated", lastUpdated);
        result.put("isDeleted", FieldValue.serverTimestamp());
        return result;
    }


}
