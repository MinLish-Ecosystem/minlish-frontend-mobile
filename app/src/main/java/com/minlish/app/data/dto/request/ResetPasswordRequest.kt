package com.minlish.app.data.dto.request

data class ResetPasswordRequest(
    val email: String,
    val newPassword: String,
    val otp: String
)
