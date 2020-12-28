package com.example.covidmind.repos

import androidx.lifecycle.LiveData
import androidx.room.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate


enum class MoodType {
    UNKNOWN, VERY_BAD, BAD, NORMAL, GOOD, VERY_GOOD
}


@Entity(
    tableName = "moodnotes",
    indices = [Index(value = ["timestamp"], unique = true)]
)
data class MoodNote(
    val moodValue: Int,
    val timestamp: LocalDate
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Dao
interface MoodNoteDao {
    @Query("SELECT * FROM moodnotes")
    suspend fun getAll(): List<MoodNote> // suspend runs a background task to fetch data

    @Query("SELECT * from moodnotes ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getLatestNotes(limit: Int = 1): List<MoodNote>

    @Query("SELECT * FROM moodnotes WHERE id IN (:moodNotesIds)")
    suspend fun loadAllByIds(moodNotesIds: IntArray): List<MoodNote>

    @Query("SELECT count(*) FROM moodnotes WHERE timestamp = :timestamp limit 1")
    fun checkIfNotedAt(timestamp: LocalDate): LiveData<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceOnDateConflict(moodNote: MoodNote)

    @Update
    suspend fun update(moodNote: MoodNote)

    @Delete
    suspend fun delete(moodNote: MoodNote)
}

class DateConverter {
    @TypeConverter
    fun timestampToLocalDate(timestamp: String): LocalDate {
        return timestamp.let { LocalDate.parse(timestamp) }
    }

    @TypeConverter
    fun localDateToTimestamp(date: LocalDate): String {
        return date.toString()
    }
}

@Database(entities = [MoodNote::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun moodNotesDao(): MoodNoteDao
}