package com.example.covidmind.api

import com.example.covidmind.dto.StimulatingActivityJson
import com.hadiyarajesh.flower.ApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface CovidMindService {
    @GET("activities/latest")
    fun getLatestStimulatingActivities(): Flow<ApiResponse<List<StimulatingActivityJson>>>
}