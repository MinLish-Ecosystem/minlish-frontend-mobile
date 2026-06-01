package com.minlish.app.presentation.screens.auth.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    var  verifyEmailSuccess by mutableStateOf(false)
            private set

    var name by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            withContext(Dispatchers.IO) {
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
    }

    fun verifyEmail(email: String, otp: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            withContext(Dispatchers.IO) {
                repository.verifyEmail(email, otp)
                    .onSuccess {
                        verifyEmailSuccess = true
                    }
                    .onFailure { e ->
                        errorMessage = e.message ?: "Verify Failed"
                    }
                isLoading = false
            }
        }
    }

    fun register(fullName: String, password: String, email: String) {
        this.name = fullName
        this.password = password
        this.email = email

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            withContext(Dispatchers.IO) {
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

    fun resendVerifyEmailOtp() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            registerSuccess = false

            withContext(Dispatchers.IO) {
                repository.register(name, password, email)
                    .onSuccess {
                        errorMessage = null
                    }
                    .onFailure { e -> errorMessage = e.message }
                isLoading = false
            }
        }
    }
}