package com.example.covidmind.model

data class StimulatingActivity(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val categories: List<StimulatingActivityCategory>,
    val link: String
)

data class StimulatingActivityCategory(
    val id: Int,
    val name: String,
    val color: String
)
