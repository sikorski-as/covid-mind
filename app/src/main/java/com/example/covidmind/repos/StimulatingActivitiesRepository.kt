package com.example.covidmind.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.covidmind.api.CovidMindService
import com.example.covidmind.dto.toEntity
import com.example.covidmind.dto.toModel
import com.example.covidmind.model.StimulatingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StimulatingActivitiesRepository @Inject constructor(
    localDatabase: LocalDatabase,
    private val covidMindService: CovidMindService
) {
    private var stimulatingActivityDao: StimulatingActivitiesDao =
        localDatabase.stimulatingActivitiesDao()

    val stimulatingActivities: LiveData<List<StimulatingActivity>> =
        Transformations.map(stimulatingActivityDao.getLatest()) {
            it.toModel()
        }

//    private var _stimulatingActivitiesLoading = MutableLiveData<Boolean>(false)
//    val stimulatingActivitiesLoading = _stimulatingActivitiesLoading

    suspend fun refreshStimulatingActivities() {
        withContext(Dispatchers.IO) {
//            _stimulatingActivitiesLoading.value = true
            val newStimulatingActivities = covidMindService.getLatestStimulatingActivities()
            stimulatingActivityDao.update(newStimulatingActivities.toModel().toEntity())
        }
    }
}
