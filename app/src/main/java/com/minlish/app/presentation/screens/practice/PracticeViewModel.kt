package com.minlish.app.presentation.screens.practice

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.mapper.PracticeModeMapper
import com.minlish.app.data.repository.PracticeRepository
import com.minlish.app.presentation.navigation.NavDestinations
import com.minlish.app.presentation.screens.learning.VocabSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class PracticeMode(
    val id: String,
    val title: String,
    val description: String,
    var isComingSoon: Boolean,
    val iconName: ImageVector,
    val gradientColor: Brush,
    val buttonText: String,
)
class PracticeViewModel(): ViewModel(){
    val practiceRepository= PracticeRepository()
    val practiceModes = mutableStateOf<List<PracticeMode>>(emptyList())
    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    fun loadPracticeModes(){
        isLoading.value = true
        viewModelScope.launch{
            val result = withContext(Dispatchers.IO) {
                practiceRepository.getPracticeMode()
            }
            result.onSuccess { practiceModeDto ->
                val newList = PracticeModeMapper.mapToUiList(practiceModeDto.practiceModes)
                practiceModes.value = newList.toList()
            }.onFailure { error->
                errorMessage.value = error.message ?: "Lỗi không xác định"

            }
        }
        isLoading.value = false
    }
}