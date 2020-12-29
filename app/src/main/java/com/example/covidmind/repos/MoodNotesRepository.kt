package com.example.covidmind.repos

import androidx.lifecycle.LiveData
import java.util.*
import javax.inject.Inject

class MoodNotesRepository @Inject constructor(localDatabase: LocalDatabase) {
    private var moodNotesDao: MoodNoteDao = localDatabase.moodNotesDao()

//    fun checkIfNotedToday(): LiveData<Boolean> = moodNotesDao.checkIfNotedAt(LocalDate.now())

    fun loadBetweenSorted(start: Date, end: Date): LiveData<List<MoodNote>> =
        moodNotesDao.loadBetweenSorted(start, end)

    fun getAllNotes(): LiveData<List<MoodNote>> = moodNotesDao.getAll()

    suspend fun insertOrReplace(moodNote: MoodNote) {
        moodNotesDao.insertOrReplaceOnDateConflict(moodNote)
    }
}