package com.kararnab.contacts.v2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.kararnab.contacts.v2.data.database.Contact
import com.kararnab.contacts.v2.data.database.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */
@HiltViewModel
class ContactViewModel @Inject constructor(
    private val repository: ContactRepository,
    application: Application
) : AndroidViewModel(application) {

    fun insert(contact: Contact) {
        viewModelScope.launch {
            repository.local.insertOrUpdateContact(contact)
        }
    }

    fun update(contact: Contact) {
        viewModelScope.launch {
            repository.local.insertOrUpdateContact(contact)
        }
    }

    fun getAllContacts(): LiveData<List<Contact>> {
        return repository.local.readAllContacts()
    }
}