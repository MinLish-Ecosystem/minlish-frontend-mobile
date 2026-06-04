package com.minlish.app.data.remote

import retrofit2.http.*

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)

data class VocabSetProgress(
    val masteredCount: Int,
    val masteredPct: Int,
    val learningCount: Int,
    val newCount: Int,
    val dueToday: Int,
    val lastStudied: String?
)

data class VocabSetResponse(
    val id: String,
    val name: String,
    val description: String?,
    val category: String,
    val level: String,
    val colorTheme: String,
    val tags: List<String>,
    val isPublic: Boolean,
    val totalWords: Int,
    val learnerCount: Int,
    val clonedFrom: String?,
    val createdAt: String,
    val updatedAt: String,
    val progress: VocabSetProgress? = null
)

data class WordResponse(
    val id: String,
    val setId: String,
    val word: String,
    val pronunciation: String?,
    val partOfSpeech: String?,
    val meaning: String,
    val descriptionEN: String?,
    val examples: List<String>,
    val synonyms: List<String>,
    val antonyms: List<String>,
    val collocations: List<String>,
    val relatedWords: List<String> = emptyList(),
    val note: String?,
    val imageUrl: String?,
    val audioUrl: String?,
    val status: String? = null,
    val masteryPct: Int? = null
)

data class PaginatedData<T>(
    val data: List<T>,
    val pagination: Pagination
)

data class Pagination(
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int
)

data class CreateSetRequest(
    val name: String,
    val description: String? = null,
    val category: String? = null,
    val level: String? = null,
    val colorTheme: String? = null,
    val tags: List<String>? = null,
    val isPublic: Boolean = false
)

data class AddWordRequest(
    val word: String,
    val meaning: String,
    val pronunciation: String? = null,
    val partOfSpeech: String? = null,
    val descriptionEN: String? = null,
    val examples: List<String>? = null,
    val collocations: List<String>? = null,
    val relatedWords: List<String>? = null,
    val note: String? = null
)

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
}
