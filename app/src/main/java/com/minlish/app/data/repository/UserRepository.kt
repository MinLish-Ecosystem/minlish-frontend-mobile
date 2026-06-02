package com.minlish.app.data.repository

import android.content.Context
import com.minlish.app.data.dto.UserDto
import com.minlish.app.data.dto.response.ApiResponse
import com.minlish.app.data.local.TokenManager
import com.minlish.app.data.local.dao.UserDao
import com.minlish.app.data.local.entity.UserEntity
import com.minlish.app.data.mapper.toEntity
import com.minlish.app.data.remote.UserAPI
import com.minlish.app.di.DatabaseModule
import com.minlish.app.di.NetworkModule
import kotlinx.coroutines.flow.Flow

class UserRepository(context: Context) {

    private val userApi = NetworkModule.userApi
    private val userDao = DatabaseModule.getUserDao(context)

    fun getUserStream(userId: String): Flow<UserEntity?> {
        return userDao.getUserById(userId)
    }

    suspend fun syncUser(userId: String): Result<Unit> {
        return try {
            val response = userApi.getUserProfile()
            if (response.success && response.data != null) {
                userDao.insertUser(response.data.toEntity())
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}