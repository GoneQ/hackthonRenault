package com.example.myapplication.mqtt

import android.content.Context
import android.util.Log
import com.example.myapplication.MainActivity
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MqttConnectionClass {
    private val clientId = MqttClient.generateClientId()
    //private val username = "team05"
    private val username = "team-prod"
    private val password ="matrix".toCharArray()
    //private val password = "pr58msaqyl".toCharArray()
    private val serverUrl = "tcp://mr1dns3dpz5mjj.messaging.solace.cloud:1883"

    private lateinit var mqttAndroidClient: MqttAndroidClient

    fun connect(context : Context, callback: () -> Unit) {
        val options = MqttConnectOptions()
        mqttAndroidClient = MqttAndroidClient ( context.applicationContext, serverUrl, clientId)
        options.password = password
        options.userName = username
        Log.e("MqttConnect", "trying to connect to ${serverUrl}")
        try {
            val token = mqttAndroidClient.connect(options)
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken)                        {
                    Log.e("MqttConnection", "success ")
                    // Give your callback on connection established here
                    callback.invoke()
                    MainActivity.dialog?.setMessage("Waiting for mission ...")
                }
                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.e("MqttConnection", "failure")
                    // Give your callback on connection failure here
                    exception.printStackTrace()
                }
            }
        } catch (e: MqttException) {
            // Give your callback on connection failure here
            e.printStackTrace()
        }
    }

    fun subscribe(topic: String, callback: () -> Unit) {
        val qos = 2 // Mention your qos value
        try {
            mqttAndroidClient.subscribe(topic, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // Give your callback on Subscription here
                    callback.invoke()
                }
                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    // Give your subscription failure callback here
                }
            })
        } catch (e: MqttException) {
            // Give your subscription failure callback here
        }
    }

    fun unSubscribe(topic: String) {
        try {
            val unsubToken = mqttAndroidClient.unsubscribe(topic)
            unsubToken.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // Give your callback on unsubscribing here
                }
                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    // Give your callback on failure here
                }
            }
        } catch (e: MqttException) {
            // Give your callback on failure here
        }
    }

    fun receiveMessages(callback: (String, String) -> Unit) {
        mqttAndroidClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable) {
                //connectionStatus = false
                // Give your callback on failure here
            }
            override fun messageArrived(topic: String, message: MqttMessage) {
                try {
                    val data = String(message.payload, charset("UTF-8"))
                    // data is the desired received message
                    // Give your callback on message received here
                    callback.invoke(topic, data)
                } catch (e: Exception) {
                    // Give your callback on error here
                }
            }
            override fun deliveryComplete(token: IMqttDeliveryToken) {
                // Acknowledgement on delivery complete
            }
        })
    }

    fun publish(topic: String, data: String) {
        val encodedPayload : ByteArray
        try {
            encodedPayload = data.toByteArray(charset("UTF-8"))
            val message = MqttMessage(encodedPayload)
            message.qos = 2
            message.isRetained = false
            mqttAndroidClient.publish(topic, message)
        } catch (e: Exception) {
            // Give Callback on error here
            Log.e("Mqtt", e.toString())
        } catch (e: MqttException) {
            // Give Callback on error here
            Log.e("Mqtt", e?.message)
        }
    }

    fun disconnect() {
        try {
            val disconToken = mqttAndroidClient.disconnect()
            disconToken.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    //connectionStatus = false
                    // Give Callback on disconnection here
                }
                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    // Give Callback on error here
                }
            }
        } catch (e: MqttException) {
            // Give Callback on error here
        }
    }
}