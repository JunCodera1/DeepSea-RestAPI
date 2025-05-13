package com.example.deepsea.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "matches")
data class Match(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val player1Id: Long,
    val player2Id: Long?,
    var gameMode: String,
    var status: String,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var completedAt: LocalDateTime? = null
)