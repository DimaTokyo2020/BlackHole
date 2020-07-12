package com.dk.blackhole.model;

import android.os.AsyncTask;

import java.util.List;

public class Model {
    public static final Model instance = new Model();

    ModelFirebase mModelFirebase;
    public interface Listener<T>{
        void onComplete(T data);
    }


    //List<Image> data = new LinkedList<>();
    private Model(){
    mModelFirebase = new ModelFirebase();
    mModelFirebase.listenToMultipleOnlyChanges();

//        for (int i = 0; i < 10; i++) {
//            data.add(new Image(""+i,"name " + i,null,false));
//        }
    }


    /**
     * @param listener we return the result to this class
     *                 and the class that made the request will
     *                 what ever it want with this data.
     */
    public void getAllImages(final Listener<List<Image>> listener){


        //this need for running DB request on background thread
        class MyAsyncTask extends AsyncTask<String, String, String>{
            private List<Image> data;
            @Override
            protected String doInBackground(String... strings) {
                data = AppLocalDb.db.imageDao().getAll();
                if(data.size() == 0){
                    createEntitiesForTesting();
                    data = AppLocalDb.db.imageDao().getAll();
                }

                return null;
            }
            /**
             * @param s came from doInBackground
             */
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                listener.onComplete(data);
            }
        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();

    }

    private void createEntitiesForTesting() {
        for(int i = 0; i < 20; i++){
            AppLocalDb.db.imageDao().insertAll(new Image("" + i, "name " + i, "" + (i * 10), false));
        }
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
