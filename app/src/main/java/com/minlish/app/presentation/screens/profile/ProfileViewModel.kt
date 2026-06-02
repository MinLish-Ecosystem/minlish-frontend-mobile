package com.minlish.app.presentation.screens.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.local.TokenManager
import com.minlish.app.data.local.entity.UserEntity
import com.minlish.app.data.repository.AuthRepository
import com.minlish.app.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ProfileViewModel(application: Application): AndroidViewModel(application) {
    private val userRepository = UserRepository(application)
    val user: StateFlow<UserEntity?> = userRepository
        .getUserStream(TokenManager.getUserId() ?: "")
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}