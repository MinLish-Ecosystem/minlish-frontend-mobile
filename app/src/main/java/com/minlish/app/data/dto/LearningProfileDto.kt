package com.minlish.app.data.dto

data class LearningProfileDto (
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
data class UpdateLearningProfileRequest(
    val learningGoal: String,
    val dailyGoal: Int,
    val reviewPerDay: Int,
    val reminderTime: String,
    val preferences: UserPreferencesDto
)
data class RegisterFCMTokenRequest(
    val token: String,
    val deviceId: String,
    val platform: String = "android"
)
data class DeleteFCMTokenRequest(
    val deviceId: String
)