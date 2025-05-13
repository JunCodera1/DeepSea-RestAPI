package com.example.deepsea.dto

data class GameStartRequest(
    val userId: Long,
    val gameMode: String,
    val language: String
)