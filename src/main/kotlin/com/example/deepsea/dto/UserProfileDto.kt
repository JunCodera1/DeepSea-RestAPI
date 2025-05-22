package com.example.deepsea.dto

import com.example.deepsea.enums.DailyGoalOption
import com.example.deepsea.enums.LanguageOption
import java.time.LocalDate

data class UserProfileDto(
    val name: String,
    val username: String,
    val joinDate: String,
    val courses: Set<LanguageOption>,
    val avatarUrl: String? = null, // Added avatar URL field
    val followers: Int,
    val following: Int,
    val dayStreak: Int,
    val totalXp: Int,
    val currentLeague: String,
    val topFinishes: Int,
    val friends: Set<Any>,
    val dailyGoalOption: DailyGoalOption?,
    val streakHistory: List<LocalDate> = emptyList() // Add streak history
)