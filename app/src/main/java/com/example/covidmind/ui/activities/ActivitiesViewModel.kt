package com.example.covidmind.ui.activities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.covidmind.model.StimulatingActivity
import com.example.covidmind.repos.MoodNotesRepository
import com.example.covidmind.repos.StimulatingActivitiesRepository
import com.hadiyarajesh.flower.Resource

class ActivitiesViewModel @ViewModelInject constructor(
    private val stimulatingActivitiesRepository: StimulatingActivitiesRepository
) : ViewModel() {

    private var _stimulatingActivities =
        stimulatingActivitiesRepository.getLatestStimulatingActivities(forceServerPull = false)
            .asLiveData()

    val stimulatingActivities = Transformations.switchMap(_stimulatingActivities) {
        return@switchMap MutableLiveData<Resource<List<StimulatingActivity>>>(_stimulatingActivities.value)
    }

    fun forceRefreshStimulatingActivities() {
        _stimulatingActivities =
            stimulatingActivitiesRepository.getLatestStimulatingActivities(forceServerPull = true)
                .asLiveData()
    }
}