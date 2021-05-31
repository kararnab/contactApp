package com.kararnab.contacts.v2.data.database.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kararnab.contacts.v2.util.Constants.Companion.CONTACT_TABLE

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

@Parcelize
@Entity(
    tableName = CONTACT_TABLE
)
data class Contact(@PrimaryKey var id: Long, @ColumnInfo(name = "name") var name: String = "", var company: String = "", var emailId: String = "", var notes: String = "") :
    Parcelable