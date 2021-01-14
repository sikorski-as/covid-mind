package com.example.covidmind.repos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.covidmind.dto.MoodNoteEntity
import java.util.*


@Dao
interface MoodNoteDao {
    @Query("SELECT * FROM moodnotes ORDER BY timestamp DESC")
    fun getAll(): LiveData<List<MoodNoteEntity>> // suspend runs a background task to fetch data

    @Query("SELECT * from moodnotes ORDER BY timestamp DESC LIMIT :limit")
    fun getLatestNotes(limit: Int = 1): List<MoodNoteEntity>

    @Query("SELECT * from moodnotes WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC")
    fun loadBetweenSorted(start: Date, end: Date): LiveData<List<MoodNoteEntity>>

    @Query("SELECT * from moodnotes WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC LIMIT :limit")
    fun loadBetweenSortedWithLimit(start: Date, end: Date, limit: Int): LiveData<List<MoodNoteEntity>>

    @Query("SELECT * from moodnotes WHERE timestamp >= (1000 * strftime('%s', datetime('now', '-' || :numberOfDays || ' day')))  ORDER BY timestamp DESC")
    fun getMoodNotesFromLastNDays(numberOfDays: Int): LiveData<List<MoodNoteEntity>>

    @Query("SELECT * FROM moodnotes WHERE id IN (:moodNotesIds)")
    fun loadAllByIds(moodNotesIds: IntArray): List<MoodNoteEntity>

    @Query("SELECT count(*) FROM moodnotes WHERE timestamp = :timestamp limit 1")
    fun checkIfNotedAt(timestamp: Date): LiveData<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceOnDateConflict(moodNoteEntity: MoodNoteEntity)

    @Update
    suspend fun update(moodNoteEntity: MoodNoteEntity)

    @Delete
    suspend fun delete(moodNoteEntity: MoodNoteEntity)
}


