package com.DK.blackhole.model;
/*
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;
*/


import java.io.Serializable;

public class Image implements Serializable {

    public String id;
    public String name;
    public String size;
    public String height;
    public String width;
    public String time_stamp;
    public String user_uploaded;
    public String event;
    public String comments;
    long lastUpdated;
    //public String imgUrl;
    public Boolean isChecked;


    //public Image() { }


    public Image(String id, String name, String size, boolean isChecked) {
        this.id = id;
        this.name = name;
        if(size != null) {
            this.size = size;
        }else{
            this.size = "none";
        }
        this.isChecked = isChecked;

    }
/*
    public Image(String id, String name, String size, String height, String width, String time_stamp, String user_uploaded
            , String event, String comments) {

        this.id = id;
        this.name = name;
        this.size = size;
        this.height = height;
        this.width = width;
        this.time_stamp = time_stamp;
        this.user_uploaded  =user_uploaded;
        this.event = event;
        this.comments = comments;
        //this.imgUrl = imgUrl;
        //this.isChecked = isChecked;
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

    public void setTime_stamp(String time_stamp) { this.time_stamp = time_stamp; }

    public void setUser_uploaded(String user_uploaded) { this.user_uploaded = user_uploaded; }

    public void setEvent(String event) { this.event = event;}

    public void setComments(String comments) { this.comments = comments; }

    //public void setImgUrl(String imgUrl) {this.imgUrl = imgUrl;}

    //public void setChecked(Boolean checked) {isChecked = checked;}


    @NonNull
    public String getId() { return id; }

    public String getName() { return name; }

    public String getSize() { return size; }

    public String getHeight() { return height; }

    public String getWidth() { return width; }

    public String getTime_stamp() { return time_stamp; }

    public String getUser_uploaded() { return user_uploaded; }

    public String getEvent() { return event; }

    public String getComments() { return comments; }

    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }

    public long getLastUpdated() { return lastUpdated; }

    //public String getImgUrl() { return imgUrl; }

    //public Boolean getChecked() { return isChecked; }

*/
}
