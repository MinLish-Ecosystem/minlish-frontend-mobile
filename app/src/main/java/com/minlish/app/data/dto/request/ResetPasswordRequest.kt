package com.minlish.app.data.dto.request

data class ResetPasswordRequest(
    val email: String,
    val password: String,
    val otp: String
)
