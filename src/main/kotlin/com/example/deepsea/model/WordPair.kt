package com.example.deepsea.model

data class WordPair(
    val id: Long,
    val unitId: Long,
    val english: String,
    val japanese: String,
    val pronunciation: String
)