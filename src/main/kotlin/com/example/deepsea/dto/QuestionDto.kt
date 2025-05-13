package com.example.deepsea.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class QuestionDto(
    @JsonProperty("id") val id: Long,
    @JsonProperty("text") val text: String,
    @JsonProperty("options") val options: List<String>, // Serialize as List
    @JsonProperty("correctAnswer") val correctAnswer: Int,
    @JsonProperty("gameMode") val gameMode: String,
    @JsonProperty("language") val language: String
)