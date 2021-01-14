package com.example.covidmind.ui.moodnoting

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covidmind.dto.MoodNoteEntity
import com.example.covidmind.model.MoodNote
import com.example.covidmind.repos.MoodNotesRepository
import kotlinx.coroutines.launch

class MoodNotingViewModel  @ViewModelInject constructor(
    private val moodNotesRepository: MoodNotesRepository
) : ViewModel() {
    fun insertOrReplace(moodNote: MoodNote){
        viewModelScope.launch {
            moodNotesRepository.insertOrReplace(moodNote)
        }
    }
}