package com.example.deepsea.model

data class HearingExercise(
    val id: String,
    val audio: String,
    val correctAnswer: String,
    val options: List<String>
)