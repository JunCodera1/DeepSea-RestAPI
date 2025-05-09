package com.example.deepsea.model
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*

@Entity
@Table(name = "quiz_question")
@JsonIgnoreProperties(ignoreUnknown = true)
data class QuizQuestion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val type: String,

    @Column(nullable = false)
    val prompt: String,

    @Column(name = "lesson_id")
    val lessonId: Long = 0,

    @ElementCollection
    @CollectionTable(name = "quiz_question_language_content", joinColumns = [JoinColumn(name = "quiz_question_id")])
    @MapKeyColumn(name = "language_code")
    val languageContent: Map<String, LanguageContent> = emptyMap(),

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "quiz_question_id")
    val options: List<VocabularyOption> = emptyList(),

    @Column(name = "correct_answer_id")
    val correctAnswerId: Long = 0
)

@Embeddable
data class LanguageContent(
    @Column(nullable = false)
    var text: String = "",

    @Column
    var pronunciation: String = ""
)
