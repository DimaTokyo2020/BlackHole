package com.dk.blackhole.models.user;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;


public class UserModelHelper {

    private final String TAG = UserModelHelper.class.getSimpleName();

    private static UserModelHelper instance;
    private UsersFirebase mUsersFirebase;
    private UserDAO mUserLocalDb;


    public void updateUser(User updatedUser, listenerOnComplete<User> listenerOnComplete) { mUsersFirebase.updateUser(updatedUser, listenerOnComplete);}

    public interface listenerOnComplete<T>{
        void onComplete(T data);
    }

    private UserModelHelper() {
        mUsersFirebase = UsersFirebase.getInstance();
        mUserLocalDb = UserLocalDb.db.userDAO();
    }

    public static UserModelHelper getInstance(){
        if(instance == null){instance = new UserModelHelper();}
        return instance;
    }







    @SuppressLint("StaticFieldLeak")
    public void insertNewUser(User newUser, listenerOnComplete<User> listenerOnComplete){
        new AsyncTask<String,String,String>(){
            @Override
            protected String doInBackground(String... strings) {
                mUsersFirebase.insertNewUser(newUser, listenerOnComplete);
                mUserLocalDb.insertNewUser(newUser);
                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.i(TAG,"new user inserted to the local and remote DB");
            }
        }.execute("");
    }


    public void getUserByEmail(String userEmail, listenerOnComplete<User> listenerOnComplete){ mUsersFirebase.getUserByEmail(userEmail, listenerOnComplete); }




    private boolean isUserWithThisEmailExistInLocalDB(User user){
        if(false == user.getEmail().isEmpty()) {
            final String userEmail = user.getEmail();
            User userFromLocalDb = UserLocalDb.db.userDAO().getUserByEmail(userEmail);
            if(userFromLocalDb == null){
                Log.i(TAG, "The user: " + user + " doesn't exist in localDB");
                return false;
            }
            else {
                Log.i(TAG, "The user: " + user + " exist in localDB");
                return true;
            }
        }
        else{ Log.i(TAG, "The user: " + user + " doesn't have an email");}

        return false;
    }

    private void isUserWithThisEmailExistInRemoteDB(User user){
        if(false == user.getEmail().isEmpty()) {
            final String userEmail = user.getEmail();
            UsersFirebase.getInstance().getUserByEmail( userEmail, new listenerOnComplete<User>() {
                @Override
                public void onComplete(User userFromRemoteDB) {
                    if (userFromRemoteDB == null) {
                        Log.i(TAG, "The user: " + user + " doesn't exist in localDB");

                    } else {
                        Log.i(TAG, "The user: " + user + " exist in localDB");

                    }
                }
            });
        }
        else{ Log.i(TAG, "The user: " + user + " doesn't have an email");}


    }
}
