package com.kararnab.contacts.v2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.kararnab.contacts.databinding.FragmentContactViewBinding

class ContactViewFragment: Fragment() {

    val args: ContactViewFragmentArgs by navArgs()
    private var _binding: FragmentContactViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true);
        _binding = FragmentContactViewBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.contact = args.contact;

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun getInstance(): ContactListFragment {
            return ContactListFragment()
        }
    }
}