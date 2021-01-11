package com.example.covidmind.repos

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MoodNote::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun moodNotesDao(): MoodNoteDao
}