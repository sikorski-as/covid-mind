package com.example.covidmind.ui.activities

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covidmind.R
import com.example.covidmind.ui.moodnotes.MoodNoteAdapter
import com.hadiyarajesh.flower.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StimulatingActivitiesFragment : Fragment() {
    private val viewModel: ActivitiesViewModel by viewModels()
    private lateinit var refreshButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var noActivitiesLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_activities, container, false)
        refreshButton = root.findViewById(R.id.stimulating_activities_refresh_button)
        recyclerView = root.findViewById(R.id.stimulating_activities_recycler_view)
        noActivitiesLabel = root.findViewById(R.id.stimulating_activities_no_data_label)
        refreshButton.setOnClickListener {
            viewModel.forceRefreshStimulatingActivities()
            refreshButton.isEnabled = false
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stimulatingActivityAdapter = StimulatingActivityAdapter()
        recyclerView.also {
            it.layoutManager = LinearLayoutManager(this.context)
            it.setHasFixedSize(true)
            it.adapter = stimulatingActivityAdapter
        }
        viewModel.stimulatingActivities.observe(viewLifecycleOwner,
            Observer { newStimulatingActivities ->
                Log.i("[CM]", newStimulatingActivities.toString())
                when (newStimulatingActivities.status) {
                    Resource.Status.SUCCESS -> {
                        val newData = newStimulatingActivities.data!!.toMutableList()
                        stimulatingActivityAdapter.stimulatingActivities = newData
                        if(newData.isNotEmpty()){
                            Log.i("CM", "dataset not empty")
                            recyclerView.visibility = View.VISIBLE
                            noActivitiesLabel.visibility = View.GONE
                        }
                        else{
                            Log.i("CM", "dataset empty")
                            recyclerView.visibility = View.GONE
                            noActivitiesLabel.visibility = View.VISIBLE
                        }
                        refreshButton.isEnabled = true
                    }
                    Resource.Status.LOADING -> {
                        refreshButton.isEnabled = false
                    }
                    Resource.Status.ERROR -> {
                        refreshButton.isEnabled = true
                        Toast.makeText(
                            requireContext(),
                            resources.getText(R.string.stimulating_activities_failed_to_fetch),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }
}