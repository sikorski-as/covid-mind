package com.example.covidmind.ui.activities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.covidmind.repos.MoodNotesRepository
import com.example.covidmind.repos.StimulatingActivitiesRepository

class ActivitiesViewModel @ViewModelInject constructor(
    private val stimulatingActivitiesRepository: StimulatingActivitiesRepository
) : ViewModel() {

    val stimulatingActivities =
        stimulatingActivitiesRepository.getLatestStimulatingActivities(forceServerPull = false)
            .asLiveData()

    fun forceRefreshStimulatingActivities() {
        stimulatingActivitiesRepository.getLatestStimulatingActivities(forceServerPull = true)
    }
}