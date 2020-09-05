package com.kararnab.contacts.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */
class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository: ContactRepository

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allContacts: LiveData<List<Contact>>

    fun insert(contact: Contact) {
        viewModelScope.launch {
            mRepository.insert(contact)
        }
    }

    fun update(contact: Contact) {
        viewModelScope.launch {
            mRepository.update(contact)
        }
    }

    fun getContact(id: Int, fallbackContact: Contact): Contact {
        return mRepository.getContact(id, fallbackContact)
    }

    init {
        mRepository = ContactRepository(application)
        allContacts = mRepository.allContacts
    }
}