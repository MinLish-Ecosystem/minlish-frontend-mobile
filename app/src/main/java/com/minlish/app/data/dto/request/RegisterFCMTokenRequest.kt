package com.minlish.app.data.dto.request

data class RegisterFCMTokenRequest(
    val token: String,
    val deviceId: String,
    val platform: String = "android"
)
