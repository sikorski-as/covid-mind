package com.example.covidmind.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.covidmind.dto.MoodNoteEntity
import com.example.covidmind.dto.toEntity
import com.example.covidmind.dto.toModel
import com.example.covidmind.model.MoodNote
import javax.inject.Inject

class MoodNotesRepository @Inject constructor(localDatabase: LocalDatabase) {
    private var moodNotesDao: MoodNoteDao = localDatabase.moodNotesDao()

    fun getAllNotes(): LiveData<List<MoodNote>> =
        Transformations.switchMap(moodNotesDao.getAll()) {
            MutableLiveData(it.toModel())
        }

    fun getNotesFromLastNDays(numberOfDays: Int): LiveData<List<MoodNote>> {
        check(numberOfDays > 0)
        return Transformations.switchMap(moodNotesDao.getMoodNotesFromLastNDays(numberOfDays)) {
            MutableLiveData(it.toModel())
        }
    }

    suspend fun insertOrReplace(moodNote: MoodNote) {
        moodNotesDao.insertOrReplaceOnDateConflict(moodNote.toEntity())
    }

    suspend fun deleteMoodNote(moodNote: MoodNote) {
        moodNotesDao.delete(moodNote.toEntity())
    }
}