package com.minlish.app.data.dto.request


data class VerifyEmailRequest (
    val email: String,
    val otp: String,
)