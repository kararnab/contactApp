package com.kararnab.contacts.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kararnab.contacts.R

class ContactDetailFragment: Fragment() {
    interface Callbacks {
        fun navigateToAddContact(contactId: String)
    }
    private var callbacks : Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_detail, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup any handles to view objects here

    }

    companion object {
        @JvmStatic
        fun getInstance(contactId: String): ContactDetailFragment {
            val bundle = Bundle().apply {
                putString("contactId", contactId) // The rest of them can be fetched on the go
            }
            return ContactDetailFragment().apply { arguments = bundle }
        }
    }

}