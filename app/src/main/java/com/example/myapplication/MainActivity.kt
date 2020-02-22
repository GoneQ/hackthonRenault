package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.json.CoordinateJson
import com.example.myapplication.mqtt.MqttConnectionClass
import com.example.myapplication.ui.login.LoginFragment
import com.example.myapplication.ui.webView.WebViewFragment
import kotlinx.android.synthetic.main.main.*



class MainActivity : AppCompatActivity() {

    companion object {
        var environment = ""
        val url: String
            get() {
                return "http://$environment.xp65.renault-digital.com"
            }
        var selectedPath = -1
        var selectedTransportation = -1
        var dialog: AlertDialog? = null
        var mqtt = MqttConnectionClass()
        var path: List<Checkpoint> = emptyList()
        var index = 0
        var enableButton = true
        var currentPosition: CoordinateJson = CoordinateJson(0f, 0f)
        var destinationDialogShown = false
        var destinations: List<CoordinateJson> = emptyList()
        var currentIndex = 0
        val destination: CoordinateJson
            get() {
                return destinations.get(currentIndex)
            }

        var recyclerView: RecyclerView? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportActionBar?.hide()
        supportActionBar?.elevation = 0f

        this.title = "CityTrip"
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }

    private var draw = false
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (selectedPath == -1) {
            nvView.setCheckedItem(R.id.fastest)
            selectedPath = 0
        }
        if (item.itemId == R.id.settings_button) {
            if (draw) {
                drawer_layout.closeDrawer(GravityCompat.END)
                draw = false
            } else {
                drawer_layout.openDrawer(GravityCompat.END)
                draw = true
            }
            this.setupMenu()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupMenu() {
        nvView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.fastest -> {
                    selectedPath = 0
                    it.isChecked = true
                    true
                }
                R.id.comfort -> {
                    selectedPath = 1
                    it.isChecked = true
                    true
                }
                R.id.pleasant -> {
                    selectedPath = 2
                    it.isChecked = true
                    true
                }
                R.id.showWebView -> {
                    val fragment = WebViewFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .add(fragment, "webView")
                        .replace(R.id.settingsFrameLayout, fragment)
                        .addToBackStack(null)
                        .commit()
                    drawer_layout.closeDrawer(GravityCompat.END)
                    true
                }
                else -> false
            }
        }

    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
            supportActionBar?.show()
        }
    }
}
