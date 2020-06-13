package com.DK.blackhole.model;
/*
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.DK.blackhole.MyApplication;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ImageModel {

    public static final ImageModel instance = new ImageModel();

    public interface Listener<T>{
        void onComplete(T data);
    }
    public interface CompListener{
        void onComplete();
    }
    private ImageModel(){
//        for (int i=0;i<5;i++){
//            Image st = new Image(""+i,"name"+1,null,false);
//            addImage(st,null);
//        }
    }

    public void addImage(Image image,Listener<Boolean> listener) {
        ImageFirebase.addImage(image,listener);
    }

    public void refreshImageList(final CompListener listener){
        long lastUpdated = MyApplication.context.getSharedPreferences("TAG",MODE_PRIVATE).getLong("ImagesLastUpdateDate",0);
        ImageFirebase.getAllImagesSince(lastUpdated,new Listener<List<Image>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Image> data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        for(Image s : data){
                            AppLocalDb.db.ImageDao().insertAll(s);
                            if (s.lastUpdated > lastUpdated) lastUpdated = s.lastUpdated;
                        }
                        SharedPreferences.Editor edit = MyApplication.context.getSharedPreferences("TAG", MODE_PRIVATE).edit();
                        edit.putLong("ImagesLastUpdateDate",lastUpdated);
                        edit.commit();
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener!=null)  listener.onComplete();
                    }
                }.execute("");
            }
        });
    }
    public LiveData<List<Image>> getAllImages(){
        LiveData<List<Image>> liveData = AppLocalDb.db.ImageDao().getAll();
        refreshImageList(null);
        return liveData;
    }


    public Image getImage(String id){
        return null;
    }

    public void update(Image image){

    }

    public void delete(Image image){

    }



}
*/