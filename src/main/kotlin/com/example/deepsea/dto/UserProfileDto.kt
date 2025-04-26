package com.example.deepsea.dto

data class UserProfileDto(
    val name: String,
    val username: String,
    val joinDate: String,
    val courses: Set<LanguageOption>,
    val followers: Int,
    val following: Int,
    val dayStreak: Int,
    val totalXp: Int,
    val currentLeague: String,
    val topFinishes: Int,
    val friends: Set<Any>
)

