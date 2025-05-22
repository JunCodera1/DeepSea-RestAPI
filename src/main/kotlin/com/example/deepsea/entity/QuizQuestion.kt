package com.example.deepsea.entity
import jakarta.persistence.*

@Entity
@Table(name = "quiz_question")
data class QuizQuestion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val type: String = "IMAGE_SELECTION",

    val prompt: String = "",

    @Column(name = "lesson_id")
    val lessonId: Long = 0,

    @ElementCollection
    @CollectionTable(name = "quiz_question_language_content", joinColumns = [JoinColumn(name = "quiz_question_id")])
    @MapKeyColumn(name = "language_code")
    val languageContent: Map<String, LanguageContent> = emptyMap(),

    @OneToMany(mappedBy = "quizQuestion", cascade = [CascadeType.ALL], orphanRemoval = true)
    val options: List<VocabularyOption> = emptyList(),

    @OneToOne
    @JoinColumn(name = "correct_answer_id")
    val correctAnswer: VocabularyOption? = null
)


@Embeddable
data class LanguageContent(
    @Column(nullable = false)
    var text: String = "",

    @Column
    var pronunciation: String = ""
)
