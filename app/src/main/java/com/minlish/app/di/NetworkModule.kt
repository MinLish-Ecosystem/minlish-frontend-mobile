package com.minlish.app.di

import com.minlish.app.data.remote.AuthApi
import com.minlish.app.data.remote.FlashCardApi
import com.minlish.app.data.remote.LearningApi
import com.minlish.app.data.remote.PracticeApi
import com.minlish.app.presentation.screens.learning.FlashCardCard
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
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val learningApi: LearningApi = retrofit.create(LearningApi::class.java)
    val flashCardApi: FlashCardApi=retrofit.create(FlashCardApi::class.java)
    val practiceApi: PracticeApi= retrofit.create(PracticeApi::class.java)
}