package com.example.deepsea.model

data class AudioResponse(
    val audioUrl: String,
    val word: String,
    val phonetics: String?
)