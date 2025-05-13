package com.example.deepsea.dto

data class AnswerResponse(
    val isCorrect: Boolean,
    val matchCompleted: Boolean,
    val xpEarned: Int
)