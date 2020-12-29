package com.example.covidmind.repos

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*


@Entity(
    tableName = "moodnotes"
)
data class MoodNote(
    val moodValue: Int,
    val timestamp: Date = Date(System.currentTimeMillis())
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        const val MOOD_VERY_BAD = 1
        const val MOOD_BAD = 2
        const val MOOD_NEUTRAL = 3
        const val MOOD_GOOD = 4
        const val MOOD_VERY_GOOD = 5
    }
}

@Dao
interface MoodNoteDao {
    @Query("SELECT * FROM moodnotes ORDER BY timestamp DESC")
    fun getAll(): LiveData<List<MoodNote>> // suspend runs a background task to fetch data

    @Query("SELECT * from moodnotes ORDER BY timestamp DESC LIMIT :limit")
    fun getLatestNotes(limit: Int = 1): List<MoodNote>

    @Query("SELECT * from moodnotes WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC")
    fun loadBetweenSorted(start: Date, end: Date): LiveData<List<MoodNote>>

    @Query("SELECT * from moodnotes WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC LIMIT :limit")
    fun loadBetweenSortedWithLimit(start: Date, end: Date, limit: Int): LiveData<List<MoodNote>>

    @Query("SELECT * FROM moodnotes WHERE id IN (:moodNotesIds)")
    fun loadAllByIds(moodNotesIds: IntArray): List<MoodNote>

    @Query("SELECT count(*) FROM moodnotes WHERE timestamp = :timestamp limit 1")
    fun checkIfNotedAt(timestamp: Date): LiveData<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceOnDateConflict(moodNote: MoodNote)

    @Update
    suspend fun update(moodNote: MoodNote)

    @Delete
    suspend fun delete(moodNote: MoodNote)
}

class DateConverter {
    @TypeConverter
    fun timestampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}

@Database(entities = [MoodNote::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun moodNotesDao(): MoodNoteDao
}