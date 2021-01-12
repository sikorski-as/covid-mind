package com.example.covidmind.ui.activities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covidmind.model.StimulatingActivity
import com.example.covidmind.repos.StimulatingActivitiesRepository
import kotlinx.coroutines.launch
import java.io.IOException

class ActivitiesViewModel @ViewModelInject constructor(
    private val stimulatingActivitiesRepository: StimulatingActivitiesRepository
) : ViewModel() {
    val stimulatingActivities: LiveData<List<StimulatingActivity>> =
        stimulatingActivitiesRepository.stimulatingActivities

    fun forceRefreshStimulatingActivities() {
        viewModelScope.launch {
            try {
                stimulatingActivitiesRepository.refreshStimulatingActivities()
//                _eventNetworkError.value = false
//                _isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
//                // Show a Toast error message and hide the progress bar.
//                if(playlist.value.isNullOrEmpty())
//                    _eventNetworkError.value = true
            }
        }
    }
}