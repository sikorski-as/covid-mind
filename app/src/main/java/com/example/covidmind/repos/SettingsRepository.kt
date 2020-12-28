package com.example.covidmind.repos

import android.content.Context


class SettingsRepository(context: Context) {
    private val preferences =
        context.getSharedPreferences("CovidMindSettings", Context.MODE_PRIVATE)

    var isGoogleFitActivated: Boolean
        get() = preferences.getBoolean("isGoogleFitActivated", false)
        set(value) = preferences.edit().putBoolean("isGoogleFitActivated", value).apply()

    var latestGoogleFitSteps: Int?
        get() {
            val value = preferences.getInt("latestGoogleFitSteps", -1)
            return if (value == -1) null else value
        }
        set(value) = preferences.edit().putInt("latestGoogleFitSteps", value ?: -1).apply()
}