package com.kararnab.contacts.room

import android.app.Application
import androidx.lifecycle.LiveData

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
class ContactRepository(application: Application) {
    private val mContactDao: ContactDao

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allContacts: LiveData<List<Contact>>

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    suspend fun insert(contact: Contact) {
        mContactDao.insert(contact)
    }

    suspend fun update(contact: Contact) {
        mContactDao.update(contact)
    }

    fun getContact(id: Int, fallbackContact: Contact) : Contact {
        val contacts = mContactDao.getContact(id)
        return if(contacts.isNotEmpty()) {
            contacts[0]
        }else{
            fallbackContact
        }
    }

    /*private class insertAsyncTask internal constructor(private val mAsyncTaskDao: ContactDao) : AsyncTask<Contact?, Void?, Void?>() {
        protected override fun doInBackground(vararg params: Contact): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }*/

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    init {
        val db = ContactRoomDatabase.getInstance(application)
        mContactDao = db.contactDao()
        allContacts = mContactDao.getAlphabetizedWords()
    }
}