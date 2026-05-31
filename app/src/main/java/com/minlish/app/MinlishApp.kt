package com.minlish.app

import android.app.Application
import com.minlish.app.data.local.TokenManager

class MinlishApp: Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
    }
}