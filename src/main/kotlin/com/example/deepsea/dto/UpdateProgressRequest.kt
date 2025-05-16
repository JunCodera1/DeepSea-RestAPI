package com.example.deepsea.dto

import java.time.LocalDate

data class UpdateProgressRequest(
    val dailyStreak: Int,
    val lastLogin: LocalDate
)