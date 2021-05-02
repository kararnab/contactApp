package com.kararnab.contacts.polling

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.kararnab.contacts.MainActivity
import com.kararnab.contacts.R
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Foreground service to do a task repeatedly at regular intervals, ADD THIS SERVICE TO MANIFEST BEFORE USING
 */
class PollingService: Service() {
    companion object {
        private const val SYNC_COMMUNICATION_CHANNEL_ID = "syncCommunication"
        private const val SYNC_COMMUNICATION_CHANNEL_NAME = "New Data Alert"
        const val FOREGROUND_NOTIFICATION_ID = 0x112

        const val DEFAULT_START_TIME_OF_SHIFT = "07:00:00"
        const val DEFAULT_END_TIME_OF_SHIFT = "16:00:00"

        const val FOREGROUND: String = "com.kararnab.contacts.polling.FOREGROUND"
        const val pollIntervalInMillis: Long = 60000 //Polling in every 60 seconds

        fun startOrStopPollingService(context: Context, isStart: Boolean) {
            val intent = Intent(PollingService.FOREGROUND)
            intent.setClass(context, PollingService::class.java)
            if(isStart) {
                context.startService(intent)
            } else {
                context.stopService(intent)
            }
        }

        fun createNotificationChannel(context : Context) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //val sound = Uri.parse("android.resource://"+context.packageName + "/" + R.raw.soundName)
                val notificationChannel = NotificationChannel(SYNC_COMMUNICATION_CHANNEL_ID, SYNC_COMMUNICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.description = "Channel for syncing data"
                notificationChannel.setShowBadge(true)

                val notificationManager : NotificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        fun sendMessage(context: Context, localBroadcastManager: LocalBroadcastManager, task: String){
            try {
                val intent = Intent("Update")
                intent.putExtra("task", task)
                localBroadcastManager.sendBroadcast(intent)
                // showNotification(context, "Contacts", "Sync Done")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun isTimeBetweenHours(currentDate: Date, startTime: String, endTime: String) :Boolean {
            val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
            try {
                val startTimeCalendar = Calendar.getInstance()
                startTimeCalendar.time = simpleDateFormat.parse(startTime)!!
                startTimeCalendar.add(Calendar.DATE, 1)
                val endTimeCalendar = Calendar.getInstance()
                endTimeCalendar.time = simpleDateFormat.parse(endTime)!!
                endTimeCalendar.add(Calendar.DATE, 1)
                val currentTime = simpleDateFormat.format(currentDate)
                val currentTimeCalendar = Calendar.getInstance()
                currentTimeCalendar.time = simpleDateFormat.parse(currentTime)!!
                currentTimeCalendar.add(Calendar.DATE, 1)
                val x = currentTimeCalendar.time
                return x.after(startTimeCalendar.time) && x.before(endTimeCalendar.time)
            } catch (e: ParseException) {
                return false
            }
        }
        fun getCompatNotification(context: Context, title: String, body: String): Notification {
            val startIntent = Intent(context, MainActivity::class.java)
            val contentIntent: PendingIntent = PendingIntent.getActivity(context, 1000, startIntent, 0)
            val builder = NotificationCompat.Builder(context, SYNC_COMMUNICATION_CHANNEL_ID)
            builder
                    .setSmallIcon(R.drawable.ic_email_24dp)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(contentIntent)
            return builder.build()
        }
    }

    private fun shouldStopSelf(userState: UserState): Boolean{
        return when(userState) {
            UserState.LOGGED_OUT -> true
            UserState.INACTIVE_HOURS -> true
            else -> false
        }
    }

    private val job = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        syncAllTasks()
        return START_STICKY
    }

    private fun startForeground() {
        startForeground(FOREGROUND_NOTIFICATION_ID, getCompatNotification(this, "Contacts", "Is syncing your data in background"))
    }

    private fun stopForegroundAndRemoveNotifications() {
        stopForeground(true)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun syncAllTasks() = serviceScope.launch(job) {
        startForeground()
        while(true) {
            var authKey: String
            try {
                val AUTH_STORE: String =  "{}" //TODO:
                authKey = JSONObject(AUTH_STORE).getString("authKey")
            }catch (e: Exception) {
                authKey = ""
            }

            val startTimeOfShift = DEFAULT_START_TIME_OF_SHIFT
            val endTimeOfShift = DEFAULT_END_TIME_OF_SHIFT

            val userState: UserState = when {
                TextUtils.isEmpty(authKey) -> {
                    UserState.LOGGED_OUT
                }
                isTimeBetweenHours(Date(), startTimeOfShift, endTimeOfShift) -> {
                    UserState.ACTIVE_HOURS
                }
                else -> {
                    UserState.INACTIVE_HOURS
                }
            }
            if(shouldStopSelf(userState)) {
                //stop polling
                break
            } else {
                sendMessage(this@PollingService, LocalBroadcastManager.getInstance(this@PollingService), "{ syncId: 1 }")
                delay(pollIntervalInMillis)
            }
        }
        stopForegroundAndRemoveNotifications()
        stopSelf()
    }
}

enum class UserState {
    LOGGED_OUT,
    ACTIVE_HOURS,
    INACTIVE_HOURS
}