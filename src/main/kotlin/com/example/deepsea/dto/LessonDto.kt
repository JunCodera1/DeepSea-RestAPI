package com.example.deepsea.dto

data class LessonDto(
    val id: Long,
    val unitId: Long,
    val title: String,
    val description: String,
    val lessonType: String,
    val orderIndex: Int,
    val xpReward: Int,
    val starsReward: Int,
    val difficultyLevel: String
)