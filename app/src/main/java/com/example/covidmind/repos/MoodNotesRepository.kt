package com.example.covidmind.repos

import android.app.Application

class MoodNotesRepository(application: Application) {
    var moodNotesDao: MoodNoteDao = LocalDatabase.getInstance(application).moodNotesDao()
}