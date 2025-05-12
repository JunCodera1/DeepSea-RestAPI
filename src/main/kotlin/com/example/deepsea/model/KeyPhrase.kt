package com.example.deepsea.model

import jakarta.persistence.*

@Entity
@Table(name = "key_phrases")
data class KeyPhrase(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "unit_id")
    val unitId: Long = 0,

    @Column(name = "original_text", nullable = false)
    val originalText: String = "",

    @Column(name = "translated_text", nullable = false)
    val translatedText: String = "",

    @Column(name = "audio_url")
    val audioUrl: String? = null,

    @Column(name = "order_index")
    val orderIndex: Int = 0,

    @ManyToOne
    @JoinColumn(name = "unit_id", insertable = false, updatable = false)
    val unit: Unit? = null
)
 {
    fun toDto(): com.example.deepsea.dto.KeyPhraseDto {
        return com.example.deepsea.dto.KeyPhraseDto(
            id = this.id,
            unitId = this.unitId,
            originalText = this.originalText,
            translatedText = this.translatedText,
            audioUrl = this.audioUrl,
            orderIndex = this.orderIndex
        )
    }
}