package com.dk.blackhole.models.album;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.dk.blackhole.models.image.Image;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {



    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private ArrayList<String> owners;
    private String wasCreated;
    private String imagesNumber;
    private ArrayList<Image> images;


    public Album() {}

    public Album(@NonNull String id, String name, ArrayList<String> owners, String wasCreated, String imagesNumber, ArrayList<Image> images) {
        this.id = id;
        this.name = name;
        this.owners = owners;
        this.wasCreated = wasCreated;
        this.imagesNumber = imagesNumber;
        this.images = images;
    }

    //Setters
    public void setId(@NonNull String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setOwners(ArrayList<String> owners) { this.owners = owners; }
    public void setWasCreated(String wasCreated) { this.wasCreated = wasCreated; }
    public void setImagesNumber(String imagesNumber) { this.imagesNumber = imagesNumber; }
    public void setImages(ArrayList<Image> images) { this.images = images; }

    //Getters
    @NonNull
    public String getId() { return id; }
    public String getName() { return name; }
    public ArrayList<String> getOwners() { return owners; }
    public String getWasCreated() { return wasCreated; }
    public String getImagesNumber() { return imagesNumber; }
    public ArrayList<Image> getImages() { return images; }
}
