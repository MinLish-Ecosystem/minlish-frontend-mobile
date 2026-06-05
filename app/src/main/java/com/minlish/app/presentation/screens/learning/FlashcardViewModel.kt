package com.minlish.app.presentation.screens.learning

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.dto.request.ReviewItemDto
import com.minlish.app.data.mapper.FlashCardMapper
import com.minlish.app.data.repository.FlashCardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.onSuccess


data class FlashcardData(
    val id: String,
    val setId: String,
    val category: String,
    val word: String,
    val phonetic: String,
    val partOfSpeech: String,
    val definition: String,
    val example: String,
    val imageUrl: Int,
    val audioUrl: String = ""
)
class FlashcardViewModel(): ViewModel() {
    val flashCardRepository = FlashCardRepository()
    val flashcards = mutableStateOf<List<FlashcardData>>(emptyList())
    val currentIndex = mutableStateOf(0)
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    val selectedAnswer = mutableStateOf<String?>(null)
    val currentCard: State<FlashcardData?> = derivedStateOf {
        flashcards.value.getOrNull(currentIndex.value)
    }
    val totalCards: State<Int> = derivedStateOf {
        flashcards.value.size
    }
    val isSubmittingBatch = mutableStateOf(false)
    val isCompleted = mutableStateOf(false)

    private val _pendingReviews = mutableListOf<ReviewItemDto>()
    private var cardStartTime = System.currentTimeMillis()

    fun loadFlashcardSet(setId: String? = null) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val result = withContext(Dispatchers.IO) {
                    flashCardRepository.getLearningSet()
                }
                result.onSuccess { dto ->
                    val newList = FlashCardMapper.mapToUiList(
                        dtoList = dto.flashCardSets,
                    )
                    if (newList.isEmpty()) {
                        errorMessage.value = "Không có từ nào cần ôn hôm nay"
                        flashcards.value = emptyList()
                    } else {
                        flashcards.value = newList
                        currentIndex.value = 0
                        cardStartTime = System.currentTimeMillis()
                    }
                }.onFailure { error ->
                    errorMessage.value = error.message ?: "Lỗi không xác định"
                    if (setId != null) {
                        val result = withContext(Dispatchers.IO) {
                            flashCardRepository.getSetLearningQueue(setId)
                        }
                        result.onSuccess { queueResponse ->
                            val allCards = queueResponse.newCards + queueResponse.reviewCards
                            flashcards.value =
                                FlashCardMapper.mapLearningCardDtoListToUiList(allCards)
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
                }
            } finally {
                isLoading.value = false
            }
        }
    }

    fun nextCard() {
        if (currentIndex.value < flashcards.value.lastIndex) {
            currentIndex.value++
            selectedAnswer.value = null
            cardStartTime = System.currentTimeMillis()
        } else {
            submitBatch()
        }
    }


    fun onAnswerSelected(answer: String) {
        val card = currentCard.value ?: return
        val rating = answer.lowercase()
        val timeSpent = ((System.currentTimeMillis() - cardStartTime) / 1000).toInt()
        _pendingReviews.add(
            ReviewItemDto(
                wordId = card.id,
                setId = card.setId,
                rating = rating,
                timeSpent = timeSpent,
            )
        )
        selectedAnswer.value = answer
        viewModelScope.launch {
            kotlinx.coroutines.delay(300)
            nextCard()
        }
    }

    fun submitBatch() {
        viewModelScope.launch {
            isSubmittingBatch.value = true
            errorMessage.value = null
            val reviews = _pendingReviews.toList()
            val result = withContext(Dispatchers.IO) {
                flashCardRepository.submitBatchReview(reviews)
            }
            result.onSuccess {
                _pendingReviews.clear()
                isCompleted.value = true
                android.util.Log.d("BATCH", "✅ Batch success: ${reviews.size} reviews")
            }.onFailure { error ->
                errorMessage.value = "Không thể đồng bộ: ${error.message}"
                android.util.Log.e("BATCH", "❌ Batch failed: ${error.message}")
            }
            isSubmittingBatch.value = false
        }
    }


    fun resetState() {
        _pendingReviews.clear()
        isCompleted.value = false
        flashcards.value = emptyList()
        currentIndex.value = 0
        selectedAnswer.value = null
    }
}
