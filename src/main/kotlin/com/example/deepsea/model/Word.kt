package com.example.deepsea.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "words")
data class Word(
    @Id
    val text: String = "",
    val reading: String = "",
    val meaning: String = "",
    val storyTitle: String = "",
    val level: String = "",
    val context: String = "",
    val theme: String = "",
    val exampleSentence: String = "",
    val pronunciation: String = ""
)
