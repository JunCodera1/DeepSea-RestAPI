package com.example.deepsea.model

import com.example.deepsea.dto.Language
import com.example.deepsea.dto.SurveyOption
import com.example.deepsea.util.toFormattedJoinDate
import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity
@Table(name = "user_profiles")
data class UserProfile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String = "",
    val username: String = "",
    val joinDate: String = LocalDateTime.now().toFormattedJoinDate(),

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_courses", joinColumns = [JoinColumn(name = "user_profile_id")])
    @Column(name = "courses")
    val courses: Set<Language> = emptySet(),

    @ElementCollection(targetClass = SurveyOption::class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_survey_selections", joinColumns = [JoinColumn(name = "user_profile_id")])
    @Column(name = "survey")
    var selectedSurveys: Set<SurveyOption> = emptySet(),

    var followers: Int = 0,
    var following: Int = 0,
    var dayStreak: Int = 0,
    var totalXp: Int = 0,
    var currentLeague: String = "",
    var topFinishes: Int = 0,

    @ManyToMany
    @JoinTable(
        name = "user_friends",
        joinColumns = [JoinColumn(name = "user_profile_id")],
        inverseJoinColumns = [JoinColumn(name = "friend_id")]
    )
    var friends: Set<UserProfile> = setOf(),

    @OneToOne
    @JoinColumn(name = "user_id")
    @com.fasterxml.jackson.annotation.JsonBackReference
    val user: User? = null

)
