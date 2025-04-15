package com.example.deepsea.dto

data class LoginDto(
    val password: String="",
    val email: String = "",
    val token: String? = null
)
