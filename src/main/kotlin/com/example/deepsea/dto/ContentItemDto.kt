package com.example.deepsea.dto

data class ContentItemDto (
    val id: Long,
    val contentType: String,
    val contentKey: String?,
    val contentValue: String,
    val isCorrect: Boolean,
    val orderIndex: Int
)