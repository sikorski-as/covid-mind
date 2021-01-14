package com.example.covidmind.ui.mood

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.covidmind.model.MoodNote
import com.example.covidmind.repos.MoodNotesRepository

class MoodViewModel @ViewModelInject constructor(
    private val moodNotesRepository: MoodNotesRepository
) : ViewModel() {
    val shortAverageLengthInDays: Int = 3
    val longAverageLengthInDays: Int = 7

    private fun computeAverageMood(moodNotes: List<MoodNote>): Double? {
        val size = moodNotes.size
        return if (size == 0) {
            null
        } else {
            val values = moodNotes.map { it.moodValue }
            values.sum().toDouble() / size
        }
    }

    val shortAverage: LiveData<Double?> =
        Transformations.switchMap(moodNotesRepository.getNotesFromLastNDays(shortAverageLengthInDays)) { newMoodNotes ->
            return@switchMap MutableLiveData(computeAverageMood(newMoodNotes))
        }
    val longAverage =
        Transformations.switchMap(moodNotesRepository.getNotesFromLastNDays(longAverageLengthInDays)) { newMoodNotes ->
            return@switchMap MutableLiveData(computeAverageMood(newMoodNotes))
        }
}
