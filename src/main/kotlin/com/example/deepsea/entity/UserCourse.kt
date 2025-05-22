package com.example.deepsea.entity

import com.example.deepsea.enums.LanguageOption
import com.example.deepsea.enums.PathOption
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "user_courses")
data class UserCourse(
    @EmbeddedId
    val id: UserCourseId = UserCourseId(),

    @Enumerated(EnumType.STRING)
    @Column(name = "path")
    var path: PathOption? = PathOption.BEGINNER
)

@Embeddable
data class UserCourseId(
    @Column(name = "user_profile_id")
    val userProfileId: Long = 75,

    @Enumerated(EnumType.STRING)
    @Column(name = "courses")
    val course: LanguageOption = LanguageOption.FRENCH
) : Serializable