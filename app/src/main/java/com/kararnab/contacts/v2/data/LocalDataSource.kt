package com.kararnab.contacts.v2.data

import androidx.lifecycle.LiveData
import com.kararnab.contacts.v2.data.database.Contact
import com.kararnab.contacts.v2.data.database.ContactDao
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val contactDao: ContactDao
) {

    fun readAllContacts(): LiveData<List<Contact>> {
        return contactDao.getAlphabetizedWords()
    }

    suspend fun deleteContact(contact: Contact) {
        contactDao.delete(contact)
    }

    suspend fun deleteAllContacts() {
        contactDao.deleteAll()
    }

    suspend fun insertOrUpdateContact(contact: Contact) {
        contactDao.insert(contact)
    }

    fun insertOrUpdateContact(searchText: String): LiveData<List<Contact>> {
        return contactDao.filterWords(searchText)
    }

    fun getContact(id: Int, fallbackContact: Contact) : Contact? {
        val contacts = contactDao.getContact(id)
        return if(contacts.isNotEmpty()) {
            contacts[0]
        }else{
            null
        }
    }

}