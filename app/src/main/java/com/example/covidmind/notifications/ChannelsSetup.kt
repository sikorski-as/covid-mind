package com.example.covidmind.notifications

import android.content.Context

// General notifications channel
const val REMINDER_NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL"
const val REMINDER_NOTIFICATION_CHANNEL_NAME = "General notifications channel"
const val REMINDER_NOTIFICATION_CHANNEL_DESCRIPTION = "Channel for general notifications"
fun createReminderNotificationChannel(context: Context) =
    createNotificationChannel(
        context,
        REMINDER_NOTIFICATION_CHANNEL_ID,
        REMINDER_NOTIFICATION_CHANNEL_NAME,
        REMINDER_NOTIFICATION_CHANNEL_DESCRIPTION
    )

