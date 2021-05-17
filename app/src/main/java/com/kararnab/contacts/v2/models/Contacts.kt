package com.kararnab.contacts.v2.models

import com.google.gson.annotations.SerializedName

data class Contacts(
    @SerializedName("results")
    val results: List<Result>
)