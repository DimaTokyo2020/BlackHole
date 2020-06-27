package com.DK.blackhole;

import android.app.Application;
import android.content.Context;


public class App extends Application{


    static public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
