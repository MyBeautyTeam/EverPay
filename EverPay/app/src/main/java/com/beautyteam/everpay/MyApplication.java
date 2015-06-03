package com.beautyteam.everpay;

import android.app.Application;

import com.flurry.android.FlurryAgent;

/**
 * Created by Admin on 03.06.2015.
 */
public class MyApplication extends Application{
    public final static String MY_FLURRY_APIKEY = "Q5K2MMRZWRPGBN5W7ZRP";
    @Override
    public void onCreate() {
        super.onCreate();

        // configure Flurry
        FlurryAgent.setLogEnabled(false);
        FlurryAgent.setCaptureUncaughtExceptions(true);
        // init Flurry
        FlurryAgent.init(this, MY_FLURRY_APIKEY);
    }

}

