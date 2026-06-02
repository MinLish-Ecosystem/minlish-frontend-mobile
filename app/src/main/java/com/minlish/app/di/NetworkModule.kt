package com.minlish.app.di

import com.minlish.app.data.local.TokenManager
import com.minlish.app.data.remote.AuthApi
import com.minlish.app.data.remote.StatsApi
import com.minlish.app.data.remote.TokenAuthenticator
import com.minlish.app.data.remote.UserAPI
import com.minlish.app.data.remote.interceptor.AuthInterceptor
import com.minlish.app.data.remote.interceptor.ErrorInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private const val BASE_URL = "http://10.0.2.2:3000/api/v1/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(ErrorInterceptor())
        .authenticator(TokenAuthenticator())
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val statsApi: StatsApi = retrofit.create(StatsApi::class.java)

    val userApi: UserAPI = retrofit.create(UserAPI::class.java)
}