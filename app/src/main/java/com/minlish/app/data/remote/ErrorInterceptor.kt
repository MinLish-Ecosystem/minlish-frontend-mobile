package com.minlish.app.data.remote

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor : Interceptor {
    private val gson = Gson()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            val errorBody = response.peekBody(Long.MAX_VALUE).string()
            val apiError = try {
                gson.fromJson(errorBody, ApiResponse::class.java)
            } catch (e: Exception) {
                null
            }

            throw AppNetworkException(
                httpCode = response.code,
                errorCode = apiError?.errorCode,
                serverMessage = apiError?.message ?: "Unknown Network Error"
            )
        }
        return response
    }
}
