package com.minlish.app.presentation.screens.auth.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


sealed interface ForgetPasswordUiEvent {
    object ForgotPasswordSuccess : AuthUiEvent
    object ResetPasswordSuccess: AuthUiEvent
    data class ShowError(val message: String?) : AuthUiEvent
}
class ForgetPasswordViewModel: ViewModel() {
    private val repository = AuthRepository()
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)

    var email by mutableStateOf("")
        private set


    private val _uiEvent = Channel<AuthUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun resendForgotPasswordEmail() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            withContext(Dispatchers.IO) {
                repository.forgotPassword(email)
                    .onSuccess {
                        errorMessage = null
                    }
                    .onFailure { e -> errorMessage = e.message }
            }
            isLoading = false
        }
    }

    fun forgotPassword(email: String) {
        this.email = email
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            withContext(Dispatchers.IO) {
                repository.forgotPassword(email)
                    .onSuccess {
                        _uiEvent.send(ForgetPasswordUiEvent.ForgotPasswordSuccess)
                    }
                    .onFailure { e ->
                        errorMessage = e.message ?: "Forgot Password Failed"
                        _uiEvent.send(ForgetPasswordUiEvent.ShowError(errorMessage))
                    }
                isLoading = false
            }
        }
    }

    fun resetPassword(password: String, otp: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            withContext(Dispatchers.IO) {
                repository.resetPassword(email, password, otp)
                    .onSuccess {
                        _uiEvent.send(ForgetPasswordUiEvent.ResetPasswordSuccess)
                    }
                    .onFailure { e ->
                        errorMessage = e.message ?: "Reset Password Failed"
                        _uiEvent.send(ForgetPasswordUiEvent.ShowError(errorMessage))
                    }
                isLoading = false
            }
        }
    }

}
