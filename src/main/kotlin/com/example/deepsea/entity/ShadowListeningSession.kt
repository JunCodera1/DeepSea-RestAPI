package com.example.deepsea.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.Date

@Entity
@Table(name = "shadow_listening_sessions")
data class ShadowListeningSession(
    @Id
    @NotBlank
    @Size(min = 1, max = 50)
    @Column(name = "id", nullable = false, length = 50)
    var id: String = "",

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(name = "title", nullable = false, length = 100)
    var title: String = "",

    @NotBlank
    @Size(min = 1, max = 20)
    @Column(name = "difficulty", nullable = false, length = 20)
    var difficulty: String = "",

    @NotBlank
    @Size(min = 1, max = 10)
    @Column(name = "duration", nullable = false, length = 10)
    var duration: String = "",

    @NotBlank
    @Size(min = 1, max = 1000)
    @Column(name = "transcript", nullable = false, length = 1000)
    var transcript: String = "",

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(
        name = "session_key_phrases",
        joinColumns = [JoinColumn(name = "session_id")]
    )
    @Column(name = "key_phrase", nullable = false, length = 100)
    var keyPhrases: List<String> = listOf(),

    @NotBlank
    @Size(min = 1, max = 50)
    @Column(name = "language", nullable = false, length = 50)
    var language: String = "Japanese",

    @Size(max = 500)
    @Column(name = "description", length = 500)
    var description: String? = null,

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Date = Date(),

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Date = Date()
) {
    @PrePersist
    fun onCreate() {
        updatedAt = createdAt
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = Date()
    }
}