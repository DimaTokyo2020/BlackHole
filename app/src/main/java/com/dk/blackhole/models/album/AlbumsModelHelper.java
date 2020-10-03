package com.dk.blackhole.models.album;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.dk.blackhole.models.image.ImagesModelHelper;
import com.dk.blackhole.models.user.UserModelHelper;
import com.dk.blackhole.models.uses_and_albums.UsersAndAlbumsModelHelper;
import com.dk.blackhole.utils.Utils;

import java.util.List;

public class AlbumsModelHelper {


    private final String TAG = AlbumsModelHelper.class.getSimpleName();

    private static AlbumsModelHelper instance;
    private static ImagesModelHelper mImagesModelHelper;
    private static AlbumsFirebase mAlbumsFirebase;
    private static AlbumDAO mAlbumLocalDb;
    private static UsersAndAlbumsModelHelper mUsersAndAlbumsModelHelper;




    public interface ListenerOnComplete<T>{
        void onComplete(T data);
    }


    private AlbumsModelHelper() { }


    public static AlbumsModelHelper getInstance(){
        if(instance == null){
            instance = new AlbumsModelHelper();
            mAlbumsFirebase = AlbumsFirebase.getInstance();
            mAlbumLocalDb = AlbumLocalDb.db.albumDAO();
            mImagesModelHelper = ImagesModelHelper.getInstance();
            mUsersAndAlbumsModelHelper = UsersAndAlbumsModelHelper.getInstance();
        }
        return instance;
    }


    public void addNewAlbumToDb(Album newAlbum){
        mAlbumsFirebase.addNewAlbum(newAlbum, new ListenerOnComplete<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                new AsyncTask<String,String,String>(){
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    protected String doInBackground(String... strings) {
                        if(mAlbumLocalDb != null){
                            mAlbumLocalDb.insertNewAlbum(newAlbum);
                            mUsersAndAlbumsModelHelper.addNewUsersAndAlbumsEntity(newAlbum.getId(), newAlbum.getOwner());
                        }
                        return "";
                    }
                }.execute("");
            }
        });
    }

//    public LiveData<List<Album>> getUserAlbums(String userId) {
//        LiveData<List<Album>> userAlbumsFromLocalDb = mAlbumLocalDb.loadUserAlbums(userId);
//        refreshUserAlbumsList(userId, null);
//        return userAlbumsFromLocalDb;
//    }


//    public void refreshUserAlbumsList(String userId, UserModelHelper.listenerOnComplete listener){
//        mAlbumsFirebase.getUserAlbums(userId, new ListenerOnComplete<List<Album>>() {
//            @SuppressLint("StaticFieldLeak")
//            @Override
//            public void onComplete(List<Album> data) {
//                new AsyncTask<String,String,String>(){
//                    @Override
//                    protected String doInBackground(String... strings) {
//                        for(Album album: data){
//                            mAlbumLocalDb.insertNewAlbum(album);
//                        }
//                        return "";
//                    }
//
//                    @Override
//                    protected void onPostExecute(String s) {
//                        super.onPostExecute(s);
//                        if(listener != null){
//                            listener.onComplete(data);
//                        }
//                    }
//                }.execute("");
//
//            }
//        });
//    }



    public void updateImagesNumberInAlbum(String albumId){
        mImagesModelHelper.getImagesNumberInAlbum(albumId, new ImagesModelHelper.Listener<Integer>() {
            @Override
            public void onComplete(Integer imagesNumber) {
                String timeOfUpdate = Utils.getCurrentTime();
                mAlbumsFirebase.updateAlbumImagesNumber(albumId, imagesNumber, timeOfUpdate, new ListenerOnComplete<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        new AsyncTask<String,String,String>(){
                            @Override
                            protected String doInBackground(String... strings) {
                                Album album = mAlbumLocalDb.getAlbumById(albumId);
                                album.setImagesNumber(imagesNumber);
                                album.setLastModify(timeOfUpdate);
                                mAlbumLocalDb.updateAlbum(album);
                                return "";
                            }
                        }.execute("");

                    }
                });
            }
        });
    }


    public void increaseOrDecreaseLikeForAlbum(String albumId, boolean isChecked) {
        mAlbumsFirebase.getAlbum(albumId, new ListenerOnComplete<Album>() {
            @Override
            public void onComplete(Album data) {
                final int albumLikesNumber;
                if(true == isChecked) {
                    albumLikesNumber = data.getLikesNumber() + 1;
                }
                else {
                    albumLikesNumber = data.getLikesNumber() - 1;
                }


                String timeOfUpdate = Utils.getCurrentTime();
                mAlbumsFirebase.updateAlbumLikesNumber(albumId, albumLikesNumber, timeOfUpdate, new ListenerOnComplete<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        new AsyncTask<String,String,String>(){
                            @Override
                            protected String doInBackground(String... strings) {
                                Album album = mAlbumLocalDb.getAlbumById(albumId);
                                album.setLikesNumber(albumLikesNumber);
                                album.setLastModify(timeOfUpdate);
                                mAlbumLocalDb.updateAlbum(album);
                                return "";
                            }
                        }.execute("");
                    }
                });
            }
        });

    }

    public LiveData<List<Album>> getAlbumsByIdes(String[] albumsIdes, ListenerOnComplete listenerOnComplete){
        Log.i(TAG,"AlbumsModelHelper.getAlbumsByIdes() Started");
        LiveData<List<Album>> userAlbumsFromLocalDb = mAlbumLocalDb.loadUserAlbums(albumsIdes);
        refreshUserAlbumsList(albumsIdes, null);
        Log.i(TAG,"AlbumsModelHelper.getAlbumsByIdes() Finished but inhered a listener");
        return userAlbumsFromLocalDb;
    }



    public void refreshUserAlbumsList(String[] albumsIdes, UserModelHelper.listenerOnComplete listener){
        Log.i(TAG,"AlbumsModelHelper.refreshUserAlbumsList() Started");
        mAlbumsFirebase.getAlbumsByIdes(albumsIdes, new ListenerOnComplete<List<Album>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(List<Album> data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        for(Album album: data){
                            mAlbumLocalDb.insertNewAlbum(album);

                        }
                        Log.i(TAG,"AlbumsModelHelper.refreshUserAlbumsList() Finished");
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if(listener != null){
                            listener.onComplete(data);
                        }
                    }
                }.execute("");

            }
        });
    }

}
