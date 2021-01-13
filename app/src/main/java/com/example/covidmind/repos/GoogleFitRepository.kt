package com.example.covidmind.repos

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.covidmind.api.Google
import com.example.covidmind.model.StepsData
import com.example.covidmind.ui.physical_activity.PhysicalActivityFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class GoogleFitRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesRepository: PreferencesRepository
) {
    private var _stepsData = MutableLiveData<StepsData>(StepsData(connected = false, steps = null))
    val stepsData: LiveData<StepsData> = _stepsData

    enum class Status(val successful: Boolean) {
        NO_PERMISSION(false),
        NO_ACCOUNT(false),
        ACTIVATED(true),
        ALREADY_ACTIVE(true)
    }

    fun tryToConnect(): Status {
        val account = Google.Fit.getAccountForStepsData(context) ?: return Status.NO_ACCOUNT
        return if (Google.Fit.hasPermissionsForStepsData(account)) {
            if (preferencesRepository.isConnectedToGoogleFit) {
                Status.ALREADY_ACTIVE
            } else {
                activateGoogleFit()
                refreshSteps()
                Status.ACTIVATED
            }
        } else {
            Status.NO_PERMISSION
        }
    }

    fun disconnect() {
        deactivateGoogleFit()
    }

    private fun activateGoogleFit() {
        _stepsData.postValue(StepsData(connected = true, steps = null))
        preferencesRepository.isConnectedToGoogleFit = true
    }

    private fun deactivateGoogleFit() {
        _stepsData.postValue(StepsData(connected = false, steps = null))
        preferencesRepository.isConnectedToGoogleFit = false
    }

    fun refreshStepsIfConnected() {
        if (preferencesRepository.isConnectedToGoogleFit) {
            refreshSteps()
        }
    }

    private fun refreshSteps() {
        val account = Google.Fit.getAccountForStepsData(context)
        if (account != null) {
            Fitness.getHistoryClient(context, account)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener { result ->
                    Log.i("CM", "REFRESHED STEPS!")
                    val totalStepsToday =
                        if (result.isEmpty) 0
                        else result.dataPoints[0].getValue(Field.FIELD_STEPS).asInt()
                    _stepsData.postValue(StepsData(connected = true, steps = totalStepsToday))
                }
                .addOnFailureListener {
                    Log.i("CM", "FAILEEED")
                }
        } else {
            deactivateGoogleFit()
            Log.i("CM", "DEACTIVATED GOOGLE FIT XD!")
        }
    }
}