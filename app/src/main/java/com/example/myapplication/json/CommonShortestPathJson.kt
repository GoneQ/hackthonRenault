package com.example.myapplication.json

import com.google.gson.annotations.SerializedName

data class CommonShortestPathJson(
    @SerializedName("arrival")
    val arrival: CoordinateJson,
    @SerializedName("departure")
    val departure: CoordinateJson
)