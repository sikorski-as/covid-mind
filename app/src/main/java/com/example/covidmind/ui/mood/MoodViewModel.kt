package com.example.covidmind.ui.mood

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.covidmind.repos.MoodNote
import com.example.covidmind.repos.MoodNotesRepository
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MoodViewModel @ViewModelInject constructor(
    private val moodNotesRepository: MoodNotesRepository
) : ViewModel() {
    val notedToday: LiveData<Boolean> = moodNotesRepository.checkIfNotedToday()

    fun insertOrReplace(moodNote: MoodNote){
        viewModelScope.launch {
            moodNotesRepository.insertOrReplace(moodNote)
        }
    }
}
