package com.kararnab.contacts

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.kararnab.contacts.v2.viewmodels.ContactViewModel
import java.util.*

class AddEditContact : AppCompatActivity() {
    private var edit_name: EditText? = null
    private var edit_company: EditText? = null
    private var edit_phone: EditText? = null
    private var edit_email: EditText? = null
    private var edit_notes: EditText? = null
    var isEditMode = false
    var mContactViewModel: ContactViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
        initViews()
        isEditMode = intent.getBooleanExtra("editContact", false)
        if (isEditMode) {
            supportActionBar!!.title = getString(R.string.edit_contact)
            val id = intent.getStringExtra("id")
            val phoneNo = intent.getStringExtra("phoneNo")
            val name = intent.getStringExtra("name")
            val company = intent.getStringExtra("company")
            val email = intent.getStringExtra("email")
            val notes = intent.getStringExtra("notes")
            populateFields(phoneNo, name, company, email, notes)
        } else {
            supportActionBar!!.title = getString(R.string.add_contact)
        }
    }

    fun populateFields(
        phoneNo: String?,
        name: String?,
        company: String?,
        email: String?,
        notes: String?
    ) {
        edit_name!!.setText(name)
        edit_company!!.setText(company)
        edit_phone!!.setText(phoneNo)
        edit_email!!.setText(email)
        edit_notes!!.setText(notes)
        edit_name!!.setSelection(name!!.length)
    }

    fun initViews() {
        edit_name = findViewById(R.id.edit_name)
        edit_company = findViewById(R.id.edit_company)
        edit_phone = findViewById(R.id.edit_phone)
        edit_email = findViewById(R.id.edit_email)
        edit_notes = findViewById(R.id.edit_notes)
        mContactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
    }

    fun saveContact(view: View?) {
        /*val id: Int?
        id = if (isEditMode) {
            intent.getIntExtra("id")
        } else {
            UUID.randomUUID().toString()
        }
        val contact = Contact(
            id!!,
            edit_phone!!.text.toString(),
            edit_name!!.text.toString(),
            edit_company!!.text.toString(),
            edit_email!!.text.toString(),
            edit_notes!!.text.toString()
        )
        mContactViewModel!!.insert(contact)*/
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}