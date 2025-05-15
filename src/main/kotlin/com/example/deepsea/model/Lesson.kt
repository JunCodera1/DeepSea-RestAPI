package com.example.deepsea.model

import com.example.deepsea.dto.LessonDto
import jakarta.persistence.*

@Entity
@Table(name = "lessons")
data class Lesson(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "unit_id")
    val unitId: Long = 0,

    val title: String = "",
    val description: String = "",

    @Column(name = "lesson_type")
    val lessonType: String = "",

    @Column(name = "order_index")
    val orderIndex: Int = 0,

    @Column(name = "xp_reward")
    val xpReward: Int = 10,

    @Column(name = "stars_reward")
    val starsReward: Int = 1,

    @Column(name = "difficulty_level")
    val difficultyLevel: String = "normal"
)


fun Lesson.toDto(): LessonDto =
    LessonDto(
        id = this.id,
        unitId = this.unitId,
        title = this.title,
        description = this.description,
        lessonType = this.lessonType,
        orderIndex = this.orderIndex,
        xpReward = this.xpReward,
        starsReward = this.starsReward,
        difficultyLevel = this.difficultyLevel
    )