package com.example.covidmind.ui.moodnoting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.covidmind.R
import com.example.covidmind.dto.MoodNoteEntity
import com.example.covidmind.model.MoodNote
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MoodNotingFragment : Fragment() {
    private val viewModel: MoodNotingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_mood_noting, container, false)
        rootView.findViewById<ImageButton>(R.id.mood_very_bad)?.setOnClickListener(noteMoodCallback(1))
        rootView.findViewById<ImageButton>(R.id.mood_bad)?.setOnClickListener(noteMoodCallback(2))
        rootView.findViewById<ImageButton>(R.id.mood_neutral)?.setOnClickListener(noteMoodCallback(3))
        rootView.findViewById<ImageButton>(R.id.mood_good)?.setOnClickListener(noteMoodCallback(4))
        rootView.findViewById<ImageButton>(R.id.mood_very_good)?.setOnClickListener(noteMoodCallback(5))
        return rootView
    }

    private fun noteMoodCallback(value: Int): View.OnClickListener {
        return View.OnClickListener{
            viewModel.insertOrReplace(MoodNote(value))
            Toast.makeText(context, R.string.moodnoting_noted_toast, Toast.LENGTH_SHORT).show()
        }
    }

}