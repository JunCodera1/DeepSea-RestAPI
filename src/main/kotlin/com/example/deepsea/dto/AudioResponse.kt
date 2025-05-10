package com.example.deepsea.dto

data class AudioResponse(
    val audioUrl: String,
    val word: String,
    val phonetics: String?
)