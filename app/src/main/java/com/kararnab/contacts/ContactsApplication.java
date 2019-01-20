package com.kararnab.contacts;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

public class ContactsApplication extends Application {

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        setupLeakCanary();
        //Either use if(BuildConfig.DEBUG) check or do it using flavors (recommended)
        Timber.plant(new ContactsTimberTree());

    }

    /**
     * Build a RefWatcher. This is an object to which you’re going to pass instances and it will
     * check if they’re being garbage collected. This works with any object, not just activities.
     * @param context Context to get application
     * @return RefWatcher
     */
    public static RefWatcher getRefWatcher(Context context) {
        ContactsApplication application = (ContactsApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    protected void setupLeakCanary() {
        //enabledStrictMode();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);
    }
    private static void enabledStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                .detectAll() //
                .penaltyLog() //
                .penaltyDeath() //
                .build());
    }
}
