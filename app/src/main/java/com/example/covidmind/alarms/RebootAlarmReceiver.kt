package com.example.covidmind.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.covidmind.repos.PreferencesRepository

class RebootAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            val preferences = PreferencesRepository(context!!)
            if(preferences.isReminderNotificationSet){
                context.enableReminderNotificationsAlarmAt(preferences.reminderNotificationTime)
            }
        }
    }
}