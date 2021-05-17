package com.kararnab.contacts.v2.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val title: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("companyName")
    val companyName: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("notes")
    val notes: String,
): Parcelable