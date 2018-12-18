package com.amp.news.Application;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by amal on 18/12/18.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
