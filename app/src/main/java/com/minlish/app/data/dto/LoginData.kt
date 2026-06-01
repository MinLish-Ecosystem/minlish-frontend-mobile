package com.minlish.app.data.dto


data class LoginData(
    val accessToken: String,
    val refreshToken: String,
    val redirectUrl: String,
    val user: UserDto
)