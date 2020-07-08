package com.abelherl.antrian.util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessaging(): FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val notification = remoteMessage.data
//
            val title = notification?.get("title")
            val body = notification?.get("message")

            Log.d("TAG", "Message data payload: " + remoteMessage.data)


            notificationHelper(this, title!!, body!!)
        }

        if (remoteMessage.notification != null) {
            val notification = remoteMessage.notification!!
    
            val title = notification.title
            val body = notification.body
    
            Log.d("TAG", "Message title: " + notification.title)
            Log.d("TAG", "Message body: " + notification.body)

            notificationHelper(this, title!!, body!!)
        }
    }

}