package com.kararnab.contacts.v2.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.*

@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class ContactRoomDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: ContactRoomDatabase? = null

        fun getInstance(context: Application): ContactRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, ContactRoomDatabase::class.java, "contact_database")
                        .fallbackToDestructiveMigration() // Wipes and rebuilds instead of migrating if no Migration object.
                        .addCallback(sRoomDatabaseCallback)
                        .build()
                INSTANCE = instance
                return instance
            }
        }

        /**
         * Override the onOpen method to populate the database.
         * For this sample, we clear the database every time it is created or opened.
         *
         * If you want to populate the database only when the database is created for the 1st time,
         * override RoomDatabase.Callback()#onCreate
         */
        private val sRoomDatabaseCallback: Callback = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                /*INSTANCE?.let {
                    GlobalScope.launch {
                        populateDbAsync(it.contactDao())
                    }
                }*/
            }
        }

        /**
         * Populate the database in the background.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDbAsync(mDao: ContactDao) {
            mDao.deleteAll()
            val contact = Contact(UUID.randomUUID().toString(), "9876543210", "Arnab", "RoomDb", "arnab@gmail.com", "")
            mDao.insert(contact)
        }
    }
}