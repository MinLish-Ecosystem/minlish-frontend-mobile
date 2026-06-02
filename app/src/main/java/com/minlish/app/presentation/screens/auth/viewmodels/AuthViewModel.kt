package com.minlish.app.presentation.screens.auth.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

sealed interface AuthUiEvent {
    object RegisterSuccess : AuthUiEvent
    object LoginSuccess: AuthUiEvent
    object VerifyEmailSuccess: AuthUiEvent
    data class ShowError(val message: String?) : AuthUiEvent
}
class AuthViewModel: ViewModel() {
    private val repository = AuthRepository()
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val _uiEvent = Channel<AuthUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var name by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            isLoading = true

            withContext(Dispatchers.IO) {
                repository.login(email, password)
                    .onSuccess {
                        _uiEvent.send(AuthUiEvent.LoginSuccess)
                    }
                    .onFailure { e ->
                        errorMessage = e.message ?: "Login Failed"
                        _uiEvent.send(AuthUiEvent.ShowError(errorMessage))
                    }
                }
            isLoading = false
        }
    }

    fun verifyEmail(email: String, otp: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            withContext(Dispatchers.IO) {
                repository.verifyEmail(email, otp)
                    .onSuccess {
                        _uiEvent.send(AuthUiEvent.VerifyEmailSuccess)
                    }
                    .onFailure { e ->
                        errorMessage = e.message ?: "Verify Failed"
                        _uiEvent.send(AuthUiEvent.ShowError(errorMessage))
                    }
                }
            isLoading = false
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
                        _uiEvent.send(AuthUiEvent.RegisterSuccess)
                    }
                    .onFailure { e ->
                        errorMessage = e.message ?: "Register Failed"
                    }
                }
            isLoading = false
        }
    }

    fun resendVerifyEmailOtp() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            withContext(Dispatchers.IO) {
                repository.register(name, email, password)
                    .onSuccess {
                        errorMessage = null
                    }
                    .onFailure { e -> errorMessage = e.message }
                isLoading = false
            }
        }
    }
}