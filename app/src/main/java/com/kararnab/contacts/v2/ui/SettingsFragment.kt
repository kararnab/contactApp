package com.kararnab.contacts.v2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.kararnab.contacts.R


class SettingsFragment: Fragment() {

    val args: ContactViewFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        return view
    }

    companion object {
        @JvmStatic
        fun getInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}