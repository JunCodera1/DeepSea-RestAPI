package com.example.deepsea.dto

import java.time.LocalDate

data class UserProgressDto(
    val userId: Long,
    val lastLogin: LocalDate? = null,
    val sectionsProgress: List<SectionProgressDto>
)