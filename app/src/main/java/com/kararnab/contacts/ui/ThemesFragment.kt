package com.kararnab.contacts.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.kararnab.contacts.R

//TODO: unused
class ThemesFragment: Fragment() {
    interface Callbacks {
        fun onThemeChanged(themeMode: AppCompatDelegate.NightMode)
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
        val view = inflater.inflate(R.layout.fragment_themes, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup any handles to view objects here

    }

    companion object {
        @JvmStatic
        fun getInstance(): ThemesFragment {
            return ThemesFragment()
        }
    }
}