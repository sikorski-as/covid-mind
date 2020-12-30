package com.example.covidmind.ui.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.covidmind.R
import com.example.covidmind.alarms.ReminderNotificationAlarmReceiver
import com.example.covidmind.notifications.*
import java.util.*


class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        val rootView = inflater.inflate(R.layout.fragment_settings, container, false)
        return rootView
    }

    fun enableNotifications(notificationTime: Calendar) {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, ReminderNotificationAlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(activity, REMINDER_NOTIFICATION_ID, intent, 0)

        val startUpTime = notificationTime.timeInMillis
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            startUpTime,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun disableNotifications() {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, ReminderNotificationAlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(activity, REMINDER_NOTIFICATION_ID, intent, 0)

        alarmManager.cancel(pendingIntent)
    }
}