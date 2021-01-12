package com.example.covidmind.ui.stimulating_activities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covidmind.model.StimulatingActivity
import com.example.covidmind.OneShotNotificationWithContent
import com.example.covidmind.repos.StimulatingActivitiesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class ActivitiesViewModel @ViewModelInject constructor(
    private val stimulatingActivitiesRepository: StimulatingActivitiesRepository
) : ViewModel() {
    val stimulatingActivities: LiveData<List<StimulatingActivity>> =
        stimulatingActivitiesRepository.stimulatingActivities

    private var _loadingDataSuccessful = MutableLiveData<OneShotNotificationWithContent<Boolean>>()
    val loadingDataSuccessful: LiveData<OneShotNotificationWithContent<Boolean>> = _loadingDataSuccessful

    private var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun forceRefreshStimulatingActivities() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                delay(1000) // for nice user experience
                stimulatingActivitiesRepository.refreshStimulatingActivities()
                _loadingDataSuccessful.postValue(
                    OneShotNotificationWithContent(
                        true
                    )
                )
            } catch (e: Exception) {
                _loadingDataSuccessful.postValue(
                    OneShotNotificationWithContent(
                        false
                    )
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}