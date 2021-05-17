package com.kararnab.contacts;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;

import timber.log.Timber;

public class ContactsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Either use if(BuildConfig.DEBUG) check or do it using flavors (recommended)
        Timber.plant(new ContactsTimberTree());

        initLocalBroadcast();
    }

    public void initLocalBroadcast() {
        BroadcastReceiver taskReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
              String message = intent.getStringExtra("task")  ;
              //TODO: Work with the message
            }
        };
    }

    private static void enabledStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                .detectAll() //
                .penaltyLog() //
                .penaltyDeath() //
                .build());
    }
}
