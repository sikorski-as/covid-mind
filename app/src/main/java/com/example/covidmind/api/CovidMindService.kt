package com.example.covidmind.api

import com.example.covidmind.dto.StimulatingActivityJson
import retrofit2.http.GET

interface CovidMindService {
    @GET("activities/latest")
    suspend fun getLatestStimulatingActivities(): List<StimulatingActivityJson>
}