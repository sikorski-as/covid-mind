package com.example.covidmind.repos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.covidmind.dto.StimulatingActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StimulatingActivitiesDao {
    @Query("SELECT * FROM stimulating_activities ORDER BY id DESC")
    fun getLatest(): Flow<List<StimulatingActivityEntity>>

    @Query("DELETE from stimulating_activities")
    fun deleteAll()

    @Insert
    fun insertMany(newStimulatingActivities: List<StimulatingActivityEntity>)

    @Transaction
    fun update(newStimulatingActivities: List<StimulatingActivityEntity>) {
        deleteAll()
        insertMany(newStimulatingActivities)
    }
}