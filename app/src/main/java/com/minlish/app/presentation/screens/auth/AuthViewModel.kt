package com.minlish.app.presentation.screens.auth

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val repository = AuthRepository()
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var loginSuccess by mutableStateOf(false)
        private set

    var registerSuccess by mutableStateOf(false)
        private set
    fun login(email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            repository.login(email, password)
                .onSuccess {
                    loginSuccess = true
                }
                .onFailure { e ->
                    errorMessage = e.message ?: "Login Failed"
                }
            isLoading = false
        }
    }

    fun register(fullName: String, password: String, email: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            repository.register(fullName, email, password)
                .onSuccess {
                    registerSuccess = true
                }
                .onFailure { e ->
                    errorMessage = e.message ?: "Register Failed"
                }
            isLoading = false
        }
    }
}