package com.example.deepsea.dto


data class UnitProgressDto(
    val unitId: Long,
    val totalLessons: Int,
    val completedLessons: Int,
    val earnedStars: Int,
    val totalStars: Int,
    val isCompleted: Boolean,
    val lastCompletedLessonId: Long?
)