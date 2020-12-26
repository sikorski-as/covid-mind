package com.example.covidmind.repos

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field

class GoogleFitRepository {
    private var totalSteps = MutableLiveData<Int>()

    companion object {
        const val GOOGLE_FIT_STEPS_PERMISSIONS_REQUEST_CODE = 0
        val FITNESS_OPTIONS: FitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()
    }

    fun getSteps(context: Context): MutableLiveData<Int> {
        Log.i(ContentValues.TAG, "[CM] Accessing steps from Google Fit")

        val account = GoogleSignIn.getAccountForExtension(context, FITNESS_OPTIONS)

        Fitness.getHistoryClient(context, account)
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { result ->
                val stepsFromApi = if (result.isEmpty)
                    0
                else
                    result.dataPoints[0].getValue(Field.FIELD_STEPS).asInt()

                totalSteps.value = stepsFromApi
                Log.i(ContentValues.TAG, "[CM] It went fine, number of steps: $totalSteps")
            }
            .addOnFailureListener { e ->
                Log.i(
                    ContentValues.TAG,
                    "[CM ]There was a problem getting steps: " + e.localizedMessage
                )

            }
        return totalSteps
    }
}