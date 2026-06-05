package com.minlish.app.data.remote

import com.minlish.app.data.dto.request.AddWordRequest
import com.minlish.app.data.dto.request.CreateSetRequest
import com.minlish.app.data.dto.response.ApiResponse
import com.minlish.app.data.dto.response.PaginatedData
import com.minlish.app.data.dto.response.VocabSetResponse
import com.minlish.app.data.dto.response.WordResponse
import retrofit2.http.*



interface VocabApi {

    @GET("vocab/sets")
    suspend fun getUserSets(
        @Query("q") q: String? = null,
        @Query("category") category: String? = null,
        @Query("level") level: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("includeProgress") includeProgress: Boolean? = null
    ): ApiResponse<PaginatedData<VocabSetResponse>>

    @GET("vocab/sets/public")
    suspend fun getPublicSets(
        @Query("q") q: String? = null,
        @Query("category") category: String? = null,
        @Query("level") level: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): ApiResponse<PaginatedData<VocabSetResponse>>

    @POST("vocab/sets")
    suspend fun createSet(
        @Body body: CreateSetRequest
    ): ApiResponse<VocabSetResponse>

    @PUT("vocab/sets/{id}")
    suspend fun updateSet(
        @Path("id") id: String,
        @Body body: CreateSetRequest
    ): ApiResponse<VocabSetResponse>

    @DELETE("vocab/sets/{id}")
    suspend fun deleteSet(
        @Path("id") id: String
    ): ApiResponse<Unit>

    @GET("vocab/sets/{id}/words")
    suspend fun getWords(
        @Path("id") setId: String,
        @Query("q") q: String? = null
    ): ApiResponse<List<WordResponse>>

    @POST("vocab/sets/{id}/words")
    suspend fun addWord(
        @Path("id") setId: String,
        @Body body: AddWordRequest
    ): ApiResponse<WordResponse>

    @DELETE("vocab/sets/{setId}/words/{wordId}")
    suspend fun deleteWord(
        @Path("setId") setId: String,
        @Path("wordId") wordId: String
    ): ApiResponse<Unit>
}
