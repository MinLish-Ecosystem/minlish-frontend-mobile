package com.minlish.app.data.remote

import retrofit2.http.*

// ─────────────────────────────────────────────────────────────────────────────
// Response wrapper — khớp với sendSuccess() của backend
// ─────────────────────────────────────────────────────────────────────────────

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)

// ─────────────────────────────────────────────────────────────────────────────
// Response models — khớp với VocabSetResponse, WordResponse từ backend
// ─────────────────────────────────────────────────────────────────────────────

data class VocabSetResponse(
    val id: String,
    val name: String,
    val description: String?,
    val category: String,          // "General" | "Business" | ...
    val level: String,             // "Beginner" | "Intermediate" | ...
    val colorTheme: String,        // "blue" | "emerald" | "amber" | ...
    val tags: List<String>,
    val isPublic: Boolean,
    val totalWords: Int,
    val learnerCount: Int,
    val clonedFrom: String?,
    val createdAt: String,
    val updatedAt: String
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

// ─────────────────────────────────────────────────────────────────────────────
// Request bodies — khớp với CreateSetDTO từ backend
// ─────────────────────────────────────────────────────────────────────────────

data class CreateSetRequest(
    val name: String,
    val description: String? = null,
    val category: String? = null,
    val level: String? = null,
    val colorTheme: String? = null,
    val tags: List<String>? = null,
    val isPublic: Boolean = false
)

// ─────────────────────────────────────────────────────────────────────────────
// VocabApi — Retrofit interface
// ─────────────────────────────────────────────────────────────────────────────

interface VocabApi {

    // GET /api/v1/vocab/sets — lấy danh sách bộ từ của user
    @GET("vocab/sets")
    suspend fun getUserSets(
        @Query("q") q: String? = null,
        @Query("category") category: String? = null,
        @Query("level") level: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): ApiResponse<PaginatedData<VocabSetResponse>>

    // POST /api/v1/vocab/sets — tạo bộ từ mới
    @POST("vocab/sets")
    suspend fun createSet(
        @Body body: CreateSetRequest
    ): ApiResponse<VocabSetResponse>

    // PUT /api/v1/vocab/sets/:id — cập nhật bộ từ
    @PUT("vocab/sets/{id}")
    suspend fun updateSet(
        @Path("id") id: String,
        @Body body: CreateSetRequest
    ): ApiResponse<VocabSetResponse>

    // DELETE /api/v1/vocab/sets/:id — xoá bộ từ
    @DELETE("vocab/sets/{id}")
    suspend fun deleteSet(
        @Path("id") id: String
    ): ApiResponse<Unit>
}
