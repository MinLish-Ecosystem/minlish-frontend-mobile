package com.minlish.app.data.dto.response

import com.minlish.app.data.dto.UserDto


data class LoginData(
    val accessToken: String,
    val refreshToken: String,
    val redirectUrl: String,
    val user: UserDto
)