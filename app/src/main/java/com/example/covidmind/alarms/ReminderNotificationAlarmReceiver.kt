package com.example.covidmind.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.covidmind.R
import com.example.covidmind.notifications.REMINDER_NOTIFICATION_CHANNEL_ID
import com.example.covidmind.notifications.REMINDER_NOTIFICATION_ID


class ReminderNotificationAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notification = NotificationCompat.Builder(context!!,
            REMINDER_NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle(context.resources?.getString(R.string.notification_reminder_title))
            .setContentText(context.resources?.getString(R.string.notification_reminder_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true).build()

        with(NotificationManagerCompat.from(context)) {
            notify(REMINDER_NOTIFICATION_ID, notification)
        }
    }
}