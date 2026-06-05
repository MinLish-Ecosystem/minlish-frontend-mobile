package com.minlish.app.presentation.screens.auth.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

sealed interface RegisterUiEvent {
    object RegisterSuccess : RegisterUiEvent
    object VerifyEmailSuccess: RegisterUiEvent
    data class ShowError(val message: String?) : RegisterUiEvent
}

data class RegisterUIState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val otpValue: String = "",
    val secondsLeft: Int = 599,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class RegisterViewModel: ViewModel() {
    private val repository = AuthRepository()

    private val _uiEvent = Channel<RegisterUiEvent>()
    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiEvent = _uiEvent.receiveAsFlow()
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    fun verifyEmail(email: String, otp: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true)  }

            withContext(Dispatchers.IO) {
                repository.verifyEmail(email, otp)
                    .onSuccess {
                        _uiEvent.send(RegisterUiEvent.VerifyEmailSuccess)
                        resetViewModel()
                    }
                    .onFailure { e ->
                        val errorMessage = e.message ?: "Verify Failed"
                        _uiState.update { it.copy(errorMessage = errorMessage)  }
                        _uiEvent.send(RegisterUiEvent.ShowError(errorMessage))
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
                        _uiEvent.send(RegisterUiEvent.RegisterSuccess)
                    }
                    .onFailure { e ->
                        val errorMessage = e.message ?: "Register Failed"
                        _uiState.update { it.copy(errorMessage = errorMessage)  }
                        _uiEvent.send(RegisterUiEvent.ShowError(errorMessage))
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
                        _uiEvent.send(RegisterUiEvent.ShowError(errorMessage))
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

    fun updateOtp(otpValue: String) {
        _uiState.update { it.copy(otpValue = otpValue) }
    }

    fun updateSecondsLeft(secondsLeft: Int) {
        _uiState.update { it.copy(secondsLeft = secondsLeft) }
    }

    fun decrementSeconds() {
        _uiState.update { it.copy(secondsLeft = it.secondsLeft - 1)
        }
    }

    fun resetViewModel() {
        _uiState.value = RegisterUIState()
    }
}