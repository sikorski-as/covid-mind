package com.example.covidmind.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import java.util.*


enum class MoodType {
    UNKNOWN, VERY_BAD, BAD, NORMAL, GOOD, VERY_GOOD
}


@Entity(tableName = "moodnotes")
data class MoodNote(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val moodValue: Int,

    val timestamp: Date
)

@Dao
interface MoodNoteDao {
    @Query("SELECT * FROM moodnotes")
    suspend fun getAll(): List<MoodNote> // suspend runs a background task to fetch data

    @Query("SELECT * from moodnotes ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getLatestNotes(limit: Int = 1): List<MoodNote>

    @Query("SELECT * FROM moodnotes WHERE id IN (:moodNotesIds)")
    suspend fun loadAllByIds(moodNotesIds: IntArray): List<MoodNote>

    @Insert
    suspend fun insert(moodNote: MoodNote)

    @Update
    suspend fun update(moodNote: MoodNote)

    @Delete
    suspend fun delete(moodNote: MoodNote)
}

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

@Database(entities = [MoodNote::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun moodNotesDao(): MoodNoteDao

    companion object {
        @Volatile // changes significant to other threads (update immediately!)
        private var instance: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): LocalDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                LocalDatabase::class.java, "covid_mind.db"
            )
                .fallbackToDestructiveMigration() // this might be replaced when migrating to production
                .build()
        }
    }
}