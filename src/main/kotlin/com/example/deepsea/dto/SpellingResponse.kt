package com.example.deepsea.dto

data class SpellingResponse(
    val word: String,
    val phoneticParts: List<String>,
    val audioUrls: List<String>
)