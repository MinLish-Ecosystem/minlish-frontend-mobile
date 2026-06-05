package com.minlish.app.data.dto.response

data class LearningProfileDto(
    val learningGoal: String,
    val targetLevel: String?,
    val currentLevel: String?,
    val dailyGoal: Int,
    val reviewPerDay: Int,
    val reminderTime: String,
    val timezone: String,
    val preferences: UserPreferencesDto
)

data class UserPreferencesDto(
    val pushNotification: Boolean,
    val emailNotification: Boolean,
    val soundEffect: Boolean = true
)
