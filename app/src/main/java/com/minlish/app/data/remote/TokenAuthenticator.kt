package com.minlish.app.data.remote

import com.minlish.app.data.local.TokenManager
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
    suspend fun refreshToken(@Body request: RefreshTokenRequest): ApiResponse<LoginData>
}

data class RefreshTokenRequest(val refreshToken: String)

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
        val currentToken = TokenManager.getAccessToken()
        val requestHeader = response.request.header("Authorization")
        if (requestHeader != null && requestHeader == "Bearer $currentToken") {
            TokenManager.clear()
            return null
        }

        val refreshToken = TokenManager.getRefreshToken() ?: return null

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
                TokenManager.saveTokens(
                    accessToken = newTokens.accessToken,
                    refreshToken = newTokens.refreshToken,
                    userId = newTokens.user.id
                )

                return response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.accessToken}")
                    .build()
            } else {
                TokenManager.clear()
                return null
            }
        }
    }
}
