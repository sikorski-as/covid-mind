package com.example.covidmind.ui.moodnotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covidmind.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoodNotesFragment : Fragment() {
    private val viewModel: MoodNotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_moodnotes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val moodNoteAdapter = MoodNoteAdapter { moodNote ->
            viewModel.deleteMoodNote(moodNote)
            Toast.makeText(context, R.string.moodnotes_removed_toast, Toast.LENGTH_SHORT).show()
        }
        view.findViewById<RecyclerView>(R.id.moodnotes_recycler_view)?.also {
            it.layoutManager = LinearLayoutManager(this.context)
            it.setHasFixedSize(true)
            it.adapter = moodNoteAdapter
        }
        viewModel.allMoodNotes.observe(viewLifecycleOwner, Observer { newMoodNotes ->
            moodNoteAdapter.moodNotes = newMoodNotes.toMutableList()
        })
    }
}