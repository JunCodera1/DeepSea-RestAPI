package com.example.deepsea.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "stories")
data class Story(
    @Id
    val id: Long = 0L,
    val title: String = "",
    val level: String = "",
    @Column(columnDefinition = "TEXT")
    val content: String = ""
)