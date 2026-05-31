package com.minlish.app.di

import android.content.Context
import android.content.SharedPreferences
import com.minlish.app.data.remote.AuthApi
import com.minlish.app.data.remote.VocabApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// ─────────────────────────────────────────────────────────────────────────────
// TokenStore — lưu accessToken vào SharedPreferences
// ─────────────────────────────────────────────────────────────────────────────

object TokenStore {
    private const val PREF_NAME = "minlish_prefs"
    private const val KEY_TOKEN  = "access_token"

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        prefs?.edit()?.putString(KEY_TOKEN, token)?.apply()
    }

    fun getToken(): String? = prefs?.getString(KEY_TOKEN, null)

    fun clearToken() {
        prefs?.edit()?.remove(KEY_TOKEN)?.apply()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// NetworkModule — cấu hình Retrofit + OkHttp
// ─────────────────────────────────────────────────────────────────────────────

object NetworkModule {

    // ⚠️ Đổi thành IP máy tính của bạn khi chạy trên máy thật
    // Ví dụ: "http://192.168.1.100:3000/api/v1/"
    // Khi dùng máy ảo Android: "http://10.0.2.2:3000/api/v1/"
    private const val BASE_URL = "http://10.0.2.2:3000/api/v1/"

    // Interceptor tự động gắn "Authorization: Bearer <token>" vào mọi request
    private val authInterceptor = Interceptor { chain ->
        val token = TokenStore.getToken()
        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        chain.proceed(request)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)      // Gắn token trước
        .addInterceptor(loggingInterceptor)   // Log sau (thấy request đầy đủ)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Các API instances
    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val vocabApi: VocabApi = retrofit.create(VocabApi::class.java)
}