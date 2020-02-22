package com.example.myapplication.json

import com.google.gson.annotations.SerializedName

data class TaxiShortestPathJson(

    @SerializedName("departure")
    var departure: CoordinateJson,
    @SerializedName("arrival")
    var arrival: CoordinateJson,
    @SerializedName("vehicules")
    var vehicules: List<VehiculeJson>
)

data class VehiculeJson(
    @SerializedName("id")
    var id: String,
    @SerializedName("x")
    var x: Float,
    @SerializedName("y")
    var y: Float
)