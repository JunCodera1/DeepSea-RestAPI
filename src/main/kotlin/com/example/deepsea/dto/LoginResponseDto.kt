package com.example.deepsea.dto

data class LoginResponseDto(
    val id: Long?,
    val token: String? = null,
    val username: String,
    val email: String
)
