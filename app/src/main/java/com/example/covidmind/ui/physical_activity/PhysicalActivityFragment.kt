package com.example.covidmind.ui.physical_activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.covidmind.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PhysicalActivityViewModel::class.java)
        viewModel.steps.observe(this, Observer { newValue ->
            refreshSteps(newValue)
        })
        refreshSteps(null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_physical_activity, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GOOGLE_FIT_STEPS_PERMISSIONS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                accessStepsFromGoogleFit()
            } else {
                Toast.makeText(context, R.string.permission_not_granted, Toast.LENGTH_LONG).show()
            }
        } else if (requestCode == GOOGLE_ACCOUNT_CHOSEN) {
            if (resultCode == Activity.RESULT_OK) {
                tryToAccessStepsFromGoogleFit()
            } else {
                Toast.makeText(context, R.string.google_account_not_chosen, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun tryToAccessStepsFromGoogleFit() {
        val account = getAccount()
        if (account == null) {
            startSignInIntent()
            return
        }

        if (hasPermissions(account)) {
            accessStepsFromGoogleFit()
        } else {
            requestGoogleFitPermissions(account)
        }
    }

    private fun accessStepsFromGoogleFit() {
        val account = getAccount()
        if (account == null) {
            Toast.makeText(context, R.string.failed_to_fetch_steps, Toast.LENGTH_LONG).show()
            return
        }

        Fitness.getHistoryClient(this.activity!!, account)
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener { result ->
                val totalSteps =
                    if (result.isEmpty) 0
                    else result.dataPoints[0].getValue(Field.FIELD_STEPS).asInt()
                viewModel.steps.value = totalSteps
            }
            .addOnFailureListener {
                Toast.makeText(context, R.string.failed_to_fetch_steps, Toast.LENGTH_LONG).show()
            }
    }

    private fun refreshSteps(value: Int?) {
        val stepsText = view?.findViewById<TextView>(R.id.steps_counter)
        val stepsButton = view?.findViewById<AppCompatButton>(R.id.steps_button)

        if (value == null) {
            stepsText?.text = resources.getString(R.string.steps_counter_placeholder)
            stepsButton?.text = resources.getString(R.string.google_fit_prompt_title)
            stepsButton?.setOnClickListener {
                tryToAccessStepsFromGoogleFit()
            }
        } else {
            stepsText?.text = value.toString()
            stepsButton?.text = resources.getString(R.string.google_fit_powered_by)
            stepsButton?.setOnClickListener {}
        }
    }
}


private fun PhysicalActivityFragment.hasPermissions(account: GoogleSignInAccount): Boolean {
    return GoogleSignIn.hasPermissions(account, PhysicalActivityFragment.FITNESS_OPTIONS)
}

private fun PhysicalActivityFragment.startSignInIntent() {
    val signInClient: GoogleSignInClient = GoogleSignIn.getClient(
        this.activity!!,
        GoogleSignInOptions.DEFAULT_SIGN_IN
    )
    val intent = signInClient.signInIntent
    startActivityForResult(
        intent,
        PhysicalActivityFragment.GOOGLE_ACCOUNT_CHOSEN
    )
}

private fun PhysicalActivityFragment.getAccount(): GoogleSignInAccount? {
    val account = GoogleSignIn.getAccountForExtension(
        requireContext(),
        PhysicalActivityFragment.FITNESS_OPTIONS
    )
    return if (account.id.isNullOrEmpty()) null
    else account
}

private fun PhysicalActivityFragment.requestGoogleFitPermissions(account: GoogleSignInAccount?) {
    GoogleSignIn.requestPermissions(
        this,
        PhysicalActivityFragment.GOOGLE_FIT_STEPS_PERMISSIONS_REQUEST_CODE,
        account,
        PhysicalActivityFragment.FITNESS_OPTIONS
    )
}
