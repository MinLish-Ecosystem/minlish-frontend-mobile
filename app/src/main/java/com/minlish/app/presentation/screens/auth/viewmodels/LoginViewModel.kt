package com.minlish.app.presentation.screens.auth.viewmodels

import android.content.Intent
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

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
    private var auth = FirebaseAuth.getInstance()

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
        }
    }

    fun googleSignIn(intent: Intent?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingGoogle = true) }
            try {
                val account = GoogleSignIn
                    .getSignedInAccountFromIntent(intent)
                    .getResult(ApiException::class.java)
                val googleCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                val idToken = withContext(Dispatchers.IO) {
                    auth.signInWithCredential(googleCredential).await()
                    auth.currentUser?.getIdToken(true)?.await()?.token
                        ?: throw Exception("idToken is null")
                }
                withContext(Dispatchers.IO) {
                    repository.signInWithGoogle(idToken)
                        .onSuccess {
                            _uiEvent.send(LoginUiEvent.GoogleSignInSuccess)
                            resetViewModel()
                        }
                        .onFailure { e ->
                            e.message?.let { Log.d("Test", it) }
                            val errorMessage = e.message ?: "Google Sign-In Failed"
                            _uiState.update { it.copy(errorMessage = errorMessage) }
                            _uiEvent.send(LoginUiEvent.ShowError(errorMessage))
                        }
                }
                _uiState.update { it.copy(isLoadingGoogle = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingGoogle = false) }
                _uiEvent.send(LoginUiEvent.ShowError(e.message))
            }
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