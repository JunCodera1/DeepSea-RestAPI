package com.example.deepsea.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "matches")
data class Match(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var player1Id: Long = 0,
    var player2Id: Long? = null,

    var gameMode: String = "",
    var status: String = "",

    var createdAt: LocalDateTime = LocalDateTime.now(),
    var completedAt: LocalDateTime? = null
)
