package com.minlish.app.presentation.screens.auth.viewmodels


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    object ForgotPasswordSuccess : ForgetPasswordUiEvent
    object ResetPasswordSuccess: ForgetPasswordUiEvent
    data class ShowError(val message: String?) : ForgetPasswordUiEvent
}

data class ForgetPasswordUiState(
    val otpValue: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val email: String = "",
    val secondsLeft: Int = 599,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class ForgetPasswordViewModel: ViewModel() {
    private val repository = AuthRepository()
    private val _uiEvent = Channel<ForgetPasswordUiEvent>()
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

    fun resetPassword() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true)  }

            withContext(Dispatchers.IO) {
                val currentState = _uiState.value
                repository.resetPassword(currentState.email, currentState.newPassword, currentState.otpValue)
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

    fun updateOtp(otp: String) {
        _uiState.update { it.copy(otpValue = otp) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(newPassword = password) }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun resetViewModel() {
        _uiState.value = ForgetPasswordUiState()
    }

    fun updateSecondsLeft(secondsLeft: Int) {
        _uiState.update { it.copy(secondsLeft = secondsLeft) }
    }

    fun decrementSeconds() {
        _uiState.update { it.copy(secondsLeft = it.secondsLeft - 1)
        }
    }
}
