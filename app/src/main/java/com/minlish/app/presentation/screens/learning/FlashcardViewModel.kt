package com.minlish.app.presentation.screens.learning

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.mapper.FlashCardMapper
import com.minlish.app.data.repository.FlashCardRepository
import com.minlish.app.presentation.screens.learning.FlashcardData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


data class FlashcardData(
    val category: String,
    val word: String,
    val phonetic: String,
    val partOfSpeech: String,
    val definition: String,
    val example: String,
    val imageUrl: Int,
)
class FlashcardViewModel(): ViewModel(){
    val flashCardRepository= FlashCardRepository()
    val flashcards = mutableStateOf<List<FlashcardData>>(emptyList())
    val currentIndex=mutableStateOf(0)
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    val selectedAnswer = mutableStateOf<String?>(null)
    val currentCard: State<FlashcardData?> = derivedStateOf {
        flashcards.value.getOrNull(currentIndex.value)
    }
    val totalCards: State<Int> = derivedStateOf {
        flashcards.value.size
    }

    fun loadFlashcardSet(setId: String? = null){
        viewModelScope.launch {
            isLoading.value=true
            errorMessage.value = null
            if (setId != null) {
                val result = withContext(Dispatchers.IO) {
                    flashCardRepository.getSetLearningQueue(setId)
                }
                result.onSuccess { queueResponse ->
                    val allCards = queueResponse.newCards + queueResponse.reviewCards
                    flashcards.value = FlashCardMapper.mapLearningCardDtoListToUiList(allCards)
                    currentIndex.value = 0
                }.onFailure { error ->
                    errorMessage.value = error.message ?: "Lỗi không xác định"
                }
            } else {
                val result = withContext(Dispatchers.IO) {
                    flashCardRepository.getLearningSet()
                }
                result.onSuccess { dto ->
                    flashcards.value = FlashCardMapper.mapToUiList(
                        dtoList = dto.flashCardSets,
                    )
                    currentIndex.value = 0
                }.onFailure { error ->
                    errorMessage.value = error.message ?: "Lỗi không xác định"
                }
            }
            isLoading.value = false
        }
    }
    fun nextCard(){
        if (currentIndex.value < flashcards.value.lastIndex) {
            currentIndex.value++
            selectedAnswer.value = null
        }
    }
    fun onAnswerSelected(answer: String) {
        selectedAnswer.value = answer
        viewModelScope.launch {
            kotlinx.coroutines.delay(300)
            nextCard()
        }
    }
}