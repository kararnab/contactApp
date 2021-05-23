package com.kararnab.contacts.v2.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kararnab.contacts.R
import com.kararnab.contacts.databinding.FragmentContactListBinding
import com.kararnab.contacts.v2.adapters.ContactsAdapter
import com.kararnab.contacts.v2.data.database.Contact
import com.kararnab.contacts.v2.viewmodels.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

/**
 * https://stackoverflow.com/questions/30369246/implementing-searchview-as-per-the-material-design-guidelines
 */
@AndroidEntryPoint
class ContactListFragment : Fragment() {

    private val mContactViewModel : ContactViewModel by viewModels()
    private val mAdapter: ContactsAdapter by lazy { ContactsAdapter() }

    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!

    private lateinit var mToolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar!!.show()
        _binding = FragmentContactListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.contactsViewModel = mContactViewModel
        mToolbar = binding.toolbar
        //activity?.setActionBar(mToolbar);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup any handles to view objects here
        binding.fab.setOnClickListener {
            mContactViewModel.insert(Contact("2","9876543210", "Rohan", "Company1", "rohan@gmail.com", "Notes here it is."))
        }

        setupRecyclerView()

        mContactViewModel.allContacts.observe(viewLifecycleOwner, { contacts ->
            mAdapter.setData(contacts)
        })
    }

    private fun setupRecyclerView() {
        binding.rvContacts.adapter = mAdapter
        binding.rvContacts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvContacts.setEmptyView(binding.emptyContacts)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                try {
                    val action = ContactListFragmentDirections.actionContactListFragmentToSettingsFragment()
                    NavHostFragment.findNavController(this).navigate(action)
                } catch (e: Exception) {
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_contact_list, menu)
        val searchViewItem = menu.findItem(R.id.app_bar_search)

        searchViewItem.setOnActionExpandListener(
            object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    // Called when SearchView is collapsing
                    Log.e("StopSearch:", "Done")
                    binding.fab.show()
                    return true
                }

                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    // Called when SearchView is expanding
                    (searchViewItem.actionView as SearchView).setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String): Boolean {
                            return true
                        }

                        override fun onQueryTextChange(newText: String): Boolean {
                            Log.e("Search:", newText)
                            return true
                        }

                    })
                    binding.fab.hide()
                    return true
                }
            })

        super.onCreateOptionsMenu(menu, inflater)
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