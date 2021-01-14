package com.example.covidmind.ui.moodnotes

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covidmind.dto.MoodNoteEntity
import com.example.covidmind.model.MoodNote
import com.example.covidmind.repos.MoodNotesRepository
import kotlinx.coroutines.launch

class MoodNotesViewModel @ViewModelInject constructor(
    private val moodNotesRepository: MoodNotesRepository
) : ViewModel() {
    val allMoodNotes = moodNotesRepository.getAllNotes()
    fun deleteMoodNote(moodNote: MoodNote){
        viewModelScope.launch {
            moodNotesRepository.deleteMoodNote(moodNote)
        }
    }
}