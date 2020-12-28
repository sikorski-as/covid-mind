package com.example.covidmind.ui.mood

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

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
        viewModel.notedToday.observe(viewLifecycleOwner, Observer {
            if(it){
                view?.findViewById<TextView>(R.id.mood_prompt_text)?.text = "You're doing great!"
            }
        })
        view?.findViewById<ImageButton>(R.id.mood_very_good)?.setOnClickListener {
            viewModel.insertOrReplace(MoodNote(moodValue = 5, timestamp = LocalDate.now()))
        }
    }

}
