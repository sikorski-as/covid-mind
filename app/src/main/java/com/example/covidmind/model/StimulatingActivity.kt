package com.example.covidmind.model

data class StimulatingActivity(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val categories: List<String>,
    val link: String
)

data class StimulatingActivityCategory(
    val id: Int = 0,
    val name: String = "",
    val color: String = ""
)
