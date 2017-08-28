package com.example.shemeshda.finalproject;

import android.app.Application;
import android.content.Context;

/**
 * Created by shemeshda on 22/08/2017.
 */

public class finalProject extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }


    public static Context getMyContext(){
        return context;
    }
}
