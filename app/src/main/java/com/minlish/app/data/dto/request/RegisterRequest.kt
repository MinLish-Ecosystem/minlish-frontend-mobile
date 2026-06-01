package com.minlish.app.data.dto.request

data class RegisterRequest(
    val name: String,
    val password: String,
    val email: String
)