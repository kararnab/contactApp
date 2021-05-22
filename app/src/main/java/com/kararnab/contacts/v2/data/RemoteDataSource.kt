package com.kararnab.contacts.v2.data

import com.kararnab.contacts.v2.data.network.ContactSyncApi
import com.kararnab.contacts.v2.models.Contacts
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: ContactSyncApi
) {

    suspend fun readAllContacts(queries: Map<String, String>): Response<Contacts> {
        return foodRecipesApi.getContacts(queries)
    }

    suspend fun backupContactsToCloud(queries: Map<String, String>): Response<Contacts> {
        return foodRecipesApi.backupContacts(queries)
    }

}