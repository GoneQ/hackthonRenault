package com.example.myapplication.json

import com.google.gson.annotations.SerializedName

data class AgentStatusJson(
    @SerializedName("status")
    val status: String,
    @SerializedName("situation")
    val situation: AgentLastSituationResponse
)