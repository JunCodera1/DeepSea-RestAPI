package com.example.deepsea.dto

data class UploadResponseDto(
    val success: Boolean,
    val url: String,
    val message: String? = null
)