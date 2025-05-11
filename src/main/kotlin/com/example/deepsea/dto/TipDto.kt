package com.example.deepsea.dto

data class TipDto(
    val id: Long,
    val title: String,
    val content: String,
    val orderIndex: Int = 0
)