package com.kararnab.contacts.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * A basic class representing an entity that is a row in a one-column database table.
 *
 * @ Entity - You must annotate the class as an entity and supply a table name if not class name.
 * @ PrimaryKey - You must identify the primary key.
 * @ ColumnInfo - You must supply the column name if it is different from the variable name.
 *
 * See the documentation for the full rich set of annotations.
 * https://developer.android.com/topic/libraries/architecture/room.html
 */

@Entity(tableName = "contact_table")
data class Contact(@PrimaryKey var id: String, @ColumnInfo(name = "phone")var phone: String, @ColumnInfo(name = "name") var name: String, var company: String, var emailId: String, var notes: String) {
    /*@PrimaryKey(autoGenerate = true)
    @NonNull
    var id:Int = 0*/

    @Ignore
    var isSelected: Boolean = false
}