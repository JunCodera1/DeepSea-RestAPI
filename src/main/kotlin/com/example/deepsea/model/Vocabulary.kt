package com.example.deepsea.model

import jakarta.persistence.*

@Entity
@Table(name = "vocabulary")
data class Vocabulary(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val nativeWord: String,

    @Column(nullable = false)
    val romaji: String,

    @Column(nullable = false)
    val english: String,

    @Column(nullable = false)
    val imageUrl: String,

    @Column(nullable = false)
    val languageCode: String,

    @Column(nullable = false)
    val difficulty: Int
)