package com.example.covidmind.ui.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat as AndroidDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.covidmind.R
import com.example.covidmind.alarms.ReminderNotificationAlarmReceiver
import com.example.covidmind.alarms.disableReminderNotificationsAlarm
import com.example.covidmind.alarms.enableReminderNotificationsAlarmAt
import com.example.covidmind.notifications.REMINDER_NOTIFICATION_ID
import com.example.covidmind.notifications.sendReminderNotification
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.util.*


@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var enableButton: Button
    private lateinit var disableButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_settings, container, false)
        enableButton = rootView.findViewById(R.id.enable_button)
        disableButton = rootView.findViewById(R.id.disable_button)
        setupReminderButtons(initial = true)
        viewModel.preferences.isReminderNotificationSetObservable.observe(viewLifecycleOwner) {
            setupReminderButtons()
        }
        return rootView
    }

    private fun setupReminderButtons(initial: Boolean = false) {
        if (viewModel.preferences.isReminderNotificationSet) {
            enableButton.visibility = View.GONE
            disableButton.visibility = View.VISIBLE
            val formattedTime = DateFormat.getTimeInstance(
                DateFormat.SHORT,
                Locale.getDefault()
            ).format(viewModel.preferences.reminderNotificationTime.time)

            disableButton.text =
                resources.getString(R.string.settings_disable_button_label, formattedTime)
        } else {
            enableButton.visibility = View.VISIBLE
            disableButton.visibility = View.GONE
        }

        if (initial) {
            enableButton.setOnClickListener { onEnableButtonClick() }
            disableButton.setOnClickListener { onDisableButtonClick() }
        }
    }

    private fun onEnableButtonClick() {
        val c = Calendar.getInstance()
        val currentHour = c.get(Calendar.HOUR_OF_DAY)
        val currentMinute = c.get(Calendar.MINUTE)
        val is24HourFormat = AndroidDateFormat.is24HourFormat(requireContext())

        TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                with(viewModel.preferences) {
                    val time: Calendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, minute)
                        set(Calendar.SECOND, 0)
                    }

                    requireContext().enableReminderNotificationsAlarmAt(time)
                    setReminderNotificationTime(time)
                    isReminderNotificationSet = true
                }
            }, currentHour, currentMinute, is24HourFormat
        ).show()
    }

    private fun onDisableButtonClick() {
        requireContext().disableReminderNotificationsAlarm()
        viewModel.preferences.isReminderNotificationSet = false
    }
}