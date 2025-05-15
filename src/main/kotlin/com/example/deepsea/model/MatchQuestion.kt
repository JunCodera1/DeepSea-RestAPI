package com.example.deepsea.model

import jakarta.persistence.*

@Entity
@Table(name = "match_questions")
data class MatchQuestion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "match_id")
    val matchId: Long?,

    @Column(name = "question_id")
    val questionId: Long,

    @Column(name = "question_order")
    val questionOrder: Int
)