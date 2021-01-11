package com.example.covidmind.repos

import android.util.Log
import com.example.covidmind.api.CovidMindService
import com.example.covidmind.dto.*
import com.example.covidmind.model.StimulatingActivity
import com.hadiyarajesh.flower.Resource
import com.hadiyarajesh.flower.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StimulatingActivitiesRepository @Inject constructor(
    localDatabase: LocalDatabase,
    private val covidMindService: CovidMindService
) {
    private var stimulatingActivityDao: StimulatingActivitiesDao =
        localDatabase.stimulatingActivitiesDao()

    fun getLatestStimulatingActivities(forceServerPull: Boolean = true)
            : Flow<Resource<List<StimulatingActivity>>> {
        return networkBoundResource(
            fetchFromLocal = { stimulatingActivityDao.getLatest().map { it.toModel() } },
            shouldFetchFromRemote = { it == null || forceServerPull },
            fetchFromRemote = {
                Log.i("[CM] fetching...", "")
                covidMindService.getLatestStimulatingActivities()
            },
            processRemoteResponse = {
                // response -> Log.i("[CM] body", response.body.toString())
            },
            saveRemoteData = { stimulatingActivityDao.update(it.toModel().toEntity()) },
            onFetchFailed = { errorBody, statusCode ->
                // Log.i("[CM]", "$statusCode: $errorBody")
            }
        ).flowOn(Dispatchers.IO)
    }
}
