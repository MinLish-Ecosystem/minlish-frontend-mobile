package com.minlish.app.data.repository

import com.minlish.app.data.dto.UserDto
import com.minlish.app.data.dto.response.ApiResponse
import com.minlish.app.data.local.TokenManager
import com.minlish.app.data.local.dao.UserDao
import com.minlish.app.data.local.entity.UserEntity
import com.minlish.app.data.remote.UserAPI
import com.minlish.app.di.NetworkModule
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val tokenManager: TokenManager = TokenManager()
) {

    private val userApi = NetworkModule.userApi
    private val userDao = UserDao,
    fun getLocalUserProfile(): Flow<UserEntity?> {
        val userId = tokenManager.getUserId() ?: ""
        return userDao.getUserById(userId)
    }

    suspend fun refreshUserProfile(): Result<Unit> {
        try {
            val userDto = userApi.getUserProfile()
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
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}