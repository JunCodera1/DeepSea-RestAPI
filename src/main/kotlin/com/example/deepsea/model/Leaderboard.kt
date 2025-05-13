package com.example.deepsea.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "leaderboard")
data class Leaderboard(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val userId: Long,
    var score: Int,
    var rank: Int,
    val weekStartDate: LocalDate
)