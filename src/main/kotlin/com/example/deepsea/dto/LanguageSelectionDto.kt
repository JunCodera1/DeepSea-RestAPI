package com.example.deepsea.dto

import com.example.deepsea.enums.LanguageOption
import com.fasterxml.jackson.annotation.JsonProperty

data class LanguageSelectionDto(
    val userId: Long,
    @JsonProperty("selectedOptions")
    val languageOptions: Set<LanguageOption>
)
