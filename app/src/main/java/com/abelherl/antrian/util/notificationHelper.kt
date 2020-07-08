package com.abelherl.antrian.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.abelherl.antrian.R
import com.abelherl.antrian.util.notificationObject.CHANNEL_ID
import com.abelherl.antrian.util.notificationObject.NOTIFICATION_ID

object notificationObject{
    val NOTIFICATION_ID = 1
    val CHANNEL_ID = "1"
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