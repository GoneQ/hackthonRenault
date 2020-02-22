package com.example.myapplication

import com.example.myapplication.json.CoordinateJson

data class Checkpoint(
    var checked: Boolean,
    val coordinate: CoordinateJson
)