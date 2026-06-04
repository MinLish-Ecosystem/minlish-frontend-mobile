package com.minlish.app.presentation.screens.library

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.remote.WordResponse
import com.minlish.app.data.repository.VocabRepository
import com.minlish.app.data.repository.VocabResult
import kotlinx.coroutines.launch

class WordListViewModel(
    private val repository: VocabRepository = VocabRepository()
) : ViewModel() {

    val words = mutableStateOf<List<WordResponse>>(emptyList())
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    val totalCount = mutableStateOf(0)
    val masteredCount = mutableStateOf(0)
    val learningCount = mutableStateOf(0)

    val isAdding = mutableStateOf(false)
    val addSuccess = mutableStateOf(false)
    val isLookingUp = mutableStateOf(false)

    fun loadWords(setId: String, query: String? = null) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            when (val result = repository.getWords(setId, query)) {
                is VocabResult.Success -> {
                    words.value = result.data
                    totalCount.value = result.data.size
                    masteredCount.value = result.data.count { it.status?.equals("mastered", ignoreCase = true) == true }
                    learningCount.value = result.data.count { 
                        it.status?.equals("learning", ignoreCase = true) == true || 
                        it.status?.equals("review", ignoreCase = true) == true 
                    }
                }
                is VocabResult.Error -> {
                    errorMessage.value = result.message
                }
            }
            isLoading.value = false
        }
    }

    fun addWord(
        setId: String,
        word: String,
        pronunciation: String,
        meaning: String,
        descriptionEN: String,
        example: String,
        collocation: String,
        relatedWord: String,
        note: String
    ) {
        viewModelScope.launch {
            isAdding.value = true
            errorMessage.value = null
            addSuccess.value = false

            val request = com.minlish.app.data.remote.AddWordRequest(
                word = word.trim(),
                meaning = meaning.trim(),
                pronunciation = pronunciation.trim().ifBlank { null },
                descriptionEN = descriptionEN.trim().ifBlank { null },
                examples = if (example.isNotBlank()) listOf(example.trim()) else null,
                collocations = if (collocation.isNotBlank()) listOf(collocation.trim()) else null,
                relatedWords = if (relatedWord.isNotBlank()) listOf(relatedWord.trim()) else null,
                note = note.trim().ifBlank { null }
            )

            when (val result = repository.addWord(setId, request)) {
                is VocabResult.Success -> {
                    addSuccess.value = true
                    loadWords(setId)
                }
                is VocabResult.Error -> {
                    errorMessage.value = result.message
                }
            }
            isAdding.value = false
        }
    }

    fun lookupWord(word: String, onResult: (com.minlish.app.data.remote.DictionaryResponse) -> Unit) {
        viewModelScope.launch {
            if (word.isBlank()) return@launch
            isLookingUp.value = true
            errorMessage.value = null

            when (val result = repository.lookupWord(word)) {
                is VocabResult.Success -> {
                    onResult(result.data)
                }
                is VocabResult.Error -> {
                    errorMessage.value = result.message
                }
            }
            isLookingUp.value = false
        }
    }

    fun resetAddSuccess() {
        addSuccess.value = false
    }
}
