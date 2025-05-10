package com.example.deepsea.dto

data class SectionDto(
    val id: Long,
    val title: String,
    val description: String?,
    val color: String?,
    val darkerColor: String?,
    val image: String?,
    val level: String?,
    val orderIndex: Int,
    val unitCount: Int = 0
)
