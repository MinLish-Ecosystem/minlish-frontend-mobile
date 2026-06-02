package com.minlish.app.data.mapper
import com.minlish.app.data.dto.UserDto
import com.minlish.app.data.local.entity.UserEntity

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        email = email,
        role = role ?: "user",
        avatar = avatar,
        isVerified = isVerified ?: false,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun UserEntity.toDto(): UserDto {
    return UserDto(
        id = id,
        name = name,
        email = email,
        avatar = avatar,
        role = role ?: "user",
        isVerified = isVerified ?: false
    )
}