package com.minlish.app.data.repository

import com.minlish.app.data.remote.LoginRequest
import com.minlish.app.data.remote.LoginResponse
import com.minlish.app.data.remote.RegisterRequest
import com.minlish.app.data.remote.RegisterResponse
import com.minlish.app.di.NetworkModule

class AuthRepository {
    private val authApi = NetworkModule.authApi
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String, fullName: String): Result<RegisterResponse> {
        return try {
            val response = authApi.register(RegisterRequest(fullName, password, email))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}