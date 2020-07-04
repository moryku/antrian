package com.abelherl.antrian

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "1"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNotif.setOnClickListener {
            pushNotification()
        }

    }

    private fun pushNotification() {

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Test")
            .setContentText("Uvuiaujsafhksjfhqk fhaha hakjdhkajdka")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(this)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "channel_one", importance)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())

    }

}
