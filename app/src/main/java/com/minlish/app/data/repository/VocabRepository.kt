package com.minlish.app.data.repository

import com.minlish.app.data.remote.CreateSetRequest
import com.minlish.app.data.remote.VocabApi
import com.minlish.app.data.remote.VocabSetResponse

// ─────────────────────────────────────────────────────────────────────────────
// Result wrapper — bọc success/error thay vì throw exception ra UI
// ─────────────────────────────────────────────────────────────────────────────

sealed class VocabResult<out T> {
    data class Success<T>(val data: T) : VocabResult<T>()
    data class Error(val message: String) : VocabResult<Nothing>()
}

// ─────────────────────────────────────────────────────────────────────────────
// VocabRepository — trung gian giữa API và ViewModel
// ─────────────────────────────────────────────────────────────────────────────

class VocabRepository(private val api: VocabApi) {

    /**
     * Lấy danh sách bộ từ của user hiện tại
     * GET /api/v1/vocab/sets
     */
    suspend fun getUserSets(
        query: String? = null,
        page: Int = 1
    ): VocabResult<List<VocabSetResponse>> {
        return try {
            val response = api.getUserSets(q = query?.ifBlank { null }, page = page)
            if (response.success && response.data != null) {
                VocabResult.Success(response.data.data)
            } else {
                VocabResult.Error(response.message)
            }
        } catch (e: Exception) {
            VocabResult.Error(e.localizedMessage ?: "Lỗi không xác định")
        }
    }

    /**
     * Tạo bộ từ mới
     * POST /api/v1/vocab/sets
     */
    suspend fun createSet(
        name: String,
        description: String?,
        category: String,
        isPublic: Boolean,
        colorTheme: String = "blue"
    ): VocabResult<VocabSetResponse> {
        return try {
            val body = CreateSetRequest(
                name = name,
                description = description?.ifBlank { null },
                category = category,
                isPublic = isPublic,
                colorTheme = colorTheme
            )
            val response = api.createSet(body)
            if (response.success && response.data != null) {
                VocabResult.Success(response.data)
            } else {
                VocabResult.Error(response.message)
            }
        } catch (e: Exception) {
            VocabResult.Error(e.localizedMessage ?: "Không thể tạo bộ từ")
        }
    }

    /**
     * Xoá bộ từ
     * DELETE /api/v1/vocab/sets/:id
     */
    suspend fun deleteSet(setId: String): VocabResult<Unit> {
        return try {
            val response = api.deleteSet(setId)
            if (response.success) {
                VocabResult.Success(Unit)
            } else {
                VocabResult.Error(response.message)
            }
        } catch (e: Exception) {
            VocabResult.Error(e.localizedMessage ?: "Không thể xoá bộ từ")
        }
    }
}
