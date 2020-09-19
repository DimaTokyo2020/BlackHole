package com.dk.blackhole.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dk.blackhole.models.image.Image;
import com.dk.blackhole.models.image.ImagesModel;

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
            liveDataAllImages = ImagesModel.instance.getUserAllImages();

        }

        return liveDataAllImages.getValue().get(album);
    }

    public void refresh(ImagesModel.CompleteListener listener) {
        ImagesModel.instance.refreshImageList(listener);
    }


}
