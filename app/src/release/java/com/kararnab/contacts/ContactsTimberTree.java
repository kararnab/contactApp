package com.kararnab.contacts;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import timber.log.Timber;

import static android.util.Log.INFO;

public class ContactsTimberTree extends Timber.Tree {

    @Override
    public boolean isLoggable(@Nullable String tag,int priority) {
        return priority >= INFO;
    }

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
        if(t!=null){
            if(priority == Log.ERROR){
                FakeCrashLibrary.logError(t);
            }else if(priority == Log.WARN){
                FakeCrashLibrary.logWarn(t);
            }
        }
    }

    private static class FakeCrashLibrary {
        static void logWarn(Throwable t){

        }
        static void logError(Throwable t){

        }
    }
}