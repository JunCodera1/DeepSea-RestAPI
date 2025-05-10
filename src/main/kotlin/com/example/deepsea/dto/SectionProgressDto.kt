package com.example.deepsea.dto

data class SectionProgressDto(
    val sectionId: Long,
    val title: String,
    val completedUnits: Int,
    val totalUnits: Int,
    val earnedStars: Int,
    val totalStars: Int,
    val isCompleted: Boolean
)