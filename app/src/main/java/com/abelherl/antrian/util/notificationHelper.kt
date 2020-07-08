package com.abelherl.antrian.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.abelherl.antrian.R
import com.abelherl.antrian.util.notificationObject.CHANNEL_ID
import com.abelherl.antrian.util.notificationObject.FCM_API
import com.abelherl.antrian.util.notificationObject.NOTIFICATION_ID
import com.abelherl.antrian.util.notificationObject.apikeyy
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

object notificationObject{
    val NOTIFICATION_ID = 1
    val CHANNEL_ID = "1"
    val FCM_API = "https://fcm.googleapis.com/fcm/send"
    val apikeyy = "AAAAwV83tD4:APA91bF7OEOcIu119Rj8wFgcL--Wk2OjlKz1P9A1Yt_t57jtFqFd3-de3vcanPaXp0eALULuO4XfQmGyUPZQCDdr0dw5omJuiEJvFBsdy3tSxLo2-Qjy6JpOoYiT7ikq_ZWgKMORbJN5"
}

fun notificationHelper(context: Context, title:String, text: String) {
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(text)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    val notificationManager = NotificationManagerCompat.from(context)


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, "channel_one", importance)
        notificationManager.createNotificationChannel(channel)
    }

    notificationManager.notify(NOTIFICATION_ID, builder.build())
}

fun sendNotificationByTopic(context: Context,topic: String,text: String, title: String){
    var topicaddress = "/topics/"+topic
    var notification = JSONObject()
    var notifBody = JSONObject()

    try {
        notifBody.put("title", title)
        notifBody.put("message", text)

        notification.put("to", topicaddress)
        notification.put("data",notifBody)
    }catch (e: JSONException){
        Log.e("ERRORJSON", e.message)
    }
    sendNotif(context,notification)
}

fun sendNotif(context: Context,notification: JSONObject) {
    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context)
    }
    val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener<JSONObject> { response ->
                Log.d("SUCCESS","Request Success")
            },
            Response.ErrorListener {
                Toast.makeText(context, "Request Error", Toast.LENGTH_SHORT).show()
            }
        ){
        override fun getHeaders(): Map<String, String> {
            val params = HashMap<String, String>()
            params["Authorization"] = "key=" + apikeyy
            params["Content-Type"] = "application/json"
            return params
        }
    }
    requestQueue.add(jsonObjectRequest)
}
