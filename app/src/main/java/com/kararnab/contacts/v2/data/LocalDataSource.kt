package com.kararnab.contacts.v2.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.kararnab.contacts.v2.data.database.ContactDao
import com.kararnab.contacts.v2.data.database.entities.PhoneContact
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val contactDao: ContactDao
) {

    fun readAllContacts(): Flow<List<PhoneContact>> {
        return contactDao.getAlphabetizedWords()
    }

    suspend fun deleteContact(contact: PhoneContact) {
        contactDao.delete(contact.user)
    }

    suspend fun deleteAllContacts() {
        contactDao.deleteAll()
    }

    suspend fun insertOrUpdateContact(contact: PhoneContact) {
        contactDao.insertPhoneContact(contact)
    }

    fun searchContacts(searchText: String): LiveData<List<PhoneContact>> {
        return contactDao.filterWords(searchText)
    }

}