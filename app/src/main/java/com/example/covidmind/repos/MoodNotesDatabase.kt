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
import androidx.room.Update
import java.time.Instant

enum class MoodType {
    UNKNOWN, VERY_BAD, BAD, NORMAL, GOOD, VERY_GOOD
}

@Entity(tableName = "moodnotes")
data class MoodNote(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val moodValue: Int,
    val timestamp: Instant
)

@Dao
interface MoodNoteDao {
    @Query("SELECT * FROM moodnotes")
    suspend fun getAll(): LiveData<List<MoodNote>> // suspend runs a background task to fetch data

    @Query("SELECT * from moodnotes ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getLatestNotes(limit: Int = 1): LiveData<List<MoodNote>>

    @Query("SELECT * FROM moodnotes WHERE id IN (:moodNotesIds)")
    suspend fun loadAllByIds(moodNotesIds: IntArray): LiveData<List<MoodNote>>

    @Insert
    suspend fun insert(moodNote: MoodNote)

    @Update
    suspend fun update(moodNote: MoodNote)

    @Delete
    suspend fun delete(moodNote: MoodNote)
}

@Database(entities = [MoodNote::class], version = 1)
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