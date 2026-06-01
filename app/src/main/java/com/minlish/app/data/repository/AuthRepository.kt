package com.minlish.app.data.repository


import com.minlish.app.data.dto.ApiResponse
import com.minlish.app.data.dto.LoginData
import com.minlish.app.data.local.TokenManager
import com.minlish.app.data.dto.LoginRequest
import com.minlish.app.data.dto.RegisterData
import com.minlish.app.data.dto.RegisterRequest
import com.minlish.app.di.NetworkModule

class AuthRepository {
    private val authApi = NetworkModule.authApi

    suspend fun login(email: String, password: String): Result<ApiResponse<LoginData>> {
        return try {
            val response = authApi.login(LoginRequest(email.trim(), password))
            val loginData = response.data ?: throw Exception("Login data is null")
            loginData.accessToken
            TokenManager.saveTokens(
                accessToken =  loginData.accessToken,
                refreshToken = loginData.refreshToken,
                userId = loginData.user.id
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(fullName: String, email: String, password: String): Result<ApiResponse<RegisterData>> {
        return try {
            val response = authApi.register(
                RegisterRequest(
                    name = fullName.trim(),
                    password = password,
                    email = email.trim()
                )
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}