package com.example.covidmind.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.covidmind.MainActivity
import com.example.covidmind.R

// Reminder notification
const val REMINDER_NOTIFICATION_ID = 0
fun sendReminderNotification(context: Context) {
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, REMINDER_NOTIFICATION_ID, intent, 0)

    val notification = NotificationCompat.Builder(context, REMINDER_NOTIFICATION_CHANNEL_ID)
        .setContentTitle(context.resources?.getString(R.string.notification_reminder_title))
        .setContentText(context.resources?.getString(R.string.notification_reminder_text))
        .setContentIntent(pendingIntent)
        .setStyle(NotificationCompat.BigTextStyle())
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setSmallIcon(R.drawable.ic_brain)
        .setAutoCancel(true).build()

    createReminderNotificationChannel(context)

    with(NotificationManagerCompat.from(context)) {
        notify(REMINDER_NOTIFICATION_ID, notification)
    }
}

