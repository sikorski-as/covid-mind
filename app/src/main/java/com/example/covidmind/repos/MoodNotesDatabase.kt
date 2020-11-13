package com.example.covidmind.repos

import androidx.room.*
import java.time.Instant

enum class MoodType {
    UNKNOWN, VERY_BAD, BAD, NORMAL, GOOD, VERY_GOOD
}

@Entity(tableName = "moodnotes")
data class MoodNote(
    @PrimaryKey val id: Int,
    val moodType: MoodType,
    val timestamp: Instant
)

@Dao
interface MoodNoteDao {
    @Query("SELECT * FROM moodnotes")
    fun getAll(): List<MoodNote>

    @Query("SELECT * from moodnotes ORDER BY timestamp DESC LIMIT :limit")
    fun getLatestNotes(limit: Int = 1)

    @Query("SELECT * FROM moodnotes WHERE id IN (:moodNotesIds)")
    fun loadAllByIds(moodNotesIds: IntArray): List<MoodNote>

    @Insert
    fun insert(moodNote: MoodNote)

    @Delete
    fun delete(moodNote: MoodNote)
}

@Database(entities = [MoodNote::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moodNotesDao(): MoodNoteDao
}