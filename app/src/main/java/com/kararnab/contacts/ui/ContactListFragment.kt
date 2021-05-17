package com.kararnab.contacts.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.kararnab.contacts.ContactListAdapter
import com.kararnab.contacts.ContactListAdapter.ContactListener
import com.kararnab.contacts.R
import com.kararnab.contacts.callbacks.DebouncedOnClickListener
import com.kararnab.contacts.room.Contact
import com.kararnab.contacts.room.ContactViewModel
import com.kararnab.contacts.widgets.EmptyRecyclerView
import timber.log.Timber
import java.util.*

//TODO: unused
class ContactListFragment : Fragment() {
    interface Callbacks {
        fun navigateToContactDetail(contactId: String)
        fun navigateToAddContact(contactId: String)
    }

    private var callbacks : Callbacks? = null
    var mWordViewModel: ContactViewModel? = null
    private lateinit var mAddContactFab: FloatingActionButton
    private lateinit var rvContacts: EmptyRecyclerView
    private lateinit var mAdapter: ContactListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup any handles to view objects here
        mAddContactFab = view.findViewById(R.id.fab)
        rvContacts = view.findViewById(R.id.rvContacts)
        rvContacts.setEmptyView(view.findViewById(R.id.emptyContacts))
        mAddContactFab.setOnClickListener(object : DebouncedOnClickListener(500) {
            override fun onDebouncedClick(view: View) {

                /*checkCallPermission(new PermissionsCallback() {
                    @Override
                    public void onGranted() {
                        callPhoneNumber("9876543210",true);
                    }
                    @Override
                    public void onRejected() {
                        requestCallPermission();
                    }
                });*/
                Timber.tag("Add ContactPOJO").e("Manufactured by: %s\n User: %s", Build.MANUFACTURER, Build.DEVICE)
                Snackbar.make(view, R.string.work_in_progress, Snackbar.LENGTH_LONG)
                        .setAction(R.string.action, null).show()
                callbacks?.navigateToAddContact(UUID.randomUUID().toString())
            }
        })

        mAdapter = ContactListAdapter(object : ContactListener {
            override fun onItemClicked(contact: Contact) {
                callbacks?.navigateToContactDetail(contact.id)
            }

            override fun onItemLongClicked(position: Int, view: View): Boolean {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                mAdapter.getContact(position).isSelected = true
                mAdapter.notifyItemChanged(position)
                return true
            }
        })
        rvContacts.adapter = mAdapter
        rvContacts.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {
        @JvmStatic
        fun getInstance(): ContactListFragment {
            return ContactListFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mWordViewModel = ViewModelProvider(this).get(ContactViewModel::class.java) // Usually what we want: Passing Fragment's view as LifecycleOwner
        mWordViewModel!!.allContacts.observe(viewLifecycleOwner, Observer {
            mAdapter.setContacts(it);
        })
    }
}