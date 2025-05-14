package com.example.deepsea.dto

data class TranslationExerciseDto(
    val id: String,
    val sourceText: String,
    val targetText: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val wordOptions: List<String>
)