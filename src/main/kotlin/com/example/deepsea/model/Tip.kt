package com.example.deepsea.model

import jakarta.persistence.*

@Entity
@Table(name = "tips")
data class Tip(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "unit_id")
    val unitId: Long = 0,

    @Column(nullable = false)
    val title: String = "",

    @Column(nullable = false, length = 1000)
    val content: String = "",

    @Column(name = "order_index")
    val orderIndex: Int = 0,

    @ManyToOne
    @JoinColumn(name = "unit_id", insertable = false, updatable = false)
    val unit: Unit? = null
)
 {
    fun toDto(): com.example.deepsea.dto.TipDto {
        return com.example.deepsea.dto.TipDto(
            id = this.id,
            title = this.title,
            content = this.content,
            orderIndex = this.orderIndex
        )
    }
}