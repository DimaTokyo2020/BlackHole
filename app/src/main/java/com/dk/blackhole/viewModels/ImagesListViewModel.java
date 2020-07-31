package com.dk.blackhole.viewModels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dk.blackhole.model.Image;
import com.dk.blackhole.model.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.List;

public class ImagesListViewModel extends ViewModel {

    public static final ImagesListViewModel instance = new ImagesListViewModel();

    private  LiveData<HashMap<String,LiveData<List<Image>>>> liveDataAllImages;



    public ImagesListViewModel() {

        //load all user albums


    }

    public  LiveData<List<Image>> getImagesOfAlbum(String album){

        if(liveDataAllImages == null){
            liveDataAllImages = Model.instance.getUserAllImages();

        }

        return liveDataAllImages.getValue().get(album);
    }

    public void refresh(Model.CompleteListener listener) {
        Model.instance.refreshImageList(listener);
    }


}
