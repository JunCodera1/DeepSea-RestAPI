package com.example.deepsea.dto

data class UserProgressDto(
    val userId: Long,
    val sectionsProgress: List<SectionProgressDto>
)