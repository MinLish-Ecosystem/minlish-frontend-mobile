package com.minlish.app.presentation.screens.analytics

data class AnalyticsUiState (
    val currentStreak: Int = 12,
    val timeSpentHours: Float = 4.5f,
    val wordsMastered: Int = 342,
    val wordsThisWeek: Int = 24,
    val weeklyMinutes: List<Pair<String, Int>> = listOf(
        "M" to 20,
        "T" to 45,
        "W" to 30,
        "T" to 10,
        "F" to 25,
        "S" to 5,
        "S" to 15
    ),
    val masteryBeginner: MasteryLevel = MasteryLevel("Beginner", 150, 0.45f),
    val masteryIntermediate: MasteryLevel = MasteryLevel("Intermediate", 120, 0.35f),
    val masteryAdvanded: MasteryLevel = MasteryLevel("Advanced", 72, 0.20f),
    val activityMap: List<Float> = listOf(
        0.0f, 0.2f, 0.4f, 0.8f, 0.0f, 1.0f, 0.6f,
        0.2f, 0.0f, 0.0f, 0.4f, 0.2f, 0.8f, 1.0f,
        0.0f, 0.4f, 0.6f, 0.2f, 0.0f, 0.0f, 0.8f,
        0.6f, 0.2f, 0.0f, 1.0f, 0.4f, 0.2f, 0.0f,
        0.8f, 0.0f, 0.4f, 0.6f, 0.2f, 0.0f, 0.0f
    ),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
data class MasteryLevel(
    val label: String,
    val wordCount: Int,
    val progress: Float
)