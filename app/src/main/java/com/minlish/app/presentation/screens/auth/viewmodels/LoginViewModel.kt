package com.minlish.app.presentation.screens.auth.viewmodels

import android.util.Log
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

sealed interface LoginUiEvent {
    object LoginSuccess: LoginUiEvent
    object GoogleSignInSuccess: LoginUiEvent
    data class ShowError(val message: String?) : LoginUiEvent
}

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoadingGoogle: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel: ViewModel() {
    private val repository = AuthRepository()
    private val _uiEvent = Channel<LoginUiEvent>()
    private val _uiState = MutableStateFlow(LoginUIState())
    val uiEvent = _uiEvent.receiveAsFlow()
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            withContext(Dispatchers.IO) {
                repository.login(email, password)
                    .onSuccess {
                        _uiEvent.send(LoginUiEvent.LoginSuccess)
                        resetViewModel()
                    }
                    .onFailure { e ->
                        val errorMessage = e.message ?: "Login Failed"
                        _uiState.update { it.copy(errorMessage = errorMessage) }
                        _uiEvent.send(LoginUiEvent.ShowError(errorMessage))
                    }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun googleSignIn(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingGoogle = true) }
            withContext(Dispatchers.IO) {
                repository.signInWithGoogle(idToken)
                    .onSuccess {
                        Log.d("Test", "Loi")
                        resetViewModel()
                        _uiEvent.send(LoginUiEvent.GoogleSignInSuccess)
                    }
                    .onFailure { e ->
                        e.message?.let { Log.d("Test", it) }
                        val errorMessage = e.message ?: "Google Sign-In Failed"
                        _uiState.update { it.copy(errorMessage = errorMessage) }
                        _uiEvent.send(LoginUiEvent.ShowError(errorMessage))
                    }
            }
            _uiState.update { it.copy(isLoadingGoogle = false) }
        }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun resetViewModel() {
        _uiState.value = LoginUIState()
    }
}