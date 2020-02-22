package com.example.myapplication.json

import com.google.gson.annotations.SerializedName

data class CoordinateJson(
    @SerializedName("x")
    val x: Float,
    @SerializedName("y")
    val y: Float
)