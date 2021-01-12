package com.example.covidmind.repos

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.covidmind.dto.StimulatingActivityEntity
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.*

@Database(entities = [MoodNote::class, StimulatingActivityEntity::class], version = 2)
@TypeConverters(LocalDatabaseTypeConverters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun moodNotesDao(): MoodNoteDao
    abstract fun stimulatingActivitiesDao(): StimulatingActivitiesDao
}

class LocalDatabaseTypeConverters {
    @TypeConverter
    fun timestampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun listToDatabase(list: List<String>): String {
        return jacksonObjectMapper().writeValueAsString(list)
    }

    @TypeConverter
    fun listFromDatabase(data: String): MutableList<String> {
        return jacksonObjectMapper().readValue(data)
    }
}
