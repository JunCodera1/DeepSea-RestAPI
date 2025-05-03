package com.example.deepsea.dto

import com.example.deepsea.model.LanguageOption
import com.example.deepsea.model.PathOption

data class PathRequest(
    val userId: Long,
    val language: LanguageOption,
    val path: PathOption
)
