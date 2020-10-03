package com.dk.blackhole.models.uses_and_albums;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.dk.blackhole.models.album.Album;
import com.dk.blackhole.models.album.AlbumsModelHelper;
import com.dk.blackhole.models.user.UserModelHelper;
import com.dk.blackhole.utils.Utils;

import java.util.List;

public class UsersAndAlbumsModelHelper {

    private final String TAG = UsersAndAlbumsModelHelper.class.getSimpleName();

    private static UsersAndAlbumsModelHelper instance;
    private static UsersAndAlbumsFirebase mUsersAndAlbumsFirebase;
    private static UsersAndAlbumsDAO mUsersAndAlbumsLocalDB;
    private static AlbumsModelHelper mAlbumsModelHelper;

    private boolean isTheLiveDataInitialized = false;
    private LiveData<List<UsersAndAlbums>> mUserAndAlbumsLiveDataList;



    public interface ListenerOnComplete<T>{
        void onComplete(T data);
    }

    private UsersAndAlbumsModelHelper() { }

    public static UsersAndAlbumsModelHelper getInstance(){
        if(instance == null){
            instance = new UsersAndAlbumsModelHelper();
            mUsersAndAlbumsFirebase = UsersAndAlbumsFirebase.getInstance();
            mUsersAndAlbumsLocalDB = UsersAndAlbumsLocalDb.db.usersAndAlbumsDAO();
            mAlbumsModelHelper = AlbumsModelHelper.getInstance();
        }
        return instance;
    }


    /**
     * For getting all users albums we need to do next things:
     *                     - Get from table "UsersAndAlbums" albums ides only of albums that shared with this user or created by this user.
     *                     - After we got the albums ides we loading all the albums.
     *
     *
     * @param userId
     * @return
     */
    public LiveData[] getUserAlbums(String userId) {
        Log.i(TAG,"UsersAndAlbumsModelHelper.getUserAlbums() Started");
        String[] albumsIdes = {"empty"};
        /* Getting all albums that created by this user or shared we him/her */
        LiveData<List<UsersAndAlbums>> userAlbumsAndSharedAlbumsFromLocalDb = mUsersAndAlbumsLocalDB.getAllUsersAlbums(userId);
        List<UsersAndAlbums> arrayListUserAndAlbums = mUsersAndAlbumsLocalDB.getAllUsersAlbumsList(userId);
        /* Pulling the albums ides */
        if(arrayListUserAndAlbums != null && arrayListUserAndAlbums.size() != 0) {
            albumsIdes = new String[arrayListUserAndAlbums.size()];
            for (int i = 0; i < albumsIdes.length; i++) {
                albumsIdes[i] = arrayListUserAndAlbums.get(i).getAlbumId();
            }
        }
        /* Now when we have all the needed albums ides we can load them from the local DB.
         *  By loading the albums we returning them wrapped by live data so if the data will changed the ui will automatically updated */
        LiveData<List<Album>> allUserAlbums = mAlbumsModelHelper.getAlbumsByIdes(albumsIdes, null);
        /* The refresh function will check if there is update on the remote DB */
        refreshUserAndAlbumsList(userId, null);
        LiveData[] liveData = new LiveData[]{userAlbumsAndSharedAlbumsFromLocalDb, allUserAlbums};
        Log.i(TAG,"UsersAndAlbumsModelHelper.getUserAlbums() Finished");
        return liveData;
    }


    /**
     * - This method ask the remote Db if there is updates in table "UsersAndAlbums" for a specific user.
     * - If there is updates we update the table "UsersAndAlbums" in the local Db and we update the table "Albums" in the local DB
     *
     *  @param userId
     * @param listener
     */
    public void refreshUserAndAlbumsList(String userId, ListenerOnComplete listener) {
        mUsersAndAlbumsFirebase.getAlbumsByUserId(userId, new ListenerOnComplete<List<UsersAndAlbums>>() {
            @Override
            public void onComplete(List<UsersAndAlbums> data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        for(UsersAndAlbums usersAndAlbums: data){
                            mUsersAndAlbumsLocalDB.insertUsersAndAlbums(usersAndAlbums);
                        }
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

    public void addNewUsersAndAlbumsEntity(String albumId, String userId) {
        String usersAndAlbumsId = Utils.getNewUniqueId();
        String timeOfCreation = Utils.getCurrentTime();
        UsersAndAlbums usersAndAlbums = new UsersAndAlbums(usersAndAlbumsId, userId,albumId, false, timeOfCreation, timeOfCreation);
        mUsersAndAlbumsFirebase.addNewUsersAndAlbumsEntity(usersAndAlbums, new ListenerOnComplete<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        mUsersAndAlbumsLocalDB.insertUsersAndAlbums(usersAndAlbums);
                        return "";
                    }
                }.execute("");
            }
        });

    }

    public void registerOnChanges(String userId){
        mUsersAndAlbumsFirebase.listenToMultipleOnlyChanges(userId, new ListenerOnComplete<UsersAndAlbums>() {
            @Override
            public void onComplete(UsersAndAlbums data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        mUsersAndAlbumsLocalDB.insertUsersAndAlbums(data);
                        return "";
                    }
                }.execute("");
            }
        });
    }

}
