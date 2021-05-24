package com.kararnab.contacts.v2.bindingadapters

import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.kararnab.contacts.v2.data.database.Contact
import com.kararnab.contacts.v2.ui.ContactListFragmentDirections
import java.lang.Exception

class ContactRowBinding {
    companion object {
        @BindingAdapter("onContactClickListener")
        @JvmStatic
        fun onContactClickListener(contactRowLayout: LinearLayout, result: Contact) {
            contactRowLayout.setOnClickListener {
                try {
                    val action = ContactListFragmentDirections.actionContactListFragmentToContactViewFragment(result)
                    contactRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                }
            }
        }
    }
}