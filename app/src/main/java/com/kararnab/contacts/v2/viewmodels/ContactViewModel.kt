package com.kararnab.contacts.v2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kararnab.contacts.v2.data.Repository
import com.kararnab.contacts.v2.data.database.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */
@HiltViewModel
class ContactViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    val allContacts = repository.local.readAllContacts().asLiveData();

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

    fun delete(contact: Contact, callback: () -> Unit) {
        viewModelScope.launch {
            repository.local.deleteContact(contact)
            callback()
        }
    }
}