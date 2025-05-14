package com.example.deepsea.model

import jakarta.persistence.*

@Entity
@Table(name = "translation_exercises")
data class TranslationExercise(
    @Id
    val id: String = "",

    @Column(name = "source_text", nullable = false)
    val sourceText: String = "",

    @Column(name = "target_text", nullable = false)
    val targetText: String = "",

    @Column(name = "source_language", nullable = false)
    val sourceLanguage: String = "",

    @Column(name = "target_language", nullable = false)
    val targetLanguage: String = "",

    @ElementCollection
    @CollectionTable(
        name = "translation_exercise_options",
        joinColumns = [JoinColumn(name = "exercise_id")]
    )
    @Column(name = "word_option")
    val wordOptions: List<String> = emptyList()
)
