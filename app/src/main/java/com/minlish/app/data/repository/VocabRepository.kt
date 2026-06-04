package com.minlish.app.data.repository

import com.minlish.app.data.remote.CreateSetRequest
import com.minlish.app.data.remote.VocabApi
import com.minlish.app.data.remote.VocabSetResponse
import com.minlish.app.data.remote.WordResponse
import com.minlish.app.di.NetworkModule

sealed class VocabResult<out T> {
    data class Success<T>(val data: T) : VocabResult<T>()
    data class Error(val message: String) : VocabResult<Nothing>()
}

class VocabRepository(private val api: VocabApi = NetworkModule.vocabApi) {

    suspend fun getUserSets(
        query: String? = null,
        page: Int = 1,
        includeProgress: Boolean? = null
    ): VocabResult<List<VocabSetResponse>> {
        return try {
            val response = api.getUserSets(
                q = query?.ifBlank { null },
                page = page,
                includeProgress = includeProgress
            )
            if (response.success && response.data != null) {
                VocabResult.Success(response.data.data)
            } else {
                VocabResult.Error(response.message)
            }
        } catch (e: Exception) {
            VocabResult.Error(e.localizedMessage ?: "Lỗi không xác định")
        }
    }

    suspend fun getWords(
        setId: String,
        query: String? = null
    ): VocabResult<List<WordResponse>> {
        return try {
            val response = api.getWords(setId = setId, q = query?.ifBlank { null })
            if (response.success && response.data != null) {
                VocabResult.Success(response.data)
            } else {
                VocabResult.Error(response.message)
            }
        } catch (e: Exception) {
            VocabResult.Error(e.localizedMessage ?: "Không thể lấy danh sách từ")
        }
    }

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

    suspend fun updateSet(
        setId: String,
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
            val response = api.updateSet(setId, body)
            if (response.success && response.data != null) {
                VocabResult.Success(response.data)
            } else {
                VocabResult.Error(response.message)
            }
        } catch (e: Exception) {
            VocabResult.Error(e.localizedMessage ?: "Không thể cập nhật bộ từ")
        }
    }

    suspend fun addWord(
        setId: String,
        request: com.minlish.app.data.remote.AddWordRequest
    ): VocabResult<WordResponse> {
        return try {
            val response = api.addWord(setId, request)
            if (response.success && response.data != null) {
                VocabResult.Success(response.data)
            } else {
                VocabResult.Error(response.message)
            }
        } catch (e: Exception) {
            VocabResult.Error(e.localizedMessage ?: "Không thể thêm từ vựng")
        }
    }

    suspend fun lookupWord(word: String): VocabResult<com.minlish.app.data.remote.DictionaryResponse> {
        return try {
            val response = NetworkModule.dictionaryApi.lookupWord(word)
            if (response.success && response.data != null) {
                VocabResult.Success(response.data)
            } else {
                VocabResult.Error(response.message)
            }
        } catch (e: Exception) {
            VocabResult.Error(e.localizedMessage ?: "Không tìm thấy từ vựng trong từ điển")
        }
    }

    suspend fun deleteWord(setId: String, wordId: String): VocabResult<Unit> {
        return try {
            val response = api.deleteWord(setId, wordId)
            if (response.success) {
                VocabResult.Success(Unit)
            } else {
                VocabResult.Error(response.message ?: "Delete failed")
            }
        } catch (e: Exception) {
            VocabResult.Error(e.message ?: "Network error")
        }
    }
}
