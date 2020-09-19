package com.dk.blackhole.models.image;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.HashMap;
import java.util.List;

public class ImagesModel {
    public static final ImagesModel instance = new ImagesModel();

    ModelFirebase mModelFirebase;



    /**
     * Anyone why use Model can implement next listener how they want.
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


    //List<Image> data = new LinkedList<>();
    private ImagesModel(){

    mModelFirebase = new ModelFirebase();
    mModelFirebase.getCustomObject2();
//    mModelFirebase.getAllUserAlbums(new Listener<List<Image>>() {
//        @Override
//        public void onComplete(List<Image> data) {
//
//        }
//    });//test
    }

    public void refreshImageList(CompleteListener listener){
        mModelFirebase.getMultipleDocuments(new Listener<List<Image>>() {
            @Override
            public void onComplete(List<Image> data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        for(Image image: data){
                            AlbumsAndImagesLocalDb.db.imageDao().insertAll(image);
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

    public LiveData<List<Image>> getAllImages(){

    LiveData<List<Image>> liveData = AlbumsAndImagesLocalDb.db.imageDao().getAll();
    refreshImageList(null);
    return liveData;



//        //this need for running DB request on background thread
//        class MyAsyncTask extends AsyncTask<String, String, String>{
//            private List<Image> data;
//            @Override
//            protected String doInBackground(String... strings) {
//                data = AppLocalDb.db.imageDao().getAll();
//                if(data.size() == 0){
//                    createEntitiesForTesting();
//                    data = AppLocalDb.db.imageDao().getAll();
//                }
//
//                return null;
//            }
//            /**
//             * @param s came from doInBackground
//             */
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                listener.onComplete(data);
//            }
//        }
//        MyAsyncTask task = new MyAsyncTask();
//        task.execute();

    }

    private void createEntitiesForTesting() {
//        for(int i = 0; i < 20; i++){
//            AppLocalDb.db.imageDao().insertAll(new Image("" + i, "name " + i, "" + (i * 10), false));
//        }
    }


    public void addUserToFirebase(String userName, String userEmail){
        mModelFirebase.createUser(userName, userEmail);
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
