package com.dk.blackhole.viewModels;

import androidx.lifecycle.ViewModel;

import com.dk.blackhole.models.user.User;
import com.dk.blackhole.models.user.UserModelHelper;
import com.dk.blackhole.models.user.UserModelHelper.listenerOnComplete;


public class SignInActivityViewModel extends ViewModel {

    private static SignInActivityViewModel instance;
    private UserModelHelper mUserModelHelper;

    private SignInActivityViewModel(){
        mUserModelHelper = UserModelHelper.getInstance();
    }

    public static SignInActivityViewModel getInstance(){
        if( instance == null){instance = new SignInActivityViewModel();}
        return instance;
    }

    public void getUserByEmail(String userEmail, listenerOnComplete<User> listenerOnComplete){mUserModelHelper.getUserByEmail(userEmail, listenerOnComplete);}

    public void insertNewUserToDB(User newUser, listenerOnComplete<User> listenerOnComplete) { mUserModelHelper.insertNewUser(newUser, listenerOnComplete);}

    public void updateUser(User updatedUser, listenerOnComplete<User> listenerOnComplete) { mUserModelHelper.updateUser(updatedUser, listenerOnComplete);}
}
