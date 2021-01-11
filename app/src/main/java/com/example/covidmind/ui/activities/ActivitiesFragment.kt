package com.example.covidmind.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.covidmind.R
import com.hadiyarajesh.flower.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivitiesFragment : Fragment() {
    private val activitiesViewModel: ActivitiesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_activities, container, false)
        root.findViewById<Button>(R.id.refresh_button).setOnClickListener {
            activitiesViewModel.forceRefreshStimulatingActivities()
        }
        activitiesViewModel.stimulatingActivities.observe(viewLifecycleOwner,
            Observer { stimulatingActivities ->
                val msg = when(stimulatingActivities.status){
                    Resource.Status.SUCCESS -> "Great success! ${stimulatingActivities.data.toString()}"
                    Resource.Status.LOADING -> "loading..."
                    Resource.Status.ERROR -> "error!"
                    else -> "???"
                }
                Log.i("[CM]", msg)
            })
        return root
    }
}