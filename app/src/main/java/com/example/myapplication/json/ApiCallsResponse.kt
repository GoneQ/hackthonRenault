package com.example.myapplication.json

import com.google.gson.annotations.SerializedName

data class AgentLastSituationResponse (
    @SerializedName("vehicle_type")
    var vehicle_type: String,
    @SerializedName("position")
    var position: CoordinateJson,
    @SerializedName("total_cost")
    var total_cost: Float
)