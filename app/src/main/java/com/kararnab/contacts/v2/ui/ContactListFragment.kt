package com.kararnab.contacts.v2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kararnab.contacts.databinding.FragmentContactListBinding
import com.kararnab.contacts.v2.adapters.ContactsAdapter
import com.kararnab.contacts.v2.data.database.Contact
import com.kararnab.contacts.v2.viewmodels.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactListFragment : Fragment() {

    private val mContactViewModel : ContactViewModel by viewModels()
    private val mAdapter: ContactsAdapter by lazy { ContactsAdapter() }

    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentContactListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.contactsViewModel = mContactViewModel

        binding.fab.setOnClickListener {

            mContactViewModel.insert(Contact("2","9876543210", "Rohan", "Company1", "", "Notes here it is."))
        }

        setupRecyclerView()

        lifecycleScope.launchWhenResumed{
            mContactViewModel.allContacts.observe(viewLifecycleOwner, { contacts ->
                mAdapter.setData(contacts)
            })
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup any handles to view objects here



    }

    private fun setupRecyclerView() {
        binding.rvContacts.adapter = mAdapter
        binding.rvContacts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvContacts.setEmptyView(binding.emptyContacts)
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