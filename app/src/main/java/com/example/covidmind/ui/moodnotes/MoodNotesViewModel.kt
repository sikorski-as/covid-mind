package com.example.covidmind.ui.moodnotes

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.covidmind.repos.MoodNotesRepository

class MoodNotesViewModel @ViewModelInject constructor(
    private val moodNotesRepository: MoodNotesRepository
) : ViewModel() {
    val allMoodNotes = moodNotesRepository.getAllNotes()
}