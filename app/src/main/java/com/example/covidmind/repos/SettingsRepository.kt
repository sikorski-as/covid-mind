package com.example.covidmind.repos

import android.content.Context

class SettingsRepository(val context: Context) {
    private val preferences =
        context.getSharedPreferences("CovidMindSettings", Context.MODE_PRIVATE)

    var shouldAskForGoogleFitPermissions: Boolean
        get() {
            return preferences.getBoolean("shouldAskForGoogleFitPermissions", true)
        }
        set(value) {
            preferences.edit().putBoolean("shouldAskForGoogleFitPermissions", value).apply()
        }
}