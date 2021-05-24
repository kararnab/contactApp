package com.kararnab.contacts.v2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.kararnab.contacts.UiUtils.callPhoneNumber
import com.kararnab.contacts.UiUtils.checkCallPermission
import com.kararnab.contacts.UiUtils.requestCallPermission
import com.kararnab.contacts.callbacks.DebouncedOnClickListener
import com.kararnab.contacts.callbacks.PermissionsCallback
import com.kararnab.contacts.databinding.FragmentContactViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactViewFragment: Fragment() {

    val args: ContactViewFragmentArgs by navArgs()
    private var _binding: FragmentContactViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()

        _binding = FragmentContactViewBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.contact = args.contact;

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.fabEdit.setOnClickListener {
            val action = ContactViewFragmentDirections.actionContactViewFragmentToContactAddEditFragment(args.contact, true)
            NavHostFragment.findNavController(this).navigate(action)
        }
        binding.actionCall.setOnClickListener {
            object : DebouncedOnClickListener(500) {
                override fun onDebouncedClick(v: View?) {
                    checkCallPermission(requireContext(), object : PermissionsCallback {
                        override fun onGranted() {
                            val phoneNo = binding.phoneNumber.text.toString()
                            callPhoneNumber(requireContext(), phoneNo, true)
                        }

                        override fun onRejected() {
                            requestCallPermission(requireActivity())
                        }
                    })
                }
            } }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun getInstance(): ContactViewFragment {
            return ContactViewFragment()
        }
    }
}