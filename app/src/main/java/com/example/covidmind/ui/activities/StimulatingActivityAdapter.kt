package com.example.covidmind.ui.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.lujun.androidtagview.TagContainerLayout
import com.example.covidmind.R
import com.example.covidmind.model.StimulatingActivity

class StimulatingActivityAdapter(private val seeMoreButtonClicked: (StimulatingActivity) -> Unit) :
    RecyclerView.Adapter<StimulatingActivityAdapter.StimulatingActivityHolder>() {
    var stimulatingActivities = mutableListOf<StimulatingActivity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class StimulatingActivityHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.stimulating_activity_title)
        val description: TextView = itemView.findViewById(R.id.stimulating_activity_description)
        val tags: TagContainerLayout = itemView.findViewById(R.id.stimulating_activity_tags)
        val seeMoreButton: Button = itemView.findViewById(R.id.stimulating_activity_see_more_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StimulatingActivityHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.stimulating_activity_item, parent, false)
        return StimulatingActivityHolder(itemView)
    }

    override fun getItemCount(): Int {
        return stimulatingActivities.size
    }

    override fun onBindViewHolder(holder: StimulatingActivityHolder, position: Int) {
        val currentStimulatingActivity = stimulatingActivities[position]
        holder.title.text = currentStimulatingActivity.title
        holder.description.text = currentStimulatingActivity.description
        holder.tags.tags = currentStimulatingActivity.categories.map { it.name }
        holder.seeMoreButton.setOnClickListener {
            seeMoreButtonClicked(currentStimulatingActivity)
        }
    }
}