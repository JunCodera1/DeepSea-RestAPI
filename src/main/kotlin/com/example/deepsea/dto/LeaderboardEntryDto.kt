package com.example.deepsea.dto

data class LeaderboardEntryDto(
    val username: String,
    val name: String,
    val totalXp: Int,
    val currentLeague: String,
    val topFinishes: Int,
    val dayStreak: Int
)
