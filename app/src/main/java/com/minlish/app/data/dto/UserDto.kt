package com.minlish.app.data.dto


data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val avatar: String? = null,
    val isVerified: Boolean
)

data class UpdateProfileRequest(
    val name: String,
    val avatar: String? = null
)