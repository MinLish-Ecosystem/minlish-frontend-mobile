package com.minlish.app.data.repository


import android.app.Activity.RESULT_CANCELED
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.minlish.app.R
import com.minlish.app.data.dto.request.ForgotPasswordRequest
import com.minlish.app.data.dto.response.ApiResponse
import com.minlish.app.data.dto.response.LoginData
import com.minlish.app.data.local.TokenManager
import com.minlish.app.data.dto.request.LoginRequest
import com.minlish.app.data.dto.response.RegisterData
import com.minlish.app.data.dto.request.RegisterRequest
import com.minlish.app.data.dto.request.ResetPasswordRequest
import com.minlish.app.data.dto.request.VerifyEmailRequest
import com.minlish.app.data.dto.response.VerifyEmailData
import com.minlish.app.di.NetworkModule
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val authApi = NetworkModule.authApi
    private val authGoogle = FirebaseAuth.getInstance()

    suspend fun login(email: String, password: String): Result<ApiResponse<LoginData>> {
        return try {
            val response = authApi.login(LoginRequest(email.trim(), password))
            val loginData = response.data ?: throw Exception("Login data is null")
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

    suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            authGoogle.signInWithCredential(credential).await()
            Result.success(Unit)
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

    suspend fun verifyEmail(email: String, otp: String): Result<ApiResponse<VerifyEmailData>> {
        return try {
            val response = authApi.verifyEmail(
                VerifyEmailRequest(
                    email = email.trim(),
                    otp = otp.trim()
                )
            )
            val verifyEmailData = response.data ?: throw Exception("Login data is null")
            TokenManager.saveTokens(
                accessToken =  verifyEmailData.accessToken,
                refreshToken = verifyEmailData.refreshToken,
                userId = verifyEmailData.user.id
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
                    newPassword = password.trim(),
                    otp = otp.trim()
                )
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<ApiResponse<String>> {
        return try {
            val response = authApi.logout()
            TokenManager.clear()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}