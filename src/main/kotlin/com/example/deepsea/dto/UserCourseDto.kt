package com.example.deepsea.dto

import com.example.deepsea.model.LanguageOption
import com.example.deepsea.model.PathOption

data class UserCourseDto(
    val language: LanguageOption,
    val path: PathOption?
)