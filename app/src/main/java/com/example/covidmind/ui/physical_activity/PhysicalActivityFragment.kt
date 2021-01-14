package com.example.covidmind.ui.physical_activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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

    private val handler: Handler = Handler()
    private lateinit var runnable: Runnable

    override fun onResume() {
        super.onResume()
        viewModel.refreshStepsIfConnected()
        val refreshTime: Long = 60_000 // 60 seconds
        handler.postDelayed(
            Runnable {
                viewModel.refreshStepsIfConnected()
                handler.postDelayed(runnable, refreshTime)
            }.also { runnable = it },
            refreshTime
        )
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

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
}