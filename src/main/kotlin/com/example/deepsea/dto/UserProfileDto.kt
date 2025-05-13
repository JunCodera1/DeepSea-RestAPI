package com.example.deepsea.dto

import com.example.deepsea.domain.enums.DailyGoalOption
import com.example.deepsea.domain.enums.LanguageOption

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
    val dailyGoalOption: DailyGoalOption?
)