package com.example.covidmind.api

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType

object Google {
    fun getAccountForExtension(
        context: Context,
        options: GoogleSignInOptionsExtension
    ): GoogleSignInAccount? {
        val account = GoogleSignIn.getAccountForExtension(
            context,
            options
        )
        return if (account.id.isNullOrEmpty()) null else account
    }

    fun hasPermissions(
        account: GoogleSignInAccount?,
        options: GoogleSignInOptionsExtension
    ): Boolean = GoogleSignIn.hasPermissions(account, options)

    fun askForGoogleAccount(activity: Activity, requestCode: Int) {
        val signInClient: GoogleSignInClient = GoogleSignIn.getClient(
            activity,
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
        val intent = signInClient.signInIntent
        activity.startActivityForResult(
            intent,
            requestCode
        )
    }

    fun askForGoogleAccount(fragment: Fragment, requestCode: Int) {
        val signInClient: GoogleSignInClient = GoogleSignIn.getClient(
            fragment.requireActivity(),
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
        val intent = signInClient.signInIntent
        fragment.startActivityForResult(
            intent,
            requestCode
        )
    }

    object Fit {
        private val GOOGLE_FIT_OPTIONS_STEPS_DATA: FitnessOptions = FitnessOptions.builder()
            .addDataType(
                DataType.TYPE_STEP_COUNT_DELTA,
                FitnessOptions.ACCESS_READ
            )
            .addDataType(
                DataType.AGGREGATE_STEP_COUNT_DELTA,
                FitnessOptions.ACCESS_READ
            )
            .build()

        fun getAccountForStepsData(context: Context): GoogleSignInAccount? {
            return getAccountForExtension(context, GOOGLE_FIT_OPTIONS_STEPS_DATA)
        }

        fun hasPermissionsForStepsData(account: GoogleSignInAccount): Boolean {
            return hasPermissions(account, GOOGLE_FIT_OPTIONS_STEPS_DATA)
        }

        fun askForStepsDataPermission(activity: Activity, requestCode: Int) {
            GoogleSignIn.requestPermissions(
                activity,
                requestCode,
                getAccountForStepsData(activity),
                GOOGLE_FIT_OPTIONS_STEPS_DATA
            )
        }

        fun askForStepsDataPermission(fragment: Fragment, requestCode: Int) {
            GoogleSignIn.requestPermissions(
                fragment,
                requestCode,
                getAccountForStepsData(fragment.requireContext()),
                GOOGLE_FIT_OPTIONS_STEPS_DATA
            )
        }
    }
}

