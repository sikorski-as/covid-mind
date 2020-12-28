package com.example.covidmind.repos

import androidx.lifecycle.LiveData
import java.time.LocalDate
import javax.inject.Inject

class MoodNotesRepository @Inject constructor(localDatabase: LocalDatabase) {
    private var moodNotesDao: MoodNoteDao = localDatabase.moodNotesDao()

    fun checkIfNotedToday(): LiveData<Boolean> = moodNotesDao.checkIfNotedAt(LocalDate.now())

    suspend fun insertOrReplace(moodNote: MoodNote) {
        moodNotesDao.insertOrReplaceOnDateConflict(moodNote)
    }
}