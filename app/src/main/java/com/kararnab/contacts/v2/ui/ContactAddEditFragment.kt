package com.kararnab.contacts.v2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kararnab.contacts.R
import com.kararnab.contacts.v2.data.database.entities.Contact
import com.kararnab.contacts.v2.data.database.entities.Phone
import com.kararnab.contacts.v2.data.database.entities.PhoneContact
import com.kararnab.contacts.v2.viewmodels.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactAddEditFragment: Fragment() {

    private var edit_name: EditText? = null
    private var edit_company: EditText? = null
    private var edit_phone: EditText? = null
    private var edit_email: EditText? = null
    private var edit_notes: EditText? = null
    private var editImg: CardView? = null
    private var btnDelete: Button? = null

    private val mContactViewModel : ContactViewModel by viewModels()
    val args: ContactAddEditFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()
        val view = inflater.inflate(R.layout.fragment_add_contact, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)
        val isEditMode = args.isEditMode
        val contact = args.contact
        if(isEditMode) {
            populateFields(contact.phone[0].phoneNumber, contact.user.name, contact.user.company, contact.user.emailId, contact.user.notes)
            //TODO: Update the titleBar to getString(R.string.edit_contact)
        } else {
            //Add mode
            btnDelete?.visibility = View.GONE
            //TODO: Update the titleBar to getString(R.string.add_contact)
        }
        editImg?.setOnClickListener {
            val editedContact = PhoneContact(
                Contact(contact.user.id,
                    edit_name!!.text.toString(),
                    edit_company!!.text.toString(),
                    edit_email!!.text.toString(),
                    edit_notes!!.text.toString()),
                listOf(Phone(0, contact.user.id, edit_phone!!.text.toString(), "Personal"))
            )
            mContactViewModel.upsert(editedContact) {
                findNavController().popBackStack()
            }
        }
        btnDelete?.setOnClickListener{
            val editedContact = PhoneContact(
                contact.user,
                contact.phone
            )
            mContactViewModel.delete(editedContact) {
                findNavController().popBackStack()
            }
        }
    }

    fun initViews(parent: View) {
        with(parent){
            edit_name = findViewById(R.id.edit_name)
            edit_company = findViewById(R.id.edit_company)
            edit_phone = findViewById(R.id.edit_phone)
            edit_email = findViewById(R.id.edit_email)
            edit_notes = findViewById(R.id.edit_notes)
            editImg = findViewById(R.id.editImg)
            btnDelete = findViewById(R.id.btnDelete)
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

    companion object {
        @JvmStatic
        fun getInstance(): ContactAddEditFragment {
            return ContactAddEditFragment()
        }
    }
}