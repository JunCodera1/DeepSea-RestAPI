package com.example.deepsea.dto

data class UnitDto(
    val id: Long,
    val sectionId: Long,
    val title: String,
    val description: String,
    val color: String,
    val darkerColor: String,
    val image: String,
    val orderIndex: Int,
    val starsRequired: Int,
    val lessonCount: Int = 0
)