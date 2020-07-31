package com.dk.blackhole.viewModels;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivityViewModel extends ViewModel {

    private FirebaseAuth mUserAuth;
    private FirebaseUser mUser;


    public HomeActivityViewModel() {
        mUserAuth = FirebaseAuth.getInstance();//Get user info
        mUser = mUserAuth.getCurrentUser();
    }





    public String getUserEmail(){ return mUser.getEmail(); }

    public String getUserDisplayName(){return mUser.getDisplayName();}

    public Uri getUserPhotoUrl(){return mUser.getPhotoUrl();}

    public void signOut(){ mUserAuth.signOut();}

}
