package com.example.covidmind.ui.moodnoting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.covidmind.R
import com.example.covidmind.repos.MoodNote
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MoodNotingFragment : Fragment() {
    private val viewModel: MoodNotingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mood_noting, container, false)
    }

    private fun noteMoodCallback(value: Int): View.OnClickListener {
        return View.OnClickListener{
            viewModel.insertOrReplace(MoodNote(value))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<ImageButton>(R.id.mood_very_bad)?.setOnClickListener(noteMoodCallback(1))
        view?.findViewById<ImageButton>(R.id.mood_bad)?.setOnClickListener(noteMoodCallback(2))
        view?.findViewById<ImageButton>(R.id.mood_neutral)?.setOnClickListener(noteMoodCallback(3))
        view?.findViewById<ImageButton>(R.id.mood_good)?.setOnClickListener(noteMoodCallback(4))
        view?.findViewById<ImageButton>(R.id.mood_very_good)?.setOnClickListener(noteMoodCallback(5))
    }

}