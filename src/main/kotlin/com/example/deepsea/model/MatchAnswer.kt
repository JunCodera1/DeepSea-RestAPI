package com.example.deepsea.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "match_answers")
data class MatchAnswer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "match_id")
    val matchId: Long,

    @Column(name = "question_id")
    val questionId: Long,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "selected_answer")
    val selectedAnswer: Int,

    @Column(name = "is_correct")
    val isCorrect: Boolean,

    @Column(name = "answered_at")
    val answeredAt: LocalDateTime = LocalDateTime.now()
)