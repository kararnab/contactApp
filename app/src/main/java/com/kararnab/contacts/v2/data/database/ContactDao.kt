package com.kararnab.contacts.v2.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kararnab.contacts.v2.util.Constants.Companion.CONTACT_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    //There is no convenience annotation for deleting multiple entities, so annotate the method with the generic @Query
    @Query("SELECT * from $CONTACT_TABLE WHERE id= :id LIMIT 1")
    fun getContact(id: Int) : List<Contact>

    //There is no convenience annotation for deleting multiple entities, so annotate the method with the generic @Query
    @Query("delete from $CONTACT_TABLE")
    suspend fun deleteAll()

    @Query("SELECT * from $CONTACT_TABLE ORDER BY name ASC")
    fun getAlphabetizedWords(): Flow<List<Contact>>

    @Query("SELECT * from $CONTACT_TABLE where name like :s1+'%' ORDER BY name ASC")
    fun filterWords(s1: String?): LiveData<List<Contact>>
}