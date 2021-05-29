package com.kararnab.contacts.v2.bindingadapters

import android.text.TextUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.kararnab.contacts.UiUtils
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

        @BindingAdapter("initialsText")
        @JvmStatic
        fun initialsText(tvLogo: TextView, username: String) {
            if (TextUtils.isEmpty(username)){
                tvLogo.text = ""
            } else {
                tvLogo.text = username[0].toString()
            }

        }

        @BindingAdapter("computeCardBackgroundColor")
        @JvmStatic
        fun computeCardBackgroundColor(card: CardView, username: String) {
            card.setCardBackgroundColor(UiUtils.materialColor(username.length))
        }
    }
}