package com.example.covidmind.ui.physical_activity

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.covidmind.R
import com.example.covidmind.repos.SettingsRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field

class PhysicalActivityFragment : Fragment() {

    companion object {
        const val GOOGLE_FIT_STEPS_PERMISSIONS_REQUEST_CODE = 0
        const val GOOGLE_ACCOUNT_CHOSEN = 1
        val FITNESS_OPTIONS: FitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()
    }

    private lateinit var viewModel: PhysicalActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_physical_activity, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PhysicalActivityViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val stepsButton = view.findViewById<AppCompatButton>(R.id.steps_button)
        val settings = SettingsRepository(requireContext())

        if (settings.isGoogleFitActivated) {
            stepsButton.text = resources.getString(R.string.google_fit_powered_by)
        } else {
            stepsButton.text = resources.getString(R.string.google_fit_prompt_title)
            stepsButton.setOnClickListener {
                tryToAccessStepsFromGoogleFit()
            }
        }
    }

    private fun startSignInIntent() {
        val signInClient: GoogleSignInClient = GoogleSignIn.getClient(
            this.activity!!,
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
        val intent = signInClient.signInIntent
        startActivityForResult(
            intent,
            GOOGLE_ACCOUNT_CHOSEN
        )
    }

    private fun tryToAccessStepsFromGoogleFit() {
        val account = GoogleSignIn.getAccountForExtension(requireContext(), FITNESS_OPTIONS)

        Log.i(ContentValues.TAG, "[CM] account=${account}")
        Log.i(ContentValues.TAG, "[CM] account.id=${account.id}")
        Log.i(ContentValues.TAG, "[CM] account.idToken=${account.idToken}")
        Log.i(ContentValues.TAG, "[CM] account.displayName=${account.displayName}")

        if (account.id.isNullOrEmpty()) {
            startSignInIntent()
            return
        }

        Log.i(ContentValues.TAG, "[CM] account=${account}")
        Log.i(ContentValues.TAG, "[CM] account.id=${account.id}")
        Log.i(ContentValues.TAG, "[CM] account.idToken=${account.idToken}")
        Log.i(ContentValues.TAG, "[CM] account.displayName=${account.displayName}")

        if (GoogleSignIn.hasPermissions(account, FITNESS_OPTIONS)) {
            Log.i(ContentValues.TAG, "[CM] Already has permissions")
            accessStepsFromGoogleFit()
        } else {
            Log.i(ContentValues.TAG, "[CM] Asking for permission")
            GoogleSignIn.requestPermissions(
                this,
                GOOGLE_FIT_STEPS_PERMISSIONS_REQUEST_CODE,
                account,
                FITNESS_OPTIONS
            )
        }
    }

    private fun accessStepsFromGoogleFit() {
        Log.i(ContentValues.TAG, "[CM] Accessing steps from Google Fit")

        val account = GoogleSignIn.getAccountForExtension(
            requireContext(),
            FITNESS_OPTIONS
        )

        Fitness.getHistoryClient(this.activity!!, account)
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { result ->
                val totalSteps =
                    if (result.isEmpty) 0
                    else result.dataPoints[0].getValue(Field.FIELD_STEPS).asInt()

                view?.findViewById<TextView>(R.id.steps_counter)?.text = totalSteps.toString()

                Log.i(ContentValues.TAG, "[CM] It went fine, number of steps: $totalSteps")
            }
            .addOnFailureListener { e ->
                Log.i(
                    ContentValues.TAG,
                    "[CM] There was a problem getting steps: " + e.localizedMessage
                )
                Toast.makeText(context, R.string.failed_to_fetch_steps, Toast.LENGTH_LONG).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(ContentValues.TAG, "[CM] Got permission answer")
        if (requestCode == GOOGLE_FIT_STEPS_PERMISSIONS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(ContentValues.TAG, "[CM] Got permission answer: YES")
                accessStepsFromGoogleFit()
            } else {
                Log.i(ContentValues.TAG, "[CM] Got permission answer: NO")
                Toast.makeText(context, R.string.permission_not_granted, Toast.LENGTH_LONG).show()
            }
        } else if (requestCode == GOOGLE_ACCOUNT_CHOSEN) {
            if (resultCode == Activity.RESULT_OK) {
                tryToAccessStepsFromGoogleFit()
            } else {
                Toast.makeText(context, R.string.google_account_not_chosen, Toast.LENGTH_LONG).show()
            }
        }
    }


}
