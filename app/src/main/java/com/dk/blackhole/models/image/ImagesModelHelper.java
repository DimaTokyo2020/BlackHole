package com.dk.blackhole.models.image;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;

import com.dk.blackhole.models.album.AlbumsModelHelper;
import com.dk.blackhole.models.uses_and_albums.UsersAndAlbums;
import com.dk.blackhole.models.uses_and_albums.UsersAndAlbumsModelHelper;
import com.dk.blackhole.utils.Utils;

import java.util.HashMap;
import java.util.List;

public class ImagesModelHelper {
    private static  ImagesModelHelper instance;

    private static ImagesDao mImagesLocalDb;
    private static ImagesFirebaseCloud mImagesFirebaseCloud;
    private static ImagesFirebaseStorage mImagesFirebaseStorage;
    private static AlbumsModelHelper mAlbumsModelHelper;




    /**
     * Anyone who use Model can implement next listener how they want.
     * This listener use as tigers for complete tasks.
     * This how we can pass the data back or notify for updates.
     * @param <T>
     */
    public interface Listener<T>{
        void onComplete(T data);
    }

    public interface CompleteListener {
        void onComplete();
    }

    public interface listenerUpload{
        void started();
        void onCompleteUpload();
        void finished(String uploadUri, String path);
    }


    //List<Image> data = new LinkedList<>();
    private ImagesModelHelper(){ }

    public static ImagesModelHelper getInstance(){
        if(instance == null){
            instance = new ImagesModelHelper();
            mImagesFirebaseStorage = ImagesFirebaseStorage.getInstance();
            mImagesLocalDb = ImagesLocalDb.db.imageDao();
            mImagesFirebaseCloud = ImagesFirebaseCloud.getInstance();
            mAlbumsModelHelper =  AlbumsModelHelper.getInstance();
        }
        return instance;
    }


    public void uploadAndInsertImageToDb(ImageView imageView, Image imageInfo, listenerUpload listener){

        byte[] data = Utils.imageViewToByteArr(imageView);
        mImagesFirebaseStorage.uploadImageToFirebase(data, imageInfo, new listenerUpload() {
            @Override
            public void started() { listener.started(); }
            @Override
            public void onCompleteUpload() {listener.onCompleteUpload(); }

            @Override
            public void finished(String uploadUri, String path) {
                imageInfo.setImgUrl(uploadUri);
                mImagesFirebaseCloud.addNewImage(imageInfo, new CompleteListener(){
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onComplete() {
                        new AsyncTask<String,String,String>(){
                            @Override
                            protected String doInBackground(String... strings) {
                                if(mImagesLocalDb != null){
                                    mImagesLocalDb.insertNewImage(imageInfo);
                                    mAlbumsModelHelper.updateImagesNumberInAlbum(imageInfo.getAlbumId());
                                }
                                return "";
                            }
                            }.execute("");
                    }
                });
            }
        });

    }


    public void refreshImageList(String userId, CompleteListener listener){
        mImagesFirebaseCloud.getUserImages(userId, new Listener<List<Image>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(List<Image> data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        for(Image image: data){
                            ImagesLocalDb.db.imageDao().insertNewImage(image);
                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if(listener != null){
                            listener.onComplete();
                        }
                    }
                }.execute("");
            }
        });
    }

    public void refreshUsersAlbumsImagesList(String[] albumsId, CompleteListener listener){
        mImagesFirebaseCloud.getUserImagesByAlbumsIdes(albumsId, new Listener<List<Image>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(List<Image> data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        for(Image image: data){
                            ImagesLocalDb.db.imageDao().insertNewImage(image);
                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if(listener != null){
                            listener.onComplete();
                        }
                    }
                }.execute("");
            }
        });
    }


    public LiveData<List<Image>> getAllImages(String userId){

    LiveData<List<Image>> liveData = ImagesLocalDb.db.imageDao().getAll();
    refreshImageList(userId,null);
    return liveData;
    }
//
//    public LiveData<List<Image>> getUserImages(String userId){
//
//        LiveData<List<Image>> liveData = ImagesLocalDb.db.imageDao().getImagesByUserId(userId);
//        refreshImageList(userId,null);
//        return liveData;
//    }


    public LiveData<List<Image>> getUserImages(String[] albumsIdes){

        LiveData<List<Image>> liveData = ImagesLocalDb.db.imageDao().getImagesByAlbumsIdes(albumsIdes);
//        refreshImageList(userId,null);
        if(albumsIdes.length !=0) {
            refreshUsersAlbumsImagesList(albumsIdes, null);
        }
        return liveData;
    }



    public void getImagesNumberInAlbum(String albumId, Listener<Integer> integerListener) {
        mImagesFirebaseCloud.getImagesNumberInAlbum(albumId, integerListener);
    }








    ///////////////////////////////





    public void addUserToFirebase(String userName, String userEmail){
        mImagesFirebaseCloud.createUser(userName, userEmail);
    }



    public void loadUserInfo() {
//        mModelFirebase.load
    }

    public LiveData<HashMap<String, LiveData<List<Image>>>> getUserAllImages() {

        //this function will load the info of all user images.
        return null;
    }



    /*
    public void getAllImages(final Listener<List<Image>> listener){
        @SuppressLint("StaticFieldLeak")
        AsyncTask<String, String, List<Image>> taskA = new AsyncTask<String, String, List<Image>>(){
            @Override
            protected List<Image> doInBackground(String... strings) {
                return AppLocalDb.db.imageDao().getAll();
            }
            @Override
            protected void onPostExecute(List<Image> images) {
                super.onPostExecute(images);
                listener.onComplete(images);
            }
        };
        taskA.execute();
    }


    public Image getImage(String id){
        return null;
    }

    public void update(Image image){

    }

    public void delete(Image image){

    }

*/


}
