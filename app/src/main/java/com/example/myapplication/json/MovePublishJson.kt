package com.example.myapplication.json

import com.google.gson.annotations.SerializedName

data class MovePublishJson(
    @SerializedName("vehicle_type")
    val vehicle_type: String,
    @SerializedName("target")
    val target: CoordinateJson
)