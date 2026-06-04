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
import com.minlish.app.data.mapper.toEntity
import com.minlish.app.di.DatabaseModule
import com.minlish.app.di.NetworkModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {
    private val userApi = NetworkModule.userApi
    private val userDao = DatabaseModule.userDao
    private val statsApi = NetworkModule.statsApi

    fun getLocalUserProfile(): Flow<UserEntity?> {
        val userId = TokenManager.getUserId() ?: ""
        return userDao.getUserById(userId)
    }

    suspend fun refreshUserProfile(): Result<Unit> = withContext(Dispatchers.IO){
        runCatching {
            val userDto = userApi.getProfile()
            if (userDto.success && userDto.data != null) {
                val user = userDto.data.toEntity()
                userDao.insertUser(user)
            }
        }
    }

    suspend fun getProfile(): Result<UserDto> = withContext(Dispatchers.IO){
        runCatching {
            userApi.getProfile().data ?: throw Exception("Profile data is null")
        }
    }

    suspend fun updateProfile(name: String, avatar: String? = null): Result<UserDto> = withContext(Dispatchers.IO){
        runCatching {
            userApi.updateProfile(UpdateProfileRequest(name, avatar)).data
                ?: throw Exception("Update profile failed")
        }
    }

    suspend fun getLearningProfile(): Result<LearningProfileDto> = withContext(Dispatchers.IO){
        runCatching {
            userApi.getLearningProfile().data
                ?: throw Exception("Learning profile is null")
        }
    }

    suspend fun updateLearningProfile(
        goal: String,
        dailyGoal: Int,
        reviewPerDay: Int,
        reminderTime: String,
        pushNotification: Boolean,
        emailNotification: Boolean
    ): Result<LearningProfileDto> = withContext(Dispatchers.IO){
        runCatching {
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
    }

    suspend fun getCurrentStreak(): Result<Int> = withContext(Dispatchers.IO){
        runCatching {
            statsApi.getDashboardStats().data?.streak?.current ?: 0
        }
    }

    suspend fun registerFCMToken(token: String, deviceId: String): Result<Unit> = withContext(Dispatchers.IO){
        runCatching {
            userApi.registerFCMToken(RegisterFCMTokenRequest(token, deviceId))
            Unit
        }
    }

    suspend fun deleteFCMToken(deviceId: String): Result<Unit> = withContext(Dispatchers.IO){
        runCatching {
            userApi.deleteFCMToken(DeleteFCMTokenRequest(deviceId))
            Unit
        }
    }
}