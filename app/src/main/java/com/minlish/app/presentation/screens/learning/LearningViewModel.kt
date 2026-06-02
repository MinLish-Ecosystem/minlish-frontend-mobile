package com.minlish.app.presentation.screens.learning

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.mapper.VocabSetMapper
import com.minlish.app.data.repository.AuthRepository
import com.minlish.app.data.repository.LearningRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


data class VocabSet(
    val id: String,
    val title: String,
    val wordCount: Int,
    val iconName: ImageVector,
    val colorHex: Long,
    val isDueToday: Boolean = false
)
// for logic
class LearningViewModel(): ViewModel(){
    val learningRepository= LearningRepository()
    val vocabSets = mutableStateOf<List<VocabSet>>(emptyList())
    val newWordsCount = mutableStateOf(0)
    val reviewsDueCount = mutableStateOf(0)
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    fun loadDashBoardData(userId: String){
        viewModelScope.launch(Dispatchers.Default){
            isLoading.value = true
            errorMessage.value = null
            val result = withContext(Dispatchers.IO) {
                learningRepository.getLearningSet(userId)
            }
            result.onSuccess { learningDto ->
                vocabSets.value = VocabSetMapper.mapToUiList(learningDto.vocabSets)
                newWordsCount.value = learningDto.newWords
                reviewsDueCount.value=learningDto.reviewsDue
            }.onFailure { error ->
                errorMessage.value = error.message ?: "Lỗi không xác định"
            }
            isLoading.value = false
        }
    }

}