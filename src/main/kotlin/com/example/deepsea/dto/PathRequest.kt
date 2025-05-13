package com.example.deepsea.dto

import com.example.deepsea.domain.enums.LanguageOption
import com.example.deepsea.domain.enums.PathOption

data class PathRequest(
    val userId: Long,
    val language: LanguageOption,
    val path: PathOption
)
