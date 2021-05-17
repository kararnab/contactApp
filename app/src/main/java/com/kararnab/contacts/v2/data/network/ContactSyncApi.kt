package com.kararnab.contacts.v2.data.network

import com.kararnab.contacts.v2.models.Contacts
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface ContactSyncApi {

    @GET("/v1/contacts")
    suspend fun getContacts(
        @QueryMap queries: Map<String, String>
    ): Response<Contacts>

    @POST("/v1/backupContacts")
    suspend fun backupContacts(
        @QueryMap searchQuery: Map<String, String>
    ): Response<Contacts>

}