package com.minlish.app.presentation.screens.vocab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.dto.request.AddWordRequest
import com.minlish.app.data.dto.response.VocabSetResponse
import com.minlish.app.data.repository.VocabRepository
import com.minlish.app.data.repository.VocabResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class LibraryUiState(
    val sets: List<VocabSetResponse> = emptyList(),
    val publicSets: List<VocabSetResponse> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreating: Boolean = false,
    val createSuccess: Boolean = false
)

class VocabViewModel(
    private val repository: VocabRepository = VocabRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    init {
        loadSets()
        loadPublicSets()
    }

    fun loadSets(query: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = withContext(Dispatchers.IO){
                repository.getUserSets(query = query, includeProgress = true)
            }
            when (result) {
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

    fun loadPublicSets(query: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = withContext(Dispatchers.IO){
                repository.getPublicSets(query = query)
            }
            when (result) {
                is VocabResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        publicSets = result.data,
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

    fun createSet(
        title: String,
        description: String,
        category: String,
        isPublic: Boolean,
        words: List<WordEntry>
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreating = true, errorMessage = null)

            val backendCategory = mapCategoryToBackend(category)
            val result = withContext(Dispatchers.IO){
                repository.createSet(
                    name = title,
                    description = description.ifBlank { null },
                    category = backendCategory,
                    isPublic = isPublic
                )
            }
            when (result) {
                is VocabResult.Success -> {
                    val setId = result.data.id
                    var hasError = false
                    var errorMsg = ""

                    for (word in words) {
                        if (word.term.isNotBlank() && word.definition.isNotBlank()) {
                            val req = AddWordRequest(
                                word = word.term.trim(),
                                meaning = word.definition.trim()
                            )
                            val wordResult = withContext(Dispatchers.IO){
                                repository.addWord(setId, req)
                            }
                            when (wordResult) {
                                is VocabResult.Error -> {
                                    hasError = true
                                    errorMsg = wordResult.message
                                }
                                else -> {}
                            }
                        }
                    }

                    if (hasError) {
                        _uiState.value = _uiState.value.copy(
                            isCreating = false,
                            errorMessage = "Bộ từ đã tạo nhưng một số từ gặp lỗi: $errorMsg"
                        )
                    } else {
                        val updatedSets = listOf(result.data) + _uiState.value.sets
                        _uiState.value = _uiState.value.copy(
                            sets = updatedSets,
                            isCreating = false,
                            createSuccess = true
                        )
                    }

                    loadSets()
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

    fun updateSet(
        setId: String,
        title: String,
        description: String,
        category: String,
        isPublic: Boolean
    ) {
        viewModelScope.launch {
            val backendCategory = mapCategoryToBackend(category)
            val result = withContext(Dispatchers.IO){
                repository.updateSet(
                    setId = setId,
                    name = title,
                    description = description.ifBlank { null },
                    category = backendCategory,
                    isPublic = isPublic
                )
            }
            when (result ) {
                is VocabResult.Success -> {
                    val updatedSets = _uiState.value.sets.map {
                        if (it.id == setId) result.data else it
                    }
                    _uiState.value = _uiState.value.copy(sets = updatedSets)
                }
                is VocabResult.Error -> {
                    _uiState.value = _uiState.value.copy(errorMessage = result.message)
                }
            }
        }
    }

    fun deleteSet(setId: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                repository.deleteSet(setId)
            }
            when (result) {
                is VocabResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        sets = _uiState.value.sets.filter { it.id != setId }
                    )
                }
                is VocabResult.Error -> {
                    
                }
            }
        }
    }

    fun resetCreateSuccess() {
        _uiState.value = _uiState.value.copy(createSuccess = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun mapCategoryToBackend(uiCategory: String): String = when (uiCategory) {
        "Academic" -> "Academic"
        "Business" -> "Business"
        "Travel"   -> "Travel"
        "Casual"   -> "General"
        "General"  -> "General"
        else       -> "Other"
    }
}
