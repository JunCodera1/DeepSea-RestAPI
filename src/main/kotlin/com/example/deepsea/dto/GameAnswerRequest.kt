package com.example.deepsea.dto

data class GameAnswerRequest(
    val matchId: Long,
    val questionId: Long,
    val userId: Long,
    val selectedAnswer: Int
)