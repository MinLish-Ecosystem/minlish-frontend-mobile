package com.minlish.app.presentation.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AnalyticsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()
    init {
        loadAnalytics()
    }
    private fun loadAnalytics(){
        viewModelScope.launch{
            _uiState.update { it.copy(isLoading = true) }
            //Them goi repo thuc te sau
            _uiState.update { it.copy(isLoading = false) }
        }
    }
    fun refresh() = loadAnalytics()
}