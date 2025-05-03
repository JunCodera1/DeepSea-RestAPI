package com.example.deepsea.dto

data class PathRequest(
    val userId: Long,
    val language: LanguageOption,
    val path: PathOption
)
