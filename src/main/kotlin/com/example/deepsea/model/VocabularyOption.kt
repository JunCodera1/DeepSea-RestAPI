package com.example.deepsea.model

import jakarta.persistence.*

@Entity
@Table(name = "vocabulary_option")
data class VocabularyOption(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column
    val image: Int,

    @ElementCollection
    @CollectionTable(name = "vocabulary_option_language_content", joinColumns = [JoinColumn(name = "vocabulary_option_id")])
    @MapKeyColumn(name = "language_code")
    val languageContent: Map<String, LanguageContent> = emptyMap()
)