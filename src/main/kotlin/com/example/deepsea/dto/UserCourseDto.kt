package com.example.deepsea.dto

import com.example.deepsea.domain.enums.LanguageOption
import com.example.deepsea.domain.enums.PathOption

data class UserCourseDto(
    val language: LanguageOption,
    val path: PathOption?
)