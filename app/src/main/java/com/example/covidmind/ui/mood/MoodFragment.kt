package com.example.covidmind.ui.mood

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation

import com.example.covidmind.R
import com.example.covidmind.repos.MoodNote
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

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
        val imageView = view?.findViewById<ImageView>(R.id.mood_image)
        val newImage = when {
            // unknown average mood
            averageMood == null ->
                R.drawable.ic_sentiment_neutral_black_24dp
            // very bad mood
            averageMood < 1.5 ->
                R.drawable.ic_sentiment_very_dissatisfied_black_24dp
            // bad mood
            averageMood in 1.5..2.5 ->
                R.drawable.ic_sentiment_dissatisfied_black_24dp
            // neutral mood
            averageMood in 2.5..3.5 ->
                R.drawable.ic_sentiment_neutral_black_24dp
            // good mood
            averageMood in 3.5..4.5 ->
                R.drawable.ic_sentiment_satisfied_black_24dp
            // very good mood
            else ->
                R.drawable.ic_sentiment_very_satisfied_black_24dp
        }
        imageView?.setImageResource(newImage);
    }

}
