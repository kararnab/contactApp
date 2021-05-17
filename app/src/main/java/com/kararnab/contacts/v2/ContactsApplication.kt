package com.kararnab.contacts.v2

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import com.kararnab.contacts.ContactsTimberTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ContactsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        //Either use if(BuildConfig.DEBUG) check or do it using flavors (recommended)
        Timber.plant(ContactsTimberTree())
        initLocalBroadcast()
    }

    private fun initLocalBroadcast() {
        val taskReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val message = intent.getStringExtra("task")
                //TODO: Work with the message
            }
        }
    }

    private fun enabledStrictMode() {
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder() //
                .detectAll() //
                .penaltyLog() //
                .penaltyDeath() //
                .build()
        )
    }
}