package com.minlish.app.data.remote

import com.minlish.app.data.dto.ApiResponse
import com.minlish.app.data.dto.LoginData
import com.minlish.app.data.dto.LoginRequest
import com.minlish.app.data.dto.RegisterData
import com.minlish.app.data.dto.RegisterRequest
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
}