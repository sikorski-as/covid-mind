package com.example.covidmind.ui.moodnotes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covidmind.R
import com.example.covidmind.dto.MoodNoteEntity
import com.example.covidmind.model.MoodNote
import java.text.DateFormat
import java.util.*

class MoodNoteAdapter(private val itemDeletedCallback: (MoodNote) -> Unit) :
    RecyclerView.Adapter<MoodNoteAdapter.MoodNoteHolder>() {
    var moodNotes = mutableListOf<MoodNote>()
        set(value) {
            field = value
            notifyDataSetChanged() // todo: change it to something more efficient
        }

    class MoodNoteHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moodImage: ImageView = itemView.findViewById(R.id.mood_image)
        val moodDate: TextView = itemView.findViewById(R.id.mood_date_text)
        val moodDeleteButton: ImageButton = itemView.findViewById(R.id.mood_delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodNoteHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.moodnote_item, parent, false)
        return MoodNoteHolder(itemView)
    }

    override fun getItemCount(): Int {
        return moodNotes.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MoodNoteHolder, position: Int) {
        val currentMoodNote = moodNotes[position]
        val moodValue = currentMoodNote.moodValue.toDouble()
        val imageResource = when {
            moodValue <= 1.5 -> R.drawable.ic_mood_very_bad_24dp
            moodValue in 1.5..2.5 -> R.drawable.ic_mood_bad_24dp
            moodValue in 2.5..3.5 -> R.drawable.ic_mood_neutral_24dp
            moodValue in 3.5..4.5 -> R.drawable.ic_mood_good_24dp
            else -> R.drawable.ic_mood_very_good_24dp
        }
        holder.moodImage.setImageResource(imageResource)
        val df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
        val tf = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.getDefault())
        holder.moodDate.text =
            "${df.format(currentMoodNote.timestamp)}, ${tf.format(currentMoodNote.timestamp)}"
        holder.moodDeleteButton.setOnClickListener {
            moodNotes.removeAt(position)
            notifyItemRemoved(position)
//            notifyItemRangeChanged(position, itemCount) // todo: make smooth animation
            itemDeletedCallback(currentMoodNote)
        }
    }
}