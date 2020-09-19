package com.dk.blackhole;

import android.app.Application;
import android.content.Context;


/**
 * This class initialized before any activity and it hold context
 * and it static so any outher class can pul it for any use.
 */
public class App extends Application{


    static public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }
}
