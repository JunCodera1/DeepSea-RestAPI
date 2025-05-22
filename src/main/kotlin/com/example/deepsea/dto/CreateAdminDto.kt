package com.example.deepsea.dto

data class CreateAdminDto(
    val name: String,
    val username: String,
    val password: String,
    val email: String,
    val avatarUrl: String? = null
)