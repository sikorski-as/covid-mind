package com.example.covidmind.ui.physical_activity

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covidmind.model.StepsData
import com.example.covidmind.repos.GoogleFitRepository

class PhysicalActivityViewModel @ViewModelInject constructor(
    private val googleFitRepository: GoogleFitRepository
) : ViewModel() {
    val stepsData: LiveData<StepsData> = googleFitRepository.stepsData
    fun tryToConnect(): GoogleFitRepository.Status = googleFitRepository.tryToConnect()
    fun refreshStepsIfConnected() = googleFitRepository.refreshStepsIfConnected()
}
