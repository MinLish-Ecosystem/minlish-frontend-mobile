package com.minlish.app.data.dto


data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val avatar: String? = null,
    val isVerified: Boolean,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class UpdateProfileRequest(
    val name: String,
    val avatar: String? = null
)