package com.example.deepsea.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "word_pairs")
data class WordPair(
    @Id
    val id: Long = 0,
    @Column(name = "unit_id")
    val unitId: Long = 0,
    val english: String = "",
    val japanese: String = "",
    val pronunciation: String = "",
    val level: String = "A1"
)