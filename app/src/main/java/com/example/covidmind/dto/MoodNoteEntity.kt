package com.example.covidmind.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.covidmind.model.MoodNote
import java.util.*

@Entity(tableName = "moodnotes")
class MoodNoteEntity(
    val moodValue: Int,
    val timestamp: Date = Date(System.currentTimeMillis()),
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)

fun MoodNoteEntity.toModel() = MoodNote(id = id, moodValue = moodValue, timestamp = timestamp)
fun MoodNote.toEntity() = MoodNoteEntity(id = id, moodValue = moodValue, timestamp = timestamp)

fun List<MoodNoteEntity>.toModel() = this.map { it.toModel() }