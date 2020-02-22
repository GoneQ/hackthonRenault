package com.example.myapplication.mqtt

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import com.example.myapplication.Checkpoint
import com.example.myapplication.MainActivity
import com.example.myapplication.json.AgentLastSituationResponse
import com.example.myapplication.json.AgentStatusJson
import com.example.myapplication.json.CoordinateJson
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class MqttEvents (
    val data: String
) {

    private val epsilon = 0.04
    var missionDialog: AlertDialog? = null

    companion object {
        val TAG = MqttEvents::class.java.simpleName
    }
    fun missionReceived(context: Context) {
        Log.d(TAG, "missionReceived $data")
        val json = Gson().fromJson(data, MissionJson::class.java)
        MainActivity.currentIndex = 0
        MainActivity.destinations = json.positions
        MainActivity.path = json.positions.map { Checkpoint(false, it) }
        missionDialog = AlertDialog.Builder(context).setTitle("Mission Received !")
            .setMessage("Go to ${MainActivity.destinations.map { "${it.x}, ${it.y}" }}")
            .setPositiveButton(android.R.string.ok) { dialog, which -> missionDialog?.dismiss() }
            .create()
        MainActivity.dialog?.dismiss()
        missionDialog?.show()
    }


    fun destinationReached(context: Context) {
        Log.d(TAG, "destination reached $data")

        if (!MainActivity.destinationDialogShown) {
             AlertDialog.Builder(context)
                .setMessage("Checkpoint reached")
                .setPositiveButton(
                    android.R.string.ok,
                    DialogInterface.OnClickListener { dialog, which ->
                        missionDialog?.dismiss()
                        MainActivity.destinationDialogShown = false
                    })
                .create()
                .show()
            if (MainActivity.currentIndex < MainActivity.destinations.count()) {
                MainActivity.currentIndex++
            }
            MainActivity.destinationDialogShown = true
        }
    }

    fun status(context: Context) {
        Log.d(TAG, "status $data")
        val json = Gson().fromJson(data, AgentStatusJson::class.java)
        when(json.status.toLowerCase()) {
            "moving" -> MainActivity.enableButton = false
            "stopping" -> MainActivity.enableButton = true
            "stopped" -> {
                MainActivity.enableButton = true
                val dstPos = MainActivity.currentPosition
                val srcPos = MainActivity.destination
                val dist = (dstPos.x - srcPos.x) * (dstPos.x - srcPos.x) + (dstPos.y - srcPos.y) * (dstPos.y - srcPos.y)
                if (dist > epsilon) {
                    Toast.makeText(
                        context,
                        "An error occurred, please select another way",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun situation(context: Context) {
        Log.d(TAG, "situation $data")
        val json = Gson().fromJson(data, AgentLastSituationResponse::class.java)
        val dstPos = json.position
        val srcPos = MainActivity.destination
        val dist = (dstPos.x - srcPos.x) * (dstPos.x - srcPos.x) + (dstPos.y - srcPos.y) * (dstPos.y - srcPos.y)
        if (dist < epsilon) {
            destinationReached(context)
            MainActivity.path.getOrNull(MainActivity.index++)?.checked = true
            MainActivity.recyclerView?.adapter?.notifyDataSetChanged()
            Log.e(TAG, "Checking checkpoint")
        }
        //Log.e(TAG, dist.toString())
        MainActivity.currentPosition = srcPos
    }

    fun changeWeather() {
        Log.d(TAG, "change weather $data")

    }
}

data class MissionJson(
    @SerializedName("mission")
    val mission: String,
    @SerializedName("positions")
    val positions: List<CoordinateJson>
)