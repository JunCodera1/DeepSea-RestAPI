package com.example.deepsea.dto

data class LessonCompletionDto(
    val score: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val timeSpent: Int // in seconds
)