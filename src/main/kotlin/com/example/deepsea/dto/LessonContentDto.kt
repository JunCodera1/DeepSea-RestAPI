package com.example.deepsea.dto

data class LessonContentDto(
    val lessonId: Long,
    val lessonType: String,
    val content: List<ContentItemDto>
)