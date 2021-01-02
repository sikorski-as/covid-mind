package com.example.covidmind.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.covidmind.notifications.REMINDER_NOTIFICATION_ID
import java.util.*

fun Context.enableReminderNotificationsAlarmAt(notificationTime: Calendar) {
    val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, ReminderNotificationAlarmReceiver::class.java)
    val pendingIntent =
        PendingIntent.getBroadcast(this, REMINDER_NOTIFICATION_ID, intent, 0)

    val startUpTime = notificationTime.timeInMillis
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        startUpTime,
        AlarmManager.INTERVAL_DAY,
        pendingIntent
    )
}

fun Context.disableReminderNotificationsAlarm() {
    val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, ReminderNotificationAlarmReceiver::class.java)
    val pendingIntent =
        PendingIntent.getBroadcast(
            this,
            REMINDER_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_NO_CREATE
        )

    alarmManager.cancel(pendingIntent)
}