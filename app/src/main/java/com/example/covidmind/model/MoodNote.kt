package com.example.covidmind.model

import java.util.*

data class MoodNote(
    val moodValue: Int,
    val timestamp: Date = Date(System.currentTimeMillis()),
    var id: Int = 0
) {
    enum class DiscreteMoodLevel {
        MOOD_UNKNOWN,
        MOOD_VERY_BAD,
        MOOD_BAD,
        MOOD_NEUTRAL,
        MOOD_GOOD,
        MOOD_VERY_GOOD
    }

    fun mapNumericToDiscreteMoodLevel(): DiscreteMoodLevel =
        mapNumericToDiscreteMoodLevel(moodValue)

    companion object {
        fun mapNumericToDiscreteMoodLevel(value: Double?): DiscreteMoodLevel {
            return when {
                value == null -> DiscreteMoodLevel.MOOD_UNKNOWN
                value < 1.5 -> DiscreteMoodLevel.MOOD_VERY_BAD
                value in 1.5..2.5 -> DiscreteMoodLevel.MOOD_BAD
                value in 2.5..3.5 -> DiscreteMoodLevel.MOOD_NEUTRAL
                value in 3.5..4.5 -> DiscreteMoodLevel.MOOD_GOOD
                else -> DiscreteMoodLevel.MOOD_VERY_GOOD
            }
        }

        fun mapNumericToDiscreteMoodLevel(value: Int?): DiscreteMoodLevel =
            mapNumericToDiscreteMoodLevel(value?.toDouble())
    }
}
