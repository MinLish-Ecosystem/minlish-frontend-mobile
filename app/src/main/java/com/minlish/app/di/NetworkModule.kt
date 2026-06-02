package com.minlish.app.di

import com.minlish.app.data.remote.AuthApi
import kotlin.jvm.java
import com.minlish.app.data.remote.StatsApi
import com.minlish.app.data.remote.UserApi
import com.minlish.app.data.remote.NotificationApi
import com.minlish.app.data.remote.TokenAuthenticator
import com.minlish.app.data.remote.interceptor.AuthInterceptor
import com.minlish.app.data.remote.interceptor.ErrorInterceptor
import okhttp3.Interceptor
import com.minlish.app.data.remote.FlashCardApi
import com.minlish.app.data.remote.LearningApi
import com.minlish.app.data.remote.PracticeApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private const val BASE_URL = "https://your-backend.com/api/"
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val statsApi: StatsApi = retrofit.create(StatsApi::class.java)
    val userApi: UserApi = retrofit.create(UserApi::class.java)
    val notificationApi: NotificationApi = retrofit.create(NotificationApi::class.java)
    val learningApi: LearningApi = retrofit.create(LearningApi::class.java)
    val flashCardApi: FlashCardApi =retrofit.create(FlashCardApi::class.java)
    val practiceApi: PracticeApi = retrofit.create(PracticeApi::class.java)
}