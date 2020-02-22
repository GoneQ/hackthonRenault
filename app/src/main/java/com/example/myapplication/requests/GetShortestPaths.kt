package com.example.myapplication.requests

import android.util.Log
import com.example.myapplication.MainActivity
import com.example.myapplication.json.CommonShortestPathJson
import com.example.myapplication.json.CoordinateJson
import com.example.myapplication.json.ShortestPathJsonResponse
import com.example.myapplication.json.TaxiShortestPathJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GetBikeShortestPath {

    @POST("/api/graph/road_graph/shortest_path/bike")
    fun getBikeShortestPath(@Body shortestPathJson: CommonShortestPathJson) : Call<ShortestPathJsonResponse>
}

interface GetWalkShortestPath {

    @POST("/api/graph/road_graph/shortest_path/walk")
    fun getWalkShortestPath(@Body shortestPathJson: CommonShortestPathJson) : Call<ShortestPathJsonResponse>
}

interface GetSubwayShortestPath {

    @POST("/api/graph/road_graph/shortest_path/subway")
    fun getSubwayShortestPath(@Body shortestPathJson: CommonShortestPathJson) : Call<ShortestPathJsonResponse>
}

interface GetTaxiShortestPath {

    @POST("/api/graph/road_graph/shortest_path/car")
    fun getTaxiShortestPath(@Body shortestPathJson: TaxiShortestPathJson) : Call<ShortestPathJsonResponse>
}

class GetShortestPaths {

    companion object {
        private val TAG = GetShortestPaths::class.java.simpleName
    }
    private fun getCommonShortestPath(callback: (CommonShortestPathJson) -> Unit) {
        if (MainActivity.currentIndex >= MainActivity.destinations.count()) {
        }
        else {
            ApiCalls.getAgentLastSituation { response ->
                callback.invoke(
                    CommonShortestPathJson(
                        CoordinateJson(MainActivity.destination.x, MainActivity.destination.y),
                        CoordinateJson(response!!.position.x, response.position.y)
                    )
                )
            }
            Log.d(
                TAG,
                "Called getCommonShortestPath with ${MainActivity.destination.x}, ${MainActivity.destination.y}"
            )
        }
    }

    fun getBikeShortestPathLength(callback: (ShortestPathJsonResponse) -> Unit) {
        val request = RetrofitClientInterface.instance().create(GetBikeShortestPath::class.java)
        getCommonShortestPath { json ->
            val call = request.getBikeShortestPath(json)

            call.enqueue(object : Callback<ShortestPathJsonResponse> {

                override fun onResponse(
                    call: Call<ShortestPathJsonResponse>,
                    response: Response<ShortestPathJsonResponse>
                ) {
                    response.body()?.let { callback.invoke(it)}
                }

                override fun onFailure(call: Call<ShortestPathJsonResponse>, t: Throwable) {
                    throw t
                }
            })
        }
    }

    fun getWalkShortestPathLength(callback: (ShortestPathJsonResponse) -> Unit) {
        val request = RetrofitClientInterface.instance().create(GetWalkShortestPath::class.java)
        getCommonShortestPath { json ->
            val call = request.getWalkShortestPath(json)

            call.enqueue(object : Callback<ShortestPathJsonResponse> {

                override fun onResponse(
                    call: Call<ShortestPathJsonResponse>,
                    response: Response<ShortestPathJsonResponse>
                ) {
                    response.body()?.let { callback.invoke(it)}
                }

                override fun onFailure(call: Call<ShortestPathJsonResponse>, t: Throwable) {
                    throw t
                }
            })
        }
    }

    fun getSubwayShortestPathLength(callback: (ShortestPathJsonResponse) -> Unit) {
        val request = RetrofitClientInterface.instance().create(GetSubwayShortestPath::class.java)
        getCommonShortestPath { json ->
            val call = request.getSubwayShortestPath(json)

            call.enqueue(object : Callback<ShortestPathJsonResponse> {

                override fun onResponse(
                    call: Call<ShortestPathJsonResponse>,
                    response: Response<ShortestPathJsonResponse>
                ) {
                    callback.invoke(response.body()!!)
                }

                override fun onFailure(call: Call<ShortestPathJsonResponse>, t: Throwable) {
                    throw t
                }
            })
        }
    }

//    fun getTaxiShortestPathLength(time: TextView) {
//        val request = RetrofitClientInterface.instance().create(GetTaxiShortestPath::class.java)
//        val json = getCommonShortestPath()
//        val call = request.getTaxiShortestPath(json)
//
//        call.enqueue(object : Callback<ShortestPathJsonResponse> {
//
//            override fun onResponse(call: Call<ShortestPathJsonResponse>, response: Response<ShortestPathJsonResponse>) {
//                time.setText(response.body()?.cars?.firstOrNull()?.path_length.toString())
//            }
//
//            override fun onFailure(call: Call<ShortestPathJsonResponse>, t: Throwable) {
//                throw t
//            }
//        })
//    }
}