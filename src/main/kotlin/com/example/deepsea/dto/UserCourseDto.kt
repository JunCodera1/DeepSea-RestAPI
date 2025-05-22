package com.example.deepsea.dto

import com.example.deepsea.enums.LanguageOption
import com.example.deepsea.enums.PathOption

data class UserCourseDto(
    val language: LanguageOption,
    val path: PathOption?
)