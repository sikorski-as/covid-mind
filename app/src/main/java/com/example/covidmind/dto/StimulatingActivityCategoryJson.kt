package com.example.covidmind.dto

import com.example.covidmind.model.StimulatingActivityCategory

class StimulatingActivityCategoryJson(
    val id: Int = 0,
    val name: String = "",
    val color: String = ""
)

fun StimulatingActivityCategory.toJson() = StimulatingActivityCategoryJson(
    id = id,
    name = name,
    color = color
)

fun StimulatingActivityCategoryJson.toModel() = StimulatingActivityCategory(
    id = id,
    name = name,
    color = color
)

fun List<StimulatingActivityCategory>.toJson() = map { it.toJson() }
fun List<StimulatingActivityCategoryJson>.toModel() = map { it.toModel() }