package com.minlish.app.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Headers

data class LoginRequest(
    val email: String,
    val password: String
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val avatar: String? = null,
    val isVerified: Boolean
)

data class LoginData(
    val accessToken: String,
    val refreshToken: String,
    val redirectUrl: String,
    val user: UserDto
)

data class RegisterRequest(
    val name: String,
    val password: String,
    val email: String
)

data class RegisterData(
    val message: String
)

interface AuthApi {
    @Headers("No-Authentication: true")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginData>

    @Headers("No-Authentication: true")
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<RegisterData>
}