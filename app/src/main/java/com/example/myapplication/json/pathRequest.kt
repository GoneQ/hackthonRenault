package com.example.myapplication.json

import com.google.gson.annotations.SerializedName

data class pathRequest (
    @SerializedName("departure")
    val departure: String,

    @SerializedName("arrival")
    val arrival: String
)