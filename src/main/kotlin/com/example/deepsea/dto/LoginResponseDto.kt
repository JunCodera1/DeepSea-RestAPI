package com.example.deepsea.dto

data class LoginResponseDto(
    val id: Long,
    val token: String,
    val username: String,
    val email: String,
    val firstLogin: Boolean,
    val profile_id: Long?,
    val role: String
)
