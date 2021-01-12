package com.example.covidmind.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stimulatingActivityAdapter = StimulatingActivityAdapter { stimulatingActivity ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(stimulatingActivity.link)))
        }
        recyclerView.also {
            it.layoutManager = LinearLayoutManager(this.context)
            it.setHasFixedSize(true)
            it.adapter = stimulatingActivityAdapter
        }
        viewModel.loadingDataSuccessful.observe(viewLifecycleOwner, Observer {
            if (it.shouldBeHandled) {
                val wasSuccessFul = it.getContentIfNotHandled()
                val message = if (wasSuccessFul) {
                    R.string.stimulating_activities_fetch_successful
                } else {
                    R.string.stimulating_activities_fetch_failed
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            refreshButton.isEnabled = !isLoading
            if (isLoading) {
                refreshButton.text = resources.getText(R.string.stimulating_activities_fetching)
            } else {
                refreshButton.text = resources.getText(R.string.stimulating_activities_fetch)
            }
        })
        viewModel.stimulatingActivities.observe(viewLifecycleOwner,
            Observer { newStimulatingActivities ->
                val newData = newStimulatingActivities.toMutableList()
                stimulatingActivityAdapter.stimulatingActivities = newData
                if (newData.isNotEmpty()) {
                    recyclerView.visibility = View.VISIBLE
                    noActivitiesLabel.visibility = View.GONE
                } else {
                    recyclerView.visibility = View.GONE
                    noActivitiesLabel.visibility = View.VISIBLE
                }
            })
    }
}