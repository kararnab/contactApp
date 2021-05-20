package com.kararnab.contacts.v2.ui

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import com.kararnab.contacts.BuildConfig
import com.kararnab.contacts.R
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ContactMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
        setContentView(R.layout.activity_contact_main)
    }

    /**
     * Update the Contact app without Play store
     */
    private fun downloadApkUpdate(url: String) {
        val appDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = getString(R.string.app_name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || packageManager.canRequestPackageInstalls()) {
                val toInstall = File(appDirectory, "$fileName.apk")
                val apkUri = FileProvider.getUriForFile(
                    this@ContactMainActivity,
                    BuildConfig.APPLICATION_ID + ".provider",
                    toInstall
                )
                downloadUpdate(toInstall, apkUri, url)
                val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
                    override fun onReceive(ctxt: Context, intent: Intent) {
                        val install = Intent(Intent.ACTION_INSTALL_PACKAGE)
                        install.data = apkUri
                        install.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        startActivity(install)
                        unregisterReceiver(this)
                        finish()
                    }
                }
                registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            } else {
                Toast.makeText(
                    this@ContactMainActivity,
                    "Please allow app install from settings",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            //Legacy code for version less than Nougat
            var destination = "$appDirectory/"
            destination += fileName
            val apkUri = Uri.parse("file://$destination")
            val file = File(destination)
            downloadUpdate(file, apkUri, url)
            val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(ctxt: Context, intent: Intent) {
                    val install = Intent(Intent.ACTION_VIEW)
                    install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    install.setDataAndType(
                        apkUri,
                        "application/vnd.android.package-archive"
                    )
                    startActivity(install)
                    unregisterReceiver(this)
                    finish()
                }
            }
            registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        }
    }

    private fun downloadUpdate(file: File, apkUri: Uri, url: String) {
        //todo
        if (file.exists()) {
            file.delete()
        }
        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle("Update App")
        request.setDescription("Download Contacts New Version")
        request.setDestinationUri(apkUri)
        val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }
}