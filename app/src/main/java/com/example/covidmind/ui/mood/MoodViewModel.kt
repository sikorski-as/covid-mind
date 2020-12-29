package com.example.covidmind.ui.mood

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.covidmind.repos.MoodNote
import com.example.covidmind.repos.MoodNotesRepository
import kotlinx.coroutines.launch

class MoodViewModel @ViewModelInject constructor(
    private val moodNotesRepository: MoodNotesRepository
) : ViewModel() {
    private val latestMoodNotes: LiveData<List<MoodNote>> = moodNotesRepository.getAllNotes()
    val averageMood: LiveData<Double?> = Transformations.switchMap(latestMoodNotes) {
        moodNotes ->
        val size = moodNotes.size
        if(size == 0){
            return@switchMap null
        }
        else{
            val values = moodNotes.map { it.moodValue }
            val average = values.sum().toDouble() / size
            return@switchMap MutableLiveData<Double?>(average)
        }
    }
}
