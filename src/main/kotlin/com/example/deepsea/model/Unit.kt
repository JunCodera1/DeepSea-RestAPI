package com.example.deepsea.model

import com.example.deepsea.dto.UnitDto
import jakarta.persistence.*

@Entity
@Table(name = "units")
data class Unit(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "section_id")
    val sectionId: Long,

    val title: String,
    val description: String,
    val color: String,
    val darkerColor: String,
    val image: String,

    @Column(name = "order_index")
    val orderIndex: Int,

    @Column(name = "stars_required")
    val starsRequired: Int = 0
)

fun Unit.toDto(lessonCount: Int = 0): UnitDto =
    UnitDto(
        id = this.id,
        sectionId = this.sectionId,
        title = this.title,
        description = this.description,
        color = this.color,
        darkerColor = this.darkerColor,
        image = this.image,
        orderIndex = this.orderIndex,
        starsRequired = this.starsRequired,
        lessonCount = lessonCount
    )