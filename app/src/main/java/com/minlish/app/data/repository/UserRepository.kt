package com.minlish.app.data.repository

import com.minlish.app.data.dto.DeleteFCMTokenRequest
import com.minlish.app.data.dto.LearningProfileDto
import com.minlish.app.data.dto.RegisterFCMTokenRequest
import com.minlish.app.data.dto.UpdateLearningProfileRequest
import com.minlish.app.data.dto.UpdateProfileRequest
import com.minlish.app.data.dto.UserDto
import com.minlish.app.data.dto.UserPreferencesDto
import com.minlish.app.data.local.TokenManager
import com.minlish.app.data.local.entity.UserEntity
import com.minlish.app.di.DatabaseModule
import com.minlish.app.di.NetworkModule
import kotlinx.coroutines.flow.Flow

class UserRepository {
    private val userApi = NetworkModule.userApi
    private val statsApi = NetworkModule.statsApi
    private val userDao = DatabaseModule.userDao

    // ── Local Database Caching (Upstream) ───────────────────────────────────
    fun getLocalUserProfile(): Flow<UserEntity?> {
        val userId = TokenManager.getUserId() ?: ""
        return userDao.getUserById(userId)
    }

    suspend fun refreshUserProfile(): Result<Unit> = runCatching {
        val userDto = userApi.getProfile()
        val user = UserEntity(
            id = userDto.data?.id ?: "",
            name = userDto.data?.name ?: "",
            email = userDto.data?.email ?: "",
            avatar = userDto.data?.avatar,
            role = userDto.data?.role ?: "user",
            isVerified = userDto.data?.isVerified ?: false,
            createdAt = userDto.data?.createdAt,
            updatedAt = userDto.data?.updatedAt
        )
        userDao.insertUser(user)
        Unit
    }

    // ── Network Operations (HEAD) ───────────────────────────────────────────
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