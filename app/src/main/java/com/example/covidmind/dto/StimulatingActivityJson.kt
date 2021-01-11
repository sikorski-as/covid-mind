package com.example.covidmind.dto

import com.example.covidmind.model.StimulatingActivity

data class StimulatingActivityJson(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val categories: List<StimulatingActivityCategoryJson> = mutableListOf(),
    val link: String = ""
)

fun StimulatingActivity.toJson() = StimulatingActivityJson(
    id = id,
    title = title,
    description = description,
    date = date,
    categories = categories.toJson(),
    link = link
)

fun StimulatingActivityJson.toModel() = StimulatingActivity(
    id = id,
    title = title,
    description = description,
    date = date,
    categories = categories.toModel(),
    link = link
)

fun List<StimulatingActivity>.toJson() = map { it.toJson() }
fun List<StimulatingActivityJson>.toModel() = map { it.toModel() }