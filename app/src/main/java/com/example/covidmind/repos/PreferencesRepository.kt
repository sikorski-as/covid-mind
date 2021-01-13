package com.example.covidmind.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.IllegalArgumentException
import java.util.*
import javax.inject.Inject


class PreferencesRepository @Inject constructor(@ApplicationContext context: Context) {
    private val preferences =
        context.getSharedPreferences("CovidMindSettings", Context.MODE_PRIVATE)

    val reminderNotificationHour: Int
        get() = preferences.getInt("reminderNotificationHour", 0)
    val reminderNotificationMinute: Int
        get() = preferences.getInt("reminderNotificationMinute", 0)

    val reminderNotificationTime: Calendar
        get() = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, reminderNotificationHour)
            set(Calendar.MINUTE, reminderNotificationMinute)
        }

    var isReminderNotificationSet: Boolean
        get() = preferences.getBoolean("isReminderNotificationSet", false)
        set(value) {
            preferences.edit().putBoolean("isReminderNotificationSet", value).apply()
            _isReminderNotificationSetObservable.postValue(value)
        }

    private val _isReminderNotificationSetObservable = MutableLiveData<Boolean>()
    val isReminderNotificationSetObservable: LiveData<Boolean> = _isReminderNotificationSetObservable

    fun setReminderNotificationTime(time: Calendar) {
        preferences.edit()
            .putInt("reminderNotificationHour", time.get(Calendar.HOUR_OF_DAY))
            .apply()
        preferences.edit()
            .putInt("reminderNotificationMinute", time.get(Calendar.MINUTE))
            .apply()
    }

    private val _isConnectedToGoogleFitObservable = MutableLiveData<Boolean>()
    val isConnectedToGoogleFitObservable: LiveData<Boolean> = _isConnectedToGoogleFitObservable

    var isConnectedToGoogleFit: Boolean
        get() = preferences.getBoolean("isConnectedToGoogleFit", false)
        set(value) {
            preferences.edit().putBoolean("isConnectedToGoogleFit", value).apply()
            _isConnectedToGoogleFitObservable.postValue(value)
        }

}