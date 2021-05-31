package com.kararnab.contacts.v2.data.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.kararnab.contacts.v2.util.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = Constants.PHONE_TABLE,
    foreignKeys = [ForeignKey(
        entity = Contact::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
    )
data class Phone(
    @PrimaryKey(autoGenerate = true) var id: Long,
    val userId: Long,
    val phoneNumber: String,
    val phoneType: String
) : Parcelable
