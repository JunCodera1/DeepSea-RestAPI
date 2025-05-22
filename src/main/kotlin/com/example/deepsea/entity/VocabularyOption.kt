package com.example.deepsea.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "vocabulary_option")
data class VocabularyOption(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val image: Int = 0, // hoặc URL ảnh

    @ManyToOne
    @JoinColumn(name = "quiz_question_id", insertable = false, updatable = false)
    @JsonIgnore
    val quizQuestion: QuizQuestion? = null,

    @ElementCollection
    @CollectionTable(name = "vocabulary_option_language_content", joinColumns = [JoinColumn(name = "vocabulary_option_id")])
    @MapKeyColumn(name = "language_code")
    val languageContent: Map<String, LanguageContent> = emptyMap()
)
