package com.example.deepsea.dto

import com.example.deepsea.model.SurveyOption
import com.fasterxml.jackson.annotation.JsonProperty

data class SurveySelectionDto(
    val userId: Long,
    @JsonProperty("selectedOptions")
    val surveyOptions: Set<SurveyOption>
)
