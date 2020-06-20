package com.DK.blackhole.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

    public interface Listener<T>{
        void onComplete(T data);
    }

    List<Image> data = new LinkedList<>();
    private Model(){
        for (int i = 0; i < 10; i++) {
            data.add(new Image(""+i,"name " + i,null,false));
        }
    }

    public List<Image> getAllImages(){
        return data;
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
