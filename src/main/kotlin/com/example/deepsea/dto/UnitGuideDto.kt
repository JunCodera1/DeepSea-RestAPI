package com.example.deepsea.dto

data class UnitGuideDto(
    val unitId: Long,
    val title: String,
    val description: String,
    val tips: List<TipDto>,
    val keyPhrases: List<KeyPhraseDto>
)