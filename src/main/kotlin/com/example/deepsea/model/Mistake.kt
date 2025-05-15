package com.example.deepsea.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "mistakes")
data class Mistake(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val userId: Long = 0L,

    @Column(nullable = false)
    val word: String = "",

    @Column(nullable = false)
    val correctAnswer: String= "",

    @Column(nullable = false)
    val userAnswer: String= "",

    @Column
    val lessonId: Long? = null,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column
    val reviewedAt: LocalDateTime? = null,

    @Column(nullable = false)
    val reviewCount: Int = 0
)