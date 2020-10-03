package com.dk.blackhole.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dk.blackhole.models.image.Image;
import com.dk.blackhole.models.image.ImagesModelHelper;

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
            liveDataAllImages = ImagesModelHelper.getInstance().getUserAllImages();

        }

        return liveDataAllImages.getValue().get(album);
    }

    public void refresh(ImagesModelHelper.CompleteListener listener) {
        ImagesModelHelper.getInstance().refreshImageList("dsdsd", listener);
    }


}
