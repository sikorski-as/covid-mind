package com.example.covidmind.ui.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.covidmind.repos.PreferencesRepository

class SettingsViewModel @ViewModelInject constructor(
    val preferences: PreferencesRepository
) : ViewModel()