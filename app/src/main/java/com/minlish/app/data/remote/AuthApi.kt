package com.minlish.app.data.remote

import com.minlish.app.data.dto.response.ApiResponse
import com.minlish.app.data.dto.response.LoginData
import com.minlish.app.data.dto.request.LoginRequest
import com.minlish.app.data.dto.request.ForgotPasswordRequest
import com.minlish.app.data.dto.request.GoogleSignInRequest
import com.minlish.app.data.dto.response.RegisterData
import com.minlish.app.data.dto.request.RegisterRequest
import com.minlish.app.data.dto.request.ResetPasswordRequest
import com.minlish.app.data.dto.request.VerifyEmailRequest
import com.minlish.app.data.dto.response.VerifyEmailData
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Headers


interface AuthApi {
    @Headers("No-Authentication: true")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginData>

    @Headers("No-Authentication: true")
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<RegisterData>

    @Headers("No-Authentication: true")
    @POST("auth/verify-email")
    suspend fun verifyEmail(@Body request: VerifyEmailRequest): ApiResponse<VerifyEmailData>

    @Headers("No-Authentication: true")
    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): ApiResponse<String>

    @Headers("No-Authentication: true")
    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ApiResponse<String>


    @Headers("No-Authentication: true")
    @POST("auth/google-login")
    suspend fun googleSignIn(@Body request: GoogleSignInRequest): ApiResponse<LoginData>

    @POST("auth/logout")
    suspend fun logout(): ApiResponse<String>
}