package com.dk.blackhole.viewModels;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dk.blackhole.models.album.Album;
import com.dk.blackhole.models.album.AlbumsModelHelper;
import com.dk.blackhole.models.image.Image;
import com.dk.blackhole.models.user.UserModelHelper;
import com.dk.blackhole.models.user.UsersFirebase;
import com.dk.blackhole.models.uses_and_albums.UsersAndAlbums;
import com.dk.blackhole.models.uses_and_albums.UsersAndAlbumsModelHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class HomeActivityViewModel extends ViewModel {

    private final String TAG = HomeActivityViewModel.class.getSimpleName();

    private FirebaseAuth mUserAuth;
    private FirebaseUser mUser;
    private UserModelHelper mUserModelHelper;
    private AlbumsModelHelper mAlbumsModelHelper;
    private UsersAndAlbumsModelHelper mUsersAndAlbumsModelHelper;
    LiveData<List<Album>> mUserAlbums;
    LiveData<List<UsersAndAlbums>> mUsersAndAlbums;
    String mUserId;
    private boolean isRegisterOnChanges = false;




    public interface OnCompleteListener <T>{
        void onComplete(T data);
    }


    public HomeActivityViewModel() {
        mUserAuth = FirebaseAuth.getInstance();//Get user info
        mUser = mUserAuth.getCurrentUser();
        mUserModelHelper = UserModelHelper.getInstance();
        mAlbumsModelHelper = AlbumsModelHelper.getInstance();
        mUsersAndAlbumsModelHelper = UsersAndAlbumsModelHelper.getInstance();
    }


//    public void setUser(String userId) {
//        mUserId = userId;
//        new AsyncTask<String,String,String>(){
//            @SuppressLint("StaticFieldLeak")
//            @Override
//            protected String doInBackground(String... strings) {
//                mUserAlbums = mAlbumsModelHelper.getUserAlbums(mUserId);
//                return "";
//            }
//        }.execute("");
//    }

    public void setUser(String userId) {
        Log.i(TAG,"HomeActivityViewModel.setUser() Started");
        mUserId = userId;
        if(false == isRegisterOnChanges){
            isRegisterOnChanges=true;
            mUsersAndAlbumsModelHelper.registerOnChanges(mUserId);
        }

        new AsyncTask<String,String,String>(){
            @SuppressLint("StaticFieldLeak")
            @Override
            protected String doInBackground(String... strings) {
                LiveData[] liveData = mUsersAndAlbumsModelHelper.getUserAlbums(mUserId);
                mUsersAndAlbums = liveData[0];
                mUserAlbums = liveData[1];
                Log.i(TAG,"HomeActivityViewModel.setUser() Finished");
                return "";
            }
        }.execute("");

    }


    public void increaseOrDecreaseLikeForAlbum(String albumId, boolean isChecked) {
        mAlbumsModelHelper.increaseOrDecreaseLikeForAlbum(albumId, isChecked);
    }



    //Getters
    public String getUserEmail(){ return mUser.getEmail(); }
    public String getUserDisplayName(){return mUser.getDisplayName();}
    public Uri getUserPhotoUrl(){return mUser.getPhotoUrl();}
    public  LiveData<List<Album>> getUserAlbums(){ return mUserAlbums;}
    public  LiveData<List<UsersAndAlbums>> getUsersAndAlbums(){ return mUsersAndAlbums;}



}
