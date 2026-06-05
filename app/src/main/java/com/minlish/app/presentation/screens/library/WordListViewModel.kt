package com.minlish.app.presentation.screens.library

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.dto.request.AddWordRequest
import com.minlish.app.data.dto.response.DictionaryResponse
import com.minlish.app.data.dto.response.WordResponse
import com.minlish.app.data.repository.VocabRepository
import com.minlish.app.data.repository.VocabResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordListViewModel(
    private val repository: VocabRepository = VocabRepository()
) : ViewModel() {

    val words = mutableStateOf<List<WordResponse>>(emptyList())
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    val isDeleting = mutableStateOf(false)
    val deleteSuccess = mutableStateOf(false)
    val wordToDelete = mutableStateOf<WordResponse?>(null)

    val totalCount = mutableStateOf(0)
    val masteredCount = mutableStateOf(0)
    val learningCount = mutableStateOf(0)

    val isAdding = mutableStateOf(false)
    val addSuccess = mutableStateOf(false)
    val isLookingUp = mutableStateOf(false)
    val lookupResult = mutableStateOf<DictionaryResponse?>(null)

    fun loadWords(setId: String, query: String? = null) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            val result = withContext(Dispatchers.IO){
                repository.getWords(setId, query)
            }
            when (result) {
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
        parOfSpeech: String,
        meaning: String,
        descriptionEN: String,
        example: String,
        collocation: String,
        relatedWord: String,
        note: String,
        audioUrl: String? = null,
        imageUrl: String? = null,
    ) {
        viewModelScope.launch {
            isAdding.value = true
            errorMessage.value = null
            addSuccess.value = false

            val request = AddWordRequest(
                word = word.trim(),
                meaning = meaning.trim(),
                pronunciation = pronunciation.trim().ifBlank { null },
                descriptionEN = descriptionEN.trim().ifBlank { null },
                examples = if (example.isNotBlank()) listOf(example.trim()) else null,
                collocations = if (collocation.isNotBlank()) listOf(collocation.trim()) else null,
                relatedWords = if (relatedWord.isNotBlank()) listOf(relatedWord.trim()) else null,
                note = note.trim().ifBlank { null },
                audioUrl = audioUrl?.trim()?.ifBlank { null },
                imageUrl = imageUrl?.trim()?.ifBlank { null },
                partOfSpeech = parOfSpeech.trim().ifBlank { null },
            )
            val result = withContext(Dispatchers.IO){
                repository.addWord(setId, request)
            }
            when (result) {
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

    fun lookupWord(word: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (word.isBlank()) {
                errorMessage.value = "Word cannot be blank"
                return@launch
            }

            isLookingUp.value = true
            errorMessage.value = null
            lookupResult.value = null

            val result = withContext(Dispatchers.IO) {
                repository.lookupWord(word)
            }
            when (result) {
                is VocabResult.Success -> {
                    lookupResult.value = result.data
                }
                is VocabResult.Error -> {
                    errorMessage.value = result.message ?: "Lookup failed"
                    lookupResult.value = null
                }
            }

            isLookingUp.value = false
        }
    }
    fun resetAddSuccess() {
        addSuccess.value = false
        resetLookupResult()
    }

    fun resetLookupResult() {
        lookupResult.value = null
        errorMessage.value = null
    }
    fun requestDeleteWord(word: WordResponse) {
        wordToDelete.value = word
    }

    fun cancelDelete() {
        wordToDelete.value = null
    }
    fun confirmDeleteWord(setId: String) {
        val word = wordToDelete.value ?: return

        viewModelScope.launch {
            isDeleting.value = true
            errorMessage.value = null

            val result = withContext(Dispatchers.IO) {
                repository.deleteWord(setId, word.id)
            }

            when (result) {
                is VocabResult.Success -> {
                    deleteSuccess.value = true
                    wordToDelete.value = null
                    loadWords(setId)  // Refresh list
                }
                is VocabResult.Error -> {
                    errorMessage.value = result.message ?: "Delete failed"
                }
            }

            isDeleting.value = false
        }
    }

    fun resetDeleteSuccess() {
        deleteSuccess.value = false
    }
}
