package com.example.deepsea.dto

import java.time.LocalDate

data class UserDto(
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val role: String,
    val avatarUrl: String?,
    val firstLogin: Boolean,
    val lastLogin: LocalDate?,
    val createdAt: LocalDate?,
    val profile: UserProfileDto?
)