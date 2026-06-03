package com.minlish.app.presentation.screens.auth.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

sealed interface AuthUiEvent {
    object RegisterSuccess : AuthUiEvent
    object LoginSuccess: AuthUiEvent
    object VerifyEmailSuccess: AuthUiEvent
    data class ShowError(val message: String?) : AuthUiEvent
}

data class AuthUIState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel: ViewModel() {
    private val repository = AuthRepository()

    private val _uiEvent = Channel<AuthUiEvent>()
    private val _uiState = MutableStateFlow(AuthUIState())
    val uiEvent = _uiEvent.receiveAsFlow()
    val uiState: StateFlow<AuthUIState> = _uiState.asStateFlow()
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true)  }

            withContext(Dispatchers.IO) {
                repository.login(email, password)
                    .onSuccess {
                        _uiEvent.send(AuthUiEvent.LoginSuccess)
                    }
                    .onFailure { e ->
                        val errorMessage = e.message ?: "Login Failed"
                         _uiState.update { it.copy(errorMessage = errorMessage)}
                        _uiEvent.send(AuthUiEvent.ShowError(errorMessage))
                    }
                }
            _uiState.update { it.copy(isLoading = false)  }
        }
    }

    fun verifyEmail(email: String, otp: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true)  }

            withContext(Dispatchers.IO) {
                repository.verifyEmail(email, otp)
                    .onSuccess {
                        _uiEvent.send(AuthUiEvent.VerifyEmailSuccess)
                    }
                    .onFailure { e ->
                        val errorMessage = e.message ?: "Verify Failed"
                        _uiState.update { it.copy(errorMessage = errorMessage)  }
                        _uiEvent.send(AuthUiEvent.ShowError(errorMessage))
                    }
                }
            _uiState.update { it.copy(isLoading = false)  }
        }
    }

    fun register(fullName: String, password: String, email: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true)  }

            withContext(Dispatchers.IO) {
                repository.register(fullName, email, password)
                    .onSuccess {
                        _uiEvent.send(AuthUiEvent.RegisterSuccess)
                    }
                    .onFailure { e ->
                        val errorMessage = e.message ?: "Register Failed"
                        _uiState.update { it.copy(errorMessage = errorMessage)  }
                        _uiEvent.send(AuthUiEvent.ShowError(errorMessage))
                    }
                }
            _uiState.update { it.copy(isLoading = false)  }
        }
    }

    fun resendVerifyEmailOtp() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true)  }
            val currentState = _uiState.value

            withContext(Dispatchers.IO) {
                repository.register(currentState.name, currentState.email, currentState.password)
                    .onSuccess {

                    }
                    .onFailure { e ->
                        val errorMessage = e.message ?: "Register Failed"
                        _uiState.update { it.copy(errorMessage = errorMessage)  }
                        _uiEvent.send(AuthUiEvent.ShowError(errorMessage))
                    }
                }
            _uiState.update { it.copy(isLoading = false)  }
        }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun resetViewModel() {
        _uiState.value = AuthUIState()
    }
}