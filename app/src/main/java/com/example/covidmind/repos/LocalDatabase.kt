package com.example.covidmind.repos

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.covidmind.dto.StimulatingActivityEntity

@Database(entities = [MoodNote::class, StimulatingActivityEntity::class], version = 2)
@TypeConverters(DateConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun moodNotesDao(): MoodNoteDao
    abstract fun stimulatingActivitiesDao(): StimulatingActivitiesDao
}