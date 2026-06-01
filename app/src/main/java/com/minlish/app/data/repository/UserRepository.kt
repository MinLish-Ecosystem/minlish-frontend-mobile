package com.minlish.app.data.repository

import com.minlish.app.data.dto.DeleteFCMTokenRequest
import com.minlish.app.data.dto.LearningProfileDto
import com.minlish.app.data.dto.RegisterFCMTokenRequest
import com.minlish.app.data.dto.UpdateLearningProfileRequest
import com.minlish.app.data.dto.UpdateProfileRequest
import com.minlish.app.data.dto.UserDto
import com.minlish.app.data.dto.UserPreferencesDto
import com.minlish.app.di.NetworkModule

class UserRepository {
    private val userApi = NetworkModule.userApi
    private val statsApi = NetworkModule.statsApi

    suspend fun getProfile(): Result<UserDto> = runCatching {
        userApi.getProfile().data ?: throw Exception("Profile data is null")
    }

    suspend fun updateProfile(name: String, avatar: String? = null): Result<UserDto> = runCatching {
        userApi.updateProfile(UpdateProfileRequest(name, avatar)).data
            ?: throw Exception("Update profile failed")
    }

    suspend fun getLearningProfile(): Result<LearningProfileDto> = runCatching {
        userApi.getLearningProfile().data
            ?: throw Exception("Learning profile is null")
    }

    suspend fun updateLearningProfile(
        goal: String,
        dailyGoal: Int,
        reviewPerDay: Int,
        reminderTime: String,
        pushNotification: Boolean,
        emailNotification: Boolean
    ): Result<LearningProfileDto> = runCatching {
        userApi.updateLearningProfile(
            UpdateLearningProfileRequest(
                learningGoal = goal.lowercase(),
                dailyGoal = dailyGoal,
                reviewPerDay = reviewPerDay,
                reminderTime = reminderTime,
                preferences = UserPreferencesDto(
                    pushNotification = pushNotification,
                    emailNotification = emailNotification
                )
            )
        ).data ?: throw Exception("Update learning profile failed")
    }

    suspend fun getCurrentStreak(): Result<Int> = runCatching {
        statsApi.getDashboardStats().data?.streak?.current ?: 0
    }

    suspend fun registerFCMToken(token: String, deviceId: String): Result<Unit> = runCatching {
        userApi.registerFCMToken(RegisterFCMTokenRequest(token, deviceId))
        Unit
    }

    suspend fun deleteFCMToken(deviceId: String): Result<Unit> = runCatching {
        userApi.deleteFCMToken(DeleteFCMTokenRequest(deviceId))
        Unit
    }
}