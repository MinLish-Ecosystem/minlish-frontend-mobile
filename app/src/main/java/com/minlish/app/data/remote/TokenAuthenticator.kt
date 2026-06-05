package com.minlish.app.data.remote

import android.util.Log
import com.minlish.app.data.dto.response.ApiResponse
import com.minlish.app.data.dto.response.RefreshTokenData
import com.minlish.app.data.dto.request.RefreshTokenRequest
import com.minlish.app.data.local.TokenManager
import com.minlish.app.util.SessionExpiredEvent
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Body

interface RefreshTokenApi {
    @POST("auth/refresh-token")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): ApiResponse<RefreshTokenData>
}


class TokenAuthenticator : Authenticator {
    private val BASE_URL = "http://10.0.2.2:3000/api/v1/"

    private val refreshApi: RefreshTokenApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RefreshTokenApi::class.java)
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val url = response.request.url.toString()
        if (url.contains("/auth/login") ||
            url.contains("/auth/register") ||
            url.contains("/auth/forgot-password") ||
            url.contains("auth/google-login") ||
            url.contains("/auth/reset-password") ||
            url.contains("/auth//verify-email")) {
            return null
        }

        val currentToken = TokenManager.getAccessToken()
        val refreshToken = TokenManager.getRefreshToken()

        if (refreshToken.isNullOrEmpty()) {
            handleLogOut()
            return null
        }

        synchronized(this) {
            val latestToken = TokenManager.getAccessToken()
            if (latestToken != null && latestToken != currentToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $latestToken")
                    .build()
            }

            val newTokens = runBlocking {
                try {
                    val apiResponse = refreshApi.refreshToken(RefreshTokenRequest(refreshToken))
                    apiResponse.data
                } catch (e: Exception) {
                    null
                }
            }
            if (newTokens != null) {
                val userId = TokenManager.getUserId()
                TokenManager.saveTokens(
                    accessToken = newTokens.accessToken,
                    refreshToken = newTokens.refreshToken,
                    userId = userId.toString()
                )

                return response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.accessToken}")
                    .build()
            } else {
                handleLogOut()
                return null
            }
        }
    }

    private fun handleLogOut() {
        TokenManager.clear()
        SessionExpiredEvent.emit()
    }
}
