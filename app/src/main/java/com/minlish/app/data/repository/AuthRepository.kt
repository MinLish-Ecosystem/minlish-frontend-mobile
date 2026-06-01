package com.minlish.app.data.repository


import com.minlish.app.data.dto.request.ForgotPasswordRequest
import com.minlish.app.data.dto.response.ApiResponse
import com.minlish.app.data.dto.response.LoginData
import com.minlish.app.data.local.TokenManager
import com.minlish.app.data.dto.request.LoginRequest
import com.minlish.app.data.dto.response.RegisterData
import com.minlish.app.data.dto.request.RegisterRequest
import com.minlish.app.data.dto.request.ResetPasswordRequest
import com.minlish.app.data.dto.request.VerifyEmailRequest
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

    suspend fun verifyEmail(email: String, otp: String): Result<ApiResponse<String>> {
        return try {
            val response = authApi.verifyEmail(
                VerifyEmailRequest(
                    email = email.trim(),
                    otp = otp.trim()
                )
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun forgotPassword(email: String): Result<ApiResponse<String>> {
        return try {
            val response = authApi.forgotPassword(
                ForgotPasswordRequest(
                    email = email.trim(),
                )
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(email: String, password: String, otp: String): Result<ApiResponse<String>> {
        return try {
            val response = authApi.resetPassword(
                ResetPasswordRequest(
                    email = email.trim(),
                    password = password.trim(),
                    otp = otp.trim()
                )
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}