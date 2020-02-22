package com.example.myapplication.ui.login

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.mqtt.MqttConnectionClass
import com.example.myapplication.mqtt.MqttEvents
import com.example.myapplication.ui.settings.SettingsFragment
import kotlinx.android.synthetic.main.login.*

class LoginFragment : Fragment() {

    companion object {
        val TAG = LoginFragment::class.simpleName
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    private val mqtt: MqttConnectionClass
        get() {
            return MainActivity.mqtt
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UrlTextEdit.setText(MainActivity.environment)
        loginButton.setOnClickListener {
            val fragment = SettingsFragment.newInstance()
            this.activity?.supportFragmentManager?.beginTransaction()
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ?.add(fragment, "settings")
                ?.replace(R.id.loginFrame, fragment)
                ?.commit()
            MainActivity.environment = UrlTextEdit.text.toString()
            Log.d(TAG, "ButtonClicked")
            mqtt.connect(context!!) { connectionSucess() }
            MainActivity.dialog = AlertDialog.Builder(this.context!!).setTitle("").setMessage("Connecting ...").create()
            MainActivity.dialog?.setCancelable(false)
            MainActivity.dialog?.show()
        }
    }

    private fun connectionSucess() {
        val env = MainActivity.environment
        mqtt.subscribe("$env/prod/user/situation") {}
        mqtt.subscribe("$env/prod/user/status") {}
        mqtt.subscribe("$env/prod/user/objective-reached") {}
        mqtt.subscribe("$env/prod/context/change/weather") {}
        mqtt.subscribe("$env/prod/user/mission") { subCallback() }
    }

    private fun subCallback() {
        mqtt.receiveMessages { topic, data -> receiveMsgCallback(topic, data) }
    }

    private fun receiveMsgCallback(topic: String, data: String) {
        val env = MainActivity.environment
        val mqtt = MqttEvents(data)
        when (topic) {
            "$env/prod/user/situation" -> mqtt.situation(context!!)
            "$env/prod/user/status" -> mqtt.status(context!!)
            "$env/prod/user/objective-reached" -> mqtt.destinationReached(context!!)
            "$env/prod/user/mission" -> mqtt.missionReceived(context!!)
            "$env/prod/context/change/weather" -> mqtt.changeWeather()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
