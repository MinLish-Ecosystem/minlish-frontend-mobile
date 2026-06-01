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
        Log.d("TokenAuthenticator", "401 detected — trying refresh...")

        val currentToken = TokenManager.getAccessToken()
        val refreshToken = TokenManager.getRefreshToken()

        if (refreshToken.isNullOrEmpty()) {
            Log.d("TokenAuthenticator", "Token invalid — logging out")
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
            Log.d("TokenAuthenticator", newTokens.toString())
            if (newTokens != null) {
                Log.d("TokenAuthenticator", "Token refreshed successfully ✅")
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
                Log.d("TokenAuthenticator", "Refresh failed — logging out ❌")
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
