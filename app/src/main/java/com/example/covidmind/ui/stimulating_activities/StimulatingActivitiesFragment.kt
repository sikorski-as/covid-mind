package com.example.covidmind.ui.stimulating_activities

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
import com.example.covidmind.OneShotNotificationWithContent
import com.example.covidmind.R
import com.example.covidmind.model.StimulatingActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StimulatingActivitiesFragment : Fragment() {
    private val viewModel: ActivitiesViewModel by viewModels()
    private lateinit var refreshButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var noActivitiesLabel: TextView
    private lateinit var stimulatingActivityAdapter: StimulatingActivityAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshButton = view.findViewById(R.id.stimulating_activities_refresh_button)
        recyclerView = view.findViewById(R.id.stimulating_activities_recycler_view)
        noActivitiesLabel = view.findViewById(R.id.stimulating_activities_no_data_label)
        stimulatingActivityAdapter = StimulatingActivityAdapter { stimulatingActivity ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(stimulatingActivity.link)))
        }
        refreshButton.setOnClickListener {
            viewModel.forceRefreshStimulatingActivities()
        }

        recyclerView.also {
            it.layoutManager = LinearLayoutManager(this.context)
            it.setHasFixedSize(true)
            it.adapter = stimulatingActivityAdapter
        }
        viewModel.loadingDataSuccessful.observe(
            viewLifecycleOwner,
            loadingDataSuccessfulChangedCallback()
        )
        viewModel.isLoading.observe(viewLifecycleOwner, isLoadingChangedCallback())
        viewModel.stimulatingActivities.observe(
            viewLifecycleOwner,
            stimulatingActivitiesChangedCallback()
        )
    }

    private fun loadingDataSuccessfulChangedCallback() =
        Observer<OneShotNotificationWithContent<Boolean>> {
            if (it.shouldBeHandled) {
                val wasSuccessFul = it.getContentIfNotHandled()
                val message = if (wasSuccessFul) {
                    R.string.stimulating_activities_fetch_successful
                } else {
                    R.string.stimulating_activities_fetch_failed
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }

    private fun isLoadingChangedCallback() =
        Observer<Boolean> { isLoading ->
            refreshButton.isEnabled = !isLoading
            if (isLoading) {
                refreshButton.text = resources.getText(R.string.stimulating_activities_fetching)
            } else {
                refreshButton.text = resources.getText(R.string.stimulating_activities_fetch)
            }
        }

    private fun stimulatingActivitiesChangedCallback() =
        Observer<List<StimulatingActivity>> { newStimulatingActivities ->
            val newData = newStimulatingActivities.toMutableList()
            stimulatingActivityAdapter.stimulatingActivities = newData
            if (newData.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                noActivitiesLabel.visibility = View.GONE
            } else {
                recyclerView.visibility = View.GONE
                noActivitiesLabel.visibility = View.VISIBLE
            }
        }
}