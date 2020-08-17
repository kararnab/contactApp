package com.kararnab.contacts.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    //There is no convenience annotation for deleting multiple entities, so annotate the method with the generic @Query
    @Query("SELECT * from contact_table WHERE id= :id LIMIT 1")
    fun getContact(id: Int) : List<Contact>

    //There is no convenience annotation for deleting multiple entities, so annotate the method with the generic @Query
    @Query("delete from contact_table")
    suspend fun deleteAll()

    @Query("SELECT * from contact_table ORDER BY name ASC")
    fun getAlphabetizedWords(): LiveData<List<Contact>>

    @Query("SELECT * from contact_table where name like :s1+'%' ORDER BY name ASC")
    fun filterWords(s1: String?): LiveData<List<Contact>>
}