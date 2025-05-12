package com.example.deepsea.model

import com.example.deepsea.dto.SectionDto
import jakarta.persistence.*

@Entity
@Table(name = "sections")
data class Section(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val title: String = "",
    val description: String = "",
    val color: String = "",
    val darkerColor: String = "",
    val image: String = "",
    val level: String = "",

    @Column(name = "order_index")
    val orderIndex: Int = 0,
    @OneToMany
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    val units: List<Unit> = emptyList()
)


fun Section.toDto(unitCount: Int = 0): SectionDto =
    SectionDto(
        id = this.id,
        title = this.title,
        description = this.description,
        color = this.color,
        darkerColor = this.darkerColor,
        image = this.image,
        level = this.level,
        orderIndex = this.orderIndex,
        unitCount = unitCount
    )