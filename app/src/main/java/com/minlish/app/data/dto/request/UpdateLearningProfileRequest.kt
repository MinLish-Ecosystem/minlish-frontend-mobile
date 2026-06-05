package com.minlish.app.data.dto.request

import com.minlish.app.data.dto.response.UserPreferencesDto

data class UpdateLearningProfileRequest(
    val learningGoal: String,
    val dailyGoal: Int,
    val reviewPerDay: Int,
    val reminderTime: String,
    val preferences: UserPreferencesDto
)
