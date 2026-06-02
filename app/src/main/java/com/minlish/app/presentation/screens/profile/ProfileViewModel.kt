package com.minlish.app.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minlish.app.data.repository.AuthRepository
import com.minlish.app.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ProfileViewModel(private val userRepository: UserRepository): ViewModel() {
    val userProfile = userRepository.getLocalUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
            viewModelScope.launch {
                userRepository.refreshUserProfile()
            }
        }
}