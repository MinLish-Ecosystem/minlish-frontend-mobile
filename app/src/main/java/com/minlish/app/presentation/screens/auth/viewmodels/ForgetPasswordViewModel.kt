package com.minlish.app.presentation.screens.auth.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


sealed interface ForgetPasswordUiEvent {
    object ForgotPasswordSuccess : AuthUiEvent
    object ResetPasswordSuccess: AuthUiEvent
    data class ShowError(val message: String?) : AuthUiEvent
}

data class ForgetPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class ForgetPasswordViewModel: ViewModel() {
    private val repository = AuthRepository()

    private val _uiEvent = Channel<AuthUiEvent>()
   private val _uiState = MutableStateFlow(ForgetPasswordUiState())
    val uiEvent = _uiEvent.receiveAsFlow()
    val uiState: StateFlow<ForgetPasswordUiState> = _uiState.asStateFlow()

    fun resendForgotPasswordEmail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true)  }

            withContext(Dispatchers.IO) {
                val currentState = _uiState.value
                repository.forgotPassword(currentState.email)
                    .onSuccess {

                    }
                    .onFailure { e ->
                        val errorMessage = e.message ?: "Resend Failed"
                        _uiState.update { it.copy(errorMessage = errorMessage)  }
                    }
            }
        }
        _uiState.update { it.copy(isLoading = false)  }
    }

    fun forgotPassword(email: String) {
        _uiState.update { it.copy(email = email)  }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true)  }

            withContext(Dispatchers.IO) {
                repository.forgotPassword(email)
                    .onSuccess {
                        _uiEvent.send(ForgetPasswordUiEvent.ForgotPasswordSuccess)
                    }
                    .onFailure { e ->
                        val errorMessage = e.message ?: "Forgot Password Failed"
                        _uiState.update { it.copy(errorMessage = errorMessage)  }
                        _uiEvent.send(ForgetPasswordUiEvent.ShowError(errorMessage))
                    }
            }
            _uiState.update { it.copy(isLoading = false)  }
        }
    }

    fun resetPassword(password: String, otp: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true)  }

            withContext(Dispatchers.IO) {
                val currentState = _uiState.value
                repository.resetPassword(currentState.email, password, otp)
                    .onSuccess {
                        _uiEvent.send(ForgetPasswordUiEvent.ResetPasswordSuccess)
                    }
                    .onFailure { e ->
                        val errorMessage = e.message ?: "Reset Password Failed"
                        _uiState.update { it.copy(errorMessage = errorMessage)  }
                        _uiEvent.send(ForgetPasswordUiEvent.ShowError(errorMessage))
                    }
                }
            _uiState.update { it.copy(isLoading = true)  }
        }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }
}
