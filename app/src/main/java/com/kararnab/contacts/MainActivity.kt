package com.kararnab.contacts

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.HapticFeedbackConstants
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.kararnab.contacts.ContactListAdapter.ContactListener
import com.kararnab.contacts.callbacks.DebouncedOnClickListener
import com.kararnab.contacts.room.Contact
import com.kararnab.contacts.room.ContactViewModel
import com.kararnab.contacts.widgets.EmptyRecyclerView
import timber.log.Timber
import java.io.File

class MainActivity : AppCompatActivity() {
    private var mAddContactFab: FloatingActionButton? = null
    private val mSwipeRefresh: SwipeRefreshLayout by lazy { findViewById(R.id.swipeRefreshContainer) }
    //private var rvContacts: EmptyRecyclerView? = null
    private val rvContacts: EmptyRecyclerView by lazy {findViewById(R.id.rvContacts)}
    var mAppBar: View? = null
    var mDrawerLayout: DrawerLayout? = null
    var mAdapter: ContactListAdapter? = null
    var mWordViewModel: ContactViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_24dp)
        //getSupportActionBar().setHomeButtonEnabled(true);
        initViews()
        initListeners()

        mAdapter = ContactListAdapter(object : ContactListener {
            override fun onItemClicked(contact: Contact) {
                navigateToViewContact(contact)
            }

            override fun onItemLongClicked(position: Int, view: View): Boolean {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                mAdapter!!.getContact(position).isSelected = true
                mAdapter!!.notifyItemChanged(position)
                return true
            }
        })
        rvContacts.adapter = mAdapter
        rvContacts.layoutManager = LinearLayoutManager(this)

        // Get a new or existing ViewModel from the ViewModelProvider.
        mWordViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mWordViewModel!!.allContacts.observe(
            this,
            { contacts -> // Update the cached copy of the contacts in the adapter.
                mAdapter!!.setContacts(contacts)
            })
    }

    private fun initViews() {
        mAppBar = findViewById(R.id.appbar)
        mAddContactFab = findViewById(R.id.fab)
        rvContacts.setEmptyView(findViewById(R.id.emptyContacts))
        mDrawerLayout = findViewById(R.id.drawer_layout)

        // Configure the refreshing colors
        UiUtils.setSwipeRefreshLayoutLoaderColors(mSwipeRefresh)
    }

    private fun initListeners() {
        mAddContactFab!!.setOnClickListener(object : DebouncedOnClickListener(500) {
            override fun onDebouncedClick(view: View?) {

                /*checkCallPermission(new PermissionsCallback() {
                    @Override
                    public void onGranted() {
                        callPhoneNumber("9876543210",true);
                    }
                    @Override
                    public void onRejected() {
                        requestCallPermission();
                    }
                });*/
                Timber.tag("Add ContactPOJO")
                    .e("Manufactured by: %s\n User: %s", Build.MANUFACTURER, Build.DEVICE)
                Snackbar.make(view!!, R.string.work_in_progress, Snackbar.LENGTH_LONG)
                    .setAction(R.string.action, null).show()
                navigateToAddContact(this@MainActivity, null)
            }
        })
        mSwipeRefresh.setOnRefreshListener { //todo: Add the contact sync logic here
            //initDummyData(mAdapter);
            mSwipeRefresh.isRefreshing = false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rvContacts.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                mAppBar!!.isSelected = rvContacts.canScrollVertically(-1)
            }
        } else {
            rvContacts.viewTreeObserver.addOnScrollChangedListener { // int scrollY = rvContacts.getScrollY(); // For ScrollView
                // int scrollX = rvContacts.getScrollX(); // For HorizontalScrollView
                mAppBar!!.isSelected = rvContacts.canScrollVertically(-1)
            }
        }
    }

    fun navigateToViewContact(contact: Contact) {
        val intent = Intent(this, ContactViewActivity::class.java)
        putContactInIntent(intent, contact)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu, this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            R.id.action_settings -> return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (mDrawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            finish()
        }
    }

    /**
     * Update the Contact app without Playstore
     */
    private fun downloadApkUpdate(url: String) {
        val appDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = getString(R.string.app_name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || packageManager.canRequestPackageInstalls()) {
                val toInstall = File(appDirectory, "$fileName.apk")
                val apkUri = FileProvider.getUriForFile(
                    this@MainActivity,
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
                    this@MainActivity,
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

    companion object {
        @JvmStatic
        fun navigateToAddContact(context: Context, contact: Contact?) {
            val intent = Intent(context, AddEditContact::class.java)
            intent.putExtra("editContact", contact != null)
            if (contact != null) {
                putContactInIntent(intent, contact)
            }
            context.startActivity(intent)
        }

        fun putContactInIntent(intent: Intent, contact: Contact) {
            intent.putExtra("id", contact.id)
            intent.putExtra("phoneNo", contact.phone)
            intent.putExtra("name", contact.name)
            intent.putExtra("company", contact.company)
            intent.putExtra("email", contact.emailId)
            intent.putExtra("notes", contact.notes)
        }
    }
}