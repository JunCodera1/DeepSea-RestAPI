package com.example.deepsea.entity

import com.example.deepsea.dto.ContentItemDto
import jakarta.persistence.*

@Entity
@Table(name = "lesson_contents")
data class LessonContent(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "lesson_id")
    val lessonId: Long,

    @Column(name = "content_type")
    val contentType: String,

    @Column(name = "content_key")
    val contentKey: String?,

    @Column(name = "content_value")
    val contentValue: String,

    @Column(name = "correct_answer")
    val correctAnswer: Boolean = false,

    @Column(name = "order_index")
    val orderIndex: Int
)

fun LessonContent.toContentItemDto(): ContentItemDto =
    ContentItemDto(
        id = this.id,
        contentType = this.contentType,
        contentKey = this.contentKey,
        contentValue = this.contentValue,
        isCorrect = this.correctAnswer,
        orderIndex = this.orderIndex
    )
