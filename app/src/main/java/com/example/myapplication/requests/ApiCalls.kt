package com.example.myapplication.requests

import android.util.Log
import com.example.myapplication.json.AgentLastSituationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET

interface AgentLastSituation {
    @GET("/api/agent/api/user/situation/last")
    fun getAgentLastSituation(): Call<AgentLastSituationResponse>
}

class ApiCalls {
    companion object {
        fun getAgentLastSituation(getAgentLastSituationReponseHandler: (AgentLastSituationResponse?) -> Unit) {
            val request = RetrofitClientInterface.instance().create(AgentLastSituation::class.java)
            val call = request.getAgentLastSituation()

            val response = call.enqueue(object : Callback<AgentLastSituationResponse> {

                override fun onResponse(
                    call: Call<AgentLastSituationResponse>,
                    response: Response<AgentLastSituationResponse>
                ) {
                    getAgentLastSituationReponseHandler.invoke(response.body())
                    Log.d("ApiCalls", response.body().toString())
                }

                override fun onFailure(call: Call<AgentLastSituationResponse>, t: Throwable) {
                    throw t
                }
            })
        }
    }
}