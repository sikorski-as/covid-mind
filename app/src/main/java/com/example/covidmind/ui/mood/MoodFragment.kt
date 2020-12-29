package com.example.covidmind.ui.mood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.covidmind.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoodFragment : Fragment() {
    private val viewModel: MoodViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mood, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.averageMood.observe(viewLifecycleOwner, Observer {
            updateMoodImage(it)
        })

        view?.findViewById<Button>(R.id.switch_button)?.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.navigation_moodnotes)
        }
    }

    private fun updateMoodImage(averageMood: Double?) {
        val moodImageView = view?.findViewById<ImageView>(R.id.mood_image)
        val moodLabelView = view?.findViewById<TextView>(R.id.mood_label)
        val newMood = when {
            // unknown average mood
            averageMood == null ->
                Pair(R.string.mood_unknown_label, R.drawable.ic_sentiment_neutral_black_24dp)
            // very bad mood
            averageMood < 1.5 ->
                Pair(R.string.mood_very_bad_label, R.drawable.ic_sentiment_very_dissatisfied_black_24dp)
            // bad mood
            averageMood in 1.5..2.5 ->
                Pair(R.string.mood_bad_label, R.drawable.ic_sentiment_dissatisfied_black_24dp)
            // neutral mood
            averageMood in 2.5..3.5 ->
                Pair(R.string.mood_neutral_label, R.drawable.ic_sentiment_neutral_black_24dp)
            // good mood
            averageMood in 3.5..4.5 ->
                Pair(R.string.mood_good_label, R.drawable.ic_sentiment_satisfied_black_24dp)
            // very good mood
            else ->
                Pair(R.string.mood_very_good_label, R.drawable.ic_sentiment_very_satisfied_black_24dp)
        }
        moodLabelView?.setText(newMood.first)
        moodImageView?.setImageResource(newMood.second);
    }

}
