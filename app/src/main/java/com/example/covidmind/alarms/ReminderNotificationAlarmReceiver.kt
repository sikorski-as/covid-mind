package com.example.covidmind.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.covidmind.notifications.sendReminderNotification


class ReminderNotificationAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        sendReminderNotification(context!!)
    }
}