package com.example.deepsea.dto

data class VocabularyDto(
    val id: Long,
    val native: String,
    val romaji: String,
    val english: String,
    val imageUrl: String
)