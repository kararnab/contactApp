package com.kararnab.contacts

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.kararnab.contacts.UiUtils.callPhoneNumber
import com.kararnab.contacts.UiUtils.checkCallPermission
import com.kararnab.contacts.UiUtils.requestCallPermission
import com.kararnab.contacts.callbacks.DebouncedOnClickListener
import com.kararnab.contacts.callbacks.PermissionsCallback
import java.util.*
import kotlin.math.abs

class ContactViewActivity : AppCompatActivity() {
    var mEmailId: TextView? = null
    var mPhoneNumber: TextView? = null
    var tvCompany: TextView? = null
    var mCollapsingToolbar: CollapsingToolbarLayout? = null
    var mAppBar: AppBarLayout? = null
    var mFABEdit: View? = null
    var mActionCall: View? = null
    var mActionEmail: View? = null
    var mCollapsedMenu: Menu? = null
    private var appBarExpanded = true
    var id: String? = null
    var phoneNo: String? = null
    var name: String? = null
    var company: String? = null
    var email: String? = null
    var notes: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_view)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
        initViews()
        id = intent.getStringExtra("id")
        phoneNo = intent.getStringExtra("phoneNo")
        name = intent.getStringExtra("name")
        company = intent.getStringExtra("company")
        email = intent.getStringExtra("email")
        notes = intent.getStringExtra("notes")
        populateFields(phoneNo, name, company, email, notes)
        mFABEdit!!.setOnClickListener { editContact(id, phoneNo, name, company, email, notes) }
        mActionCall!!.setOnClickListener(object : DebouncedOnClickListener(500) {
            override fun onDebouncedClick(v: View?) {
                checkCallPermission(this@ContactViewActivity, object : PermissionsCallback {
                    override fun onGranted() {
                        callPhoneNumber(this@ContactViewActivity, phoneNo, true)
                    }

                    override fun onRejected() {
                        requestCallPermission(this@ContactViewActivity)
                    }
                })
            }
        })
        mAppBar!!.addOnOffsetChangedListener(OnOffsetChangedListener { _, verticalOffset ->
            if (abs(verticalOffset) > 200) {
                appBarExpanded = false
                invalidateOptionsMenu()
            } else {
                appBarExpanded = true
                invalidateOptionsMenu()
            }
        })
    }

    fun initViews() {
        mAppBar = findViewById(R.id.appbar)
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar)
        mPhoneNumber = findViewById(R.id.phoneNumber)
        mEmailId = findViewById(R.id.emailId)
        mFABEdit = findViewById(R.id.fabEdit)
        mActionCall = findViewById(R.id.action_call)
        mActionEmail = findViewById(R.id.action_mail)
        tvCompany = findViewById(R.id.tvCompany)
    }

    private fun populateFields(
        phoneNo: String?,
        name: String?,
        company: String?,
        email: String?,
        notes: String?
    ) {
        mCollapsingToolbar!!.title = name
        mPhoneNumber!!.text = formattedPhoneNo(phoneNo)
        mEmailId!!.text = email
        tvCompany!!.text = company
    }

    fun editContact(
        id: String?,
        phoneNo: String?,
        name: String?,
        company: String?,
        email: String?,
        notes: String?
    ) {
        /*val contact = Contact(id!!, phoneNo!!, name!!, company!!, email!!, notes!!)
        navigateToAddContact(this@ContactViewActivity, contact)*/
    }

    fun formattedPhoneNo(phoneNo: String?): String {
        return phoneNo!!.replaceFirst("(\\d{3})(\\d{3})(\\d+)".toRegex(), "($1) $2-$3")
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (mCollapsedMenu != null && !appBarExpanded) {
            //collapsed
            mCollapsedMenu!!.add(Menu.NONE, 1, Menu.NONE, "Edit")
                .setIcon(R.drawable.ic_edit_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
        return super.onPrepareOptionsMenu(mCollapsedMenu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_empty, menu)
        mCollapsedMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) {
            editContact(id, phoneNo, name, company, email, notes)
            return true
        } else if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val REQUEST_CALL = 0
    }
}