package com.minlish.app.presentation.screens.auth.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgetPasswordViewModel: ViewModel() {
    private val repository = AuthRepository()
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)

    var  forgotPasswordSuccess by mutableStateOf(false)
        private set

    var  resetPasswordSuccess by mutableStateOf(false)
        private set

    var email by mutableStateOf("")
        private set

    fun resendForgotPasswordEmail() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            forgotPasswordSuccess = false

            withContext(Dispatchers.IO) {
                repository.forgotPassword(email)
                    .onSuccess {
                        errorMessage = null
                    }
                    .onFailure { e -> errorMessage = e.message }
                isLoading = false
            }
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
                        forgotPasswordSuccess = true
                    }
                    .onFailure { e ->
                        errorMessage = e.message ?: "Forgot Password Failed"
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
                        resetPasswordSuccess = true
                    }
                    .onFailure { e ->
                        errorMessage = e.message ?: "Reset Password Failed"
                    }
                isLoading = false
            }
        }
    }

}
