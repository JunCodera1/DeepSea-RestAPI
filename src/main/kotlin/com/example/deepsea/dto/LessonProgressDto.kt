package com.example.deepsea.dto

data class LessonProgressDto(
    val lessonId: Long,
    val completed: Boolean,
    val starsEarned: Int,
    val xpEarned: Int,
    val nextLessonId: Long?
)