package com.example.covidmind.repos

import androidx.lifecycle.LiveData
import javax.inject.Inject

class MoodNotesRepository @Inject constructor(localDatabase: LocalDatabase) {
    private var moodNotesDao: MoodNoteDao = localDatabase.moodNotesDao()

    fun getAllNotes(): LiveData<List<MoodNote>> = moodNotesDao.getAll()

    suspend fun insertOrReplace(moodNote: MoodNote) {
        moodNotesDao.insertOrReplaceOnDateConflict(moodNote)
    }

    suspend fun deleteMoodNote(moodNote: MoodNote){
        moodNotesDao.delete(moodNote)
    }
}