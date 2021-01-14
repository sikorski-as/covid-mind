package com.example.covidmind.ui.mood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.covidmind.R
import com.example.covidmind.model.MoodNote
import com.example.covidmind.ui.views.MoodStatusView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoodFragment : Fragment() {
    private val viewModel: MoodViewModel by viewModels()

    private lateinit var leftMoodStatusView: MoodStatusView
    private lateinit var rightMoodStatusView: MoodStatusView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mood, container, false).also {
            leftMoodStatusView = it.findViewById(R.id.left_mood_status)
            leftMoodStatusView.description = resources.getQuantityString(
                R.plurals.mood_average_label,
                viewModel.shortAverageLengthInDays,
                viewModel.shortAverageLengthInDays
            )
            rightMoodStatusView = it.findViewById(R.id.right_mood_status)
            rightMoodStatusView.description = resources.getQuantityString(
                R.plurals.mood_average_label,
                viewModel.longAverageLengthInDays,
                viewModel.longAverageLengthInDays
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.shortAverage.observe(viewLifecycleOwner, Observer { shortAverage ->
            val representation = getMoodRepresentation(shortAverage)
            leftMoodStatusView.status = resources.getString(representation.label)
            leftMoodStatusView.image = representation.image
        })
        viewModel.longAverage.observe(viewLifecycleOwner, Observer { longAverage ->
            val representation = getMoodRepresentation(longAverage)
            rightMoodStatusView.status = resources.getString(representation.label)
            rightMoodStatusView.image = representation.image
        })
    }

    private class MoodRepresentation(val label: Int, val image: Int)

    private fun getMoodRepresentation(averageMood: Double?): MoodRepresentation {
        return when (MoodNote.mapNumericToDiscreteMoodLevel(averageMood)) {
            MoodNote.DiscreteMoodLevel.MOOD_UNKNOWN ->
                MoodRepresentation(
                    R.string.mood_unknown_description,
                    R.drawable.ic_mood_neutral_24dp
                )
            MoodNote.DiscreteMoodLevel.MOOD_VERY_BAD ->
                MoodRepresentation(R.string.mood_very_bad_label, R.drawable.ic_mood_very_bad_24dp)
            MoodNote.DiscreteMoodLevel.MOOD_BAD ->
                MoodRepresentation(R.string.mood_bad_label, R.drawable.ic_mood_bad_24dp)
            MoodNote.DiscreteMoodLevel.MOOD_NEUTRAL ->
                MoodRepresentation(R.string.mood_neutral_label, R.drawable.ic_mood_neutral_24dp)
            MoodNote.DiscreteMoodLevel.MOOD_GOOD ->
                MoodRepresentation(R.string.mood_good_label, R.drawable.ic_mood_good_24dp)
            MoodNote.DiscreteMoodLevel.MOOD_VERY_GOOD ->
                MoodRepresentation(R.string.mood_very_good_label, R.drawable.ic_mood_very_good_24dp)
        }
    }

}
