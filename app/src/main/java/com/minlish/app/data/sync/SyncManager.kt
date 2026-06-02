package com.minlish.app.data.sync

import android.content.Context
import com.minlish.app.data.local.TokenManager
import com.minlish.app.data.repository.UserRepository

class SyncManager(context: Context) {
    private val userRepository = UserRepository()

    suspend fun syncAll() {
        userRepository.refreshUserProfile()
    }
}