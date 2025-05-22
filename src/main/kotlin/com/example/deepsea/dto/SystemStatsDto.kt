package com.example.deepsea.dto

data class SystemStatsDto(
    val totalUsers: Int,
    val adminUsers: Int,
    val regularUsers: Int,
    val activeUsersToday: Int,
    val newUsersThisWeek: Int,
    val totalProfiles: Int
)