package com.minlish.app.data.sync

import com.minlish.app.MainActivity
import com.minlish.app.data.repository.UserRepository

class SyncManager() {
    private val userRepository = UserRepository()

    suspend fun syncAll() {
        userRepository.refreshUserProfile()
    }
}