package com.minlish.app.data.remote

import com.minlish.app.data.dto.response.ApiResponse
import com.minlish.app.data.dto.response.DictionaryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DictionaryApi {
    @GET("dictionary/lookup")
    suspend fun lookupWord(
        @Query("word") word: String
    ): ApiResponse<DictionaryResponse>
}
