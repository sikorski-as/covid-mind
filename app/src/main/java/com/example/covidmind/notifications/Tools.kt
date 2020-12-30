package com.example.covidmind.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

fun createNotificationChannel(
    context: Context,
    id: String,
    name: String,
    description: String
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // required since Android Oreo
        val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = description
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}