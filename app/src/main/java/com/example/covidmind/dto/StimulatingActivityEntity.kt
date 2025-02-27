package com.example.covidmind.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.covidmind.model.StimulatingActivity
import com.example.covidmind.model.StimulatingActivityCategory
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

@Entity(tableName = "stimulating_activities")
class StimulatingActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val categories: List<String>,
    val link: String
)

fun StimulatingActivity.toEntity() = StimulatingActivityEntity(
    id = id,
    title = title,
    description = description,
    date = date,
    categories = categories,
    link = link
)

fun StimulatingActivityEntity.toModel() = StimulatingActivity(
    id = id,
    title = title,
    description = description,
    date = date,
    categories = categories,
    link = link
)


fun List<StimulatingActivity>.toEntity() = map { it.toEntity() }
fun List<StimulatingActivityEntity>.toModel() = map { it.toModel() }