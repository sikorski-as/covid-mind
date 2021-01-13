package com.example.covidmind.ui.physical_activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.covidmind.R
import com.example.covidmind.api.Google
import com.example.covidmind.repos.GoogleFitRepository
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhysicalActivityFragment : Fragment() {

    private companion object {
        const val GOOGLE_FIT_STEPS_PERMISSIONS_REQUEST_CODE = 0
        const val GOOGLE_ACCOUNT_CHOSEN = 1
    }

    private val viewModel: PhysicalActivityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_physical_activity, container, false)
        val stepsButton = root.findViewById<Button>(R.id.steps_button)
        val stepsText = root.findViewById<TextView>(R.id.steps_counter)
        viewModel.stepsData.observe(viewLifecycleOwner, Observer { stepsData ->
            if (stepsData.connected) {
                stepsButton.text = resources.getString(R.string.steps_button_connected)
                stepsButton.setOnClickListener {}
            } else {
                stepsButton.text = resources.getString(R.string.steps_button_connect)
                stepsButton.setOnClickListener {
                    tryToConnect()
                }
            }

            stepsText.text = if (stepsData.steps == null) {
                resources.getString(R.string.steps_counter_placeholder)
            } else {
                stepsData.steps.toString()
            }
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.refreshStepsIfConnected()
    }

    private fun tryToConnect() {
        val status = viewModel.tryToConnect()
        when (status) {
            GoogleFitRepository.Status.NO_ACCOUNT -> {
                askForAccount()
            }
            GoogleFitRepository.Status.NO_PERMISSION -> {
                askForPermissions()
            }
            GoogleFitRepository.Status.ACTIVATED -> {
                Toast.makeText(
                    requireContext(),
                    R.string.connected_to_google_fit,
                    Toast.LENGTH_LONG
                ).show()
            }
            GoogleFitRepository.Status.ALREADY_ACTIVE -> {
                Toast.makeText(
                    requireContext(),
                    "ALREADY ACTIVE",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun askForPermissions() {
        Google.Fit.askForStepsDataPermission(this, GOOGLE_FIT_STEPS_PERMISSIONS_REQUEST_CODE)
    }

    private fun askForAccount() {
        Google.askForGoogleAccount(this, GOOGLE_ACCOUNT_CHOSEN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i("CM", "requestCode = $requestCode, resultCode: $resultCode")
        if (requestCode == GOOGLE_FIT_STEPS_PERMISSIONS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                tryToConnect()
            } else {
                Toast.makeText(context, R.string.permission_not_granted, Toast.LENGTH_LONG).show()
            }
        } else if (requestCode == GOOGLE_ACCOUNT_CHOSEN) {
            if (resultCode == Activity.RESULT_OK) {
                tryToConnect()
            } else {
                Toast.makeText(context, R.string.google_account_not_chosen, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

//    private fun tryToAccessStepsFromGoogleFit() {
//        val account = getAccount()
//        if (account == null) {
//            startSignInIntent()
//            return
//        }
//
//        if (hasPermissions(account)) {
//            accessStepsFromGoogleFit()
//        } else {
//            requestGoogleFitPermissions(account)
//        }
//    }

//    private fun accessStepsFromGoogleFit() {
//        val account = getAccount()
//        if (account == null) {
//            Toast.makeText(context, R.string.failed_to_fetch_steps, Toast.LENGTH_LONG).show()
//            return
//        }
//
//        Fitness.getHistoryClient(this.requireActivity(), account)
//            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
//            .addOnSuccessListener { result ->
//                val totalSteps =
//                    if (result.isEmpty) 0
//                    else result.dataPoints[0].getValue(Field.FIELD_STEPS).asInt()
//                viewModel.steps.value = totalSteps
//            }
//            .addOnFailureListener {
//                Toast.makeText(context, R.string.failed_to_fetch_steps, Toast.LENGTH_LONG).show()
//            }
//    }

//    private fun refreshSteps(value: Int?) {
//        val stepsText = view?.findViewById<TextView>(R.id.steps_counter)
//        val stepsButton = view?.findViewById<AppCompatButton>(R.id.steps_button)
//
//        if (value == null) {
//            stepsText?.text = resources.getString(R.string.steps_counter_placeholder)
//            stepsButton?.text = resources.getString(R.string.steps_button_connect)
//            stepsButton?.setOnClickListener {
//                tryToAccessStepsFromGoogleFit()
//            }
//        } else {
//            stepsText?.text = value.toString()
//            stepsButton?.text = resources.getString(R.string.steps_button_connected)
//            stepsButton?.setOnClickListener {}
//        }
//    }
}


//private fun PhysicalActivityFragment.hasPermissions(account: GoogleSignInAccount): Boolean {
//    return GoogleSignIn.hasPermissions(account, PhysicalActivityFragment.FITNESS_OPTIONS)
//}

//private fun PhysicalActivityFragment.startSignInIntent() {
//    val signInClient: GoogleSignInClient = GoogleSignIn.getClient(
//        this.requireActivity(),
//        GoogleSignInOptions.DEFAULT_SIGN_IN
//    )
//    val intent = signInClient.signInIntent
//    startActivityForResult(
//        intent,
//        PhysicalActivityFragment.GOOGLE_ACCOUNT_CHOSEN
//    )
//}

//private fun PhysicalActivityFragment.getAccount(): GoogleSignInAccount? {
//    val account = GoogleSignIn.getAccountForExtension(
//        requireContext(),
//        PhysicalActivityFragment.FITNESS_OPTIONS
//    )
//    return if (account.id.isNullOrEmpty()) null
//    else account
//}

//private fun PhysicalActivityFragment.requestGoogleFitPermissions(account: GoogleSignInAccount?) {
//    GoogleSignIn.requestPermissions(
//        this,
//        PhysicalActivityFragment.GOOGLE_FIT_STEPS_PERMISSIONS_REQUEST_CODE,
//        account,
//        PhysicalActivityFragment.FITNESS_OPTIONS
//    )
//}
