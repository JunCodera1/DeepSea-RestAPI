package com.example.deepsea.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*


@Entity
class PasswordResetToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val token: String = UUID.randomUUID().toString(),

    val verificationCode: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    val expiryDate: LocalDateTime = LocalDateTime.now().plusHours(1),

    var used: Boolean = false
)