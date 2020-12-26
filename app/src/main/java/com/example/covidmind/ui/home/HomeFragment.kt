package com.example.covidmind.ui.home

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.covidmind.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field


class HomeFragment : Fragment() {
    companion object {
        const val GOOGLE_FIT_STEPS_PERMISSIONS_REQUEST_CODE = 0
        val FITNESS_OPTIONS: FitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()
    }

    private lateinit var homeViewModel: HomeViewModel

    private fun tryToAccessStepsFromGoogleFit() {
        val account = GoogleSignIn.getAccountForExtension(context!!, FITNESS_OPTIONS)

        if (!GoogleSignIn.hasPermissions(account, FITNESS_OPTIONS)) {
            Log.i(TAG, "[CM] Asking for permission")
            GoogleSignIn.requestPermissions(
                this,
                GOOGLE_FIT_STEPS_PERMISSIONS_REQUEST_CODE, // e.g. 1
                account,
                FITNESS_OPTIONS
            )
        } else {
            Log.i(TAG, "[CM] Already has permissions")
            accessStepsFromGoogleFit()
        }
    }

    private fun accessStepsFromGoogleFit() {
        Log.i(TAG, "[CM] Accessing steps from Google Fit")

        val account = GoogleSignIn.getAccountForExtension(context!!, FITNESS_OPTIONS)

        Fitness.getHistoryClient(this.activity!!, account)
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { result ->
                val totalSteps = if (result.isEmpty)
                    0
                else
                    result.dataPoints[0].getValue(Field.FIELD_STEPS).asInt()

                view?.findViewById<TextView>(R.id.steps_counter)?.text = totalSteps.toString()
                view?.findViewById<Button>(R.id.steps_button)?.text =
                    resources.getString(R.string.google_fit_powered_by)

                Log.i(TAG, "[CM] It went fine, number of steps: $totalSteps")
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "[CM ]There was a problem getting steps: " + e.localizedMessage)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG, "[CM] Got permission answer")
        if (requestCode == GOOGLE_FIT_STEPS_PERMISSIONS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG, "[CM] Got permission answer: YES")
                accessStepsFromGoogleFit()
            } else {
                Log.i(TAG, "[CM] Got permission answer: NO")
                Toast.makeText(context, R.string.permission_not_granted, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<AppCompatButton>(R.id.steps_button).setOnClickListener {
            tryToAccessStepsFromGoogleFit()
        }
        tryToAccessStepsFromGoogleFit()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}