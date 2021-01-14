package com.example.covidmind.ui.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.covidmind.R

class MoodStatusView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var statusTextView: TextView
    var status: String?
        get() = statusTextView.text.toString()
        set(value) {
            statusTextView.text = value
            reload()
        }

    private var descriptionTextView: TextView
    var description: String?
        get() = descriptionTextView.text.toString()
        set(value) {
            descriptionTextView.text = value
            reload()
        }

    private var imageView: ImageView
    private var _imageResource: Int? = null
    var image: Int?
        get() = _imageResource
        set(resourceId) {
            if(resourceId == _imageResource){
                return // no need to change and redraw
            }
            _imageResource = resourceId
            if (resourceId == null) {
                imageView.setImageDrawable(null)
            } else {
                Log.i("CM", "XDD $resourceId")
                imageView.setImageResource(resourceId)
            }
        }

    private fun reload() {
        invalidate()
        requestLayout()
    }

    init {
        val root = View.inflate(context, R.layout.view_mood_status, this)
        imageView = root.findViewById(R.id.mood_image)
        descriptionTextView = root.findViewById(R.id.mood_description)
        statusTextView = root.findViewById(R.id.mood_status)

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.MoodStatus, 0, 0)

        status = a.getString(R.styleable.MoodStatus_status)
        description = a.getString(R.styleable.MoodStatus_description)

        val imageResource = a.getResourceId(R.styleable.MoodStatus_image, -1)
        image = if (imageResource == -1) null else imageResource
        a.recycle()
    }
}