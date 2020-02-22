package com.example.myapplication.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.json.MovePublishJson
import com.example.myapplication.json.ShortestPathJsonResponse
import com.example.myapplication.recyclerView.RecyclerViewAdapter
import com.example.myapplication.requests.GetShortestPaths
import com.google.gson.Gson
import kotlinx.android.synthetic.main.settings.*


class SettingsFragment : Fragment() {
    companion object {
        fun newInstance() = SettingsFragment()
        val TAG = SettingsFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.SettingLetsGoButton.setOnClickListener {

        }
        transportationText.visibility = View.INVISIBLE
        detailsText.visibility = View.INVISIBLE
        timeText.visibility = View.INVISIBLE

        val array = arrayOf(context!!.getColor(R.color.colorPrimary), context!!.getColor(R.color.colorDark), context!!.getColor(R.color.red), context!!.getColor(R.color.green))
        val shortestPaths = GetShortestPaths()
        this.walk.setOnClickListener {
            this.transportation.setText("Walk")
            MainActivity.selectedTransportation = 0
            this.clearFilter()
            this.walk.setColorFilter(context!!.getColor(R.color.colorPrimary))
            this.walk.borderColor = array.random()
            shortestPaths.getWalkShortestPathLength { response -> this.setValues(response)}
        }

        this.bike.setOnClickListener {
            this.transportation.setText("Bike")
            MainActivity.selectedTransportation = 1
            this.clearFilter()
            this.bike.setColorFilter(context!!.getColor(R.color.colorPrimary))
            this.bike.borderColor = array.random()
            shortestPaths.getBikeShortestPathLength { response -> this.setValues(response)}
        }
        this.subway.setOnClickListener {
            this.transportation.setText("Subway")
            MainActivity.selectedTransportation = 2
            this.clearFilter()
            this.subway.setColorFilter(context!!.getColor(R.color.colorPrimary))
            this.subway.borderColor = array.random()
            shortestPaths.getSubwayShortestPathLength { response -> this.setValues(response)}
        }
        this.taxi.setOnClickListener {
            this.transportation.setText("Taxi")
            MainActivity.selectedTransportation = 3
            this.clearFilter()
            this.taxi.setColorFilter(context!!.getColor(R.color.colorPrimary))
            this.taxi.borderColor = array.random()
            shortestPaths.getBikeShortestPathLength { response -> this.setValues(response)}
        }

        this.SettingLetsGoButton.setOnClickListener {
            if (MainActivity.enableButton && MainActivity.currentIndex < MainActivity.destinations.count()) {
                val vehicle = when (MainActivity.selectedTransportation) {
                    0 -> {
                        shortestPaths.getWalkShortestPathLength { response ->
                            this.getCheckpoints(
                                response
                            )
                        }
                        "walk"
                    }
                    1 -> {
                        shortestPaths.getBikeShortestPathLength { response ->
                            this.getCheckpoints(
                                response
                            )
                        }
                        "bike"
                    }
                    2 -> "subway"
                    3 -> "taxi"
                    else -> ""
                }


                if (vehicle == "") {
                    AlertDialog.Builder(context!!)
                        .setMessage("You must select a transportation first")
                        .setNegativeButton(
                            android.R.string.ok,
                            DialogInterface.OnClickListener { dialog, which ->
                                dialog.cancel()
                            })
                        .show()
                }
                val json = MovePublishJson(vehicle, MainActivity.destination)
                Log.d(TAG, Gson().toJson(json))
                MainActivity.mqtt.publish(
                    "${MainActivity.environment}/prod/user/path",
                    Gson().toJson(json)
                )
            }

        }

    }

    private fun setValues(response: ShortestPathJsonResponse) {

        time.setText(response.cars.firstOrNull()?.path_length.toString())
        details.setText(response.cars.firstOrNull()?.paths.toString())
    }

    private fun getCheckpoints(response: ShortestPathJsonResponse) {
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView?.adapter = RecyclerViewAdapter(context!!)
        MainActivity.recyclerView = this.recyclerView
    }

    private fun clearFilter() {
        this.walk.clearColorFilter()
        this.bike.clearColorFilter()
        this.subway.clearColorFilter()
        this.taxi.clearColorFilter()

        transportationText.visibility = View.VISIBLE
        detailsText.visibility = View.VISIBLE
        timeText.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.activity as? AppCompatActivity)?.supportActionBar?.let {
            it.show()
            it.title = Html.fromHtml("<font color=\"#FBC81D\">" + getString(R.string.app_name) + "</font>")
        }
    }
}