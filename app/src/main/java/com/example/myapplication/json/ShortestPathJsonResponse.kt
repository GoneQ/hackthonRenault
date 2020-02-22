package com.example.myapplication.json

import com.google.gson.annotations.SerializedName

data class ShortestPathJsonResponse(
    @SerializedName("cars")
    val cars:List<ShortestPathJson>
)

data class ShortestPathJson(
    @SerializedName("id")
    val id: String,
    @SerializedName("paths")
    val paths: List<List<Float>>,
    @SerializedName("costs")
    val costs: List<Float>,
    @SerializedName("path_length")
    val path_length: Float
)