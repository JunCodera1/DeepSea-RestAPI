package com.example.deepsea.model

import jakarta.persistence.*

@Entity
@Table(name = "questions")
data class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val text: String,
    @Column(columnDefinition = "jsonb")
    val options: String,
    val correctAnswer: Int,
    val gameMode: String,
    val language: String
)
