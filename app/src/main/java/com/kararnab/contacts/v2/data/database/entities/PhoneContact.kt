package com.kararnab.contacts.v2.data.database.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhoneContact(
    @Embedded val user: Contact,
    @Relation(
          parentColumn = "id",
          entityColumn = "userId"
    )
    val phone: List<Phone>
): Parcelable