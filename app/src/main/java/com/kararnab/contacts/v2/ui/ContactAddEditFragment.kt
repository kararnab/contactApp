package com.kararnab.contacts.v2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.kararnab.contacts.databinding.FragmentAddContactBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactAddEditFragment: Fragment() {

    val args: ContactViewFragmentArgs by navArgs()
    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()

        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
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
        fun getInstance(): ContactAddEditFragment {
            return ContactAddEditFragment()
        }
    }
}