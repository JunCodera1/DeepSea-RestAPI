package com.example.deepsea.dto

import java.time.LocalDate


data class UserStatsDto(
    val userId: Long,
    val totalXp: Int,
    val totalStars: Int,
    val currentStreak: Int,
    val lastActivityDate: LocalDate?,
    val levelsCompleted: Int
)