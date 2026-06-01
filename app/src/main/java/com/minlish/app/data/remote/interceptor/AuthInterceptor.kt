package com.minlish.app.data.remote.interceptor

import com.minlish.app.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        if (originalRequest.header("No-Authentication") == null) {
            TokenManager.getAccessToken()?.let { token ->
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        } else {
            requestBuilder.removeHeader("No-Authentication")
        }

        return chain.proceed(requestBuilder.build())
    }
}