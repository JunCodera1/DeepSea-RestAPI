package com.example.deepsea.dto

data class MistakeRequest(
    val userId: Long,
    val word: String,
    val correctAnswer: String,
    val userAnswer: String,
    val lessonId: Long? = null
)

data class MistakeResponse(
    val id: Long,
    val word: String,
    val correctAnswer: String,
    val userAnswer: String,
    val createdAt: String,
    val reviewCount: Int
)