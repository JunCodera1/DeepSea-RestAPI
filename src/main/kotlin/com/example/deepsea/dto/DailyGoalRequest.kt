package com.example.deepsea.dto

import com.example.deepsea.domain.enums.DailyGoalOption

data class DailyGoalRequest(
    val goal: DailyGoalOption
)
