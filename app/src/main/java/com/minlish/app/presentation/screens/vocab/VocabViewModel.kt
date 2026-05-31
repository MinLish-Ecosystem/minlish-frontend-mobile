package com.minlish.app.presentation.screens.vocab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.remote.VocabSetResponse
import com.minlish.app.data.repository.VocabRepository
import com.minlish.app.data.repository.VocabResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────────────────────────────────
// UI State cho Library
// ─────────────────────────────────────────────────────────────────────────────

data class LibraryUiState(
    val sets: List<VocabSetResponse> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreating: Boolean = false,
    val createSuccess: Boolean = false
)

// ─────────────────────────────────────────────────────────────────────────────
// VocabViewModel — kết nối Repository với UI
// ─────────────────────────────────────────────────────────────────────────────

class VocabViewModel(
    private val repository: VocabRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    // Khởi tạo: load danh sách ngay khi ViewModel được tạo
    init {
        loadSets()
    }

    // ── Load danh sách bộ từ ──────────────────────────────────────────────────

    fun loadSets(query: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = repository.getUserSets(query = query)) {
                is VocabResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        sets = result.data,
                        isLoading = false
                    )
                }
                is VocabResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    // ── Tạo bộ từ mới ────────────────────────────────────────────────────────

    fun createSet(
        title: String,
        description: String,
        category: String,
        isPublic: Boolean
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreating = true, errorMessage = null)

            // Map category từ UI chip sang backend enum
            val backendCategory = mapCategoryToBackend(category)

            when (val result = repository.createSet(
                name = title,
                description = description.ifBlank { null },
                category = backendCategory,
                isPublic = isPublic
            )) {
                is VocabResult.Success -> {
                    // Thêm set mới vào đầu danh sách (không cần reload toàn bộ)
                    val updatedSets = listOf(result.data) + _uiState.value.sets
                    _uiState.value = _uiState.value.copy(
                        sets = updatedSets,
                        isCreating = false,
                        createSuccess = true
                    )
                }
                is VocabResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isCreating = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    // ── Xoá bộ từ ────────────────────────────────────────────────────────────

    fun deleteSet(setId: String) {
        viewModelScope.launch {
            when (repository.deleteSet(setId)) {
                is VocabResult.Success -> {
                    // Xoá khỏi danh sách local
                    _uiState.value = _uiState.value.copy(
                        sets = _uiState.value.sets.filter { it.id != setId }
                    )
                }
                is VocabResult.Error -> {
                    // Lỗi không ảnh hưởng danh sách
                }
            }
        }
    }

    // ── Reset trạng thái sau khi navigate ────────────────────────────────────

    fun resetCreateSuccess() {
        _uiState.value = _uiState.value.copy(createSuccess = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    // ── Helper: map category chip label → backend enum ────────────────────────

    private fun mapCategoryToBackend(uiCategory: String): String = when (uiCategory) {
        "Academic" -> "Academic"
        "Business" -> "Business"
        "Travel"   -> "Travel"
        "Casual"   -> "General"
        "General"  -> "General"
        else       -> "Other"
    }
}
