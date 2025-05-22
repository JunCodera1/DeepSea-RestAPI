package com.example.deepsea.entity

import com.example.deepsea.enums.DailyGoalOption
import com.example.deepsea.enums.LanguageOption
import com.example.deepsea.enums.SurveyOption
import com.example.deepsea.util.toFormattedJoinDate
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "user_profiles")
data class UserProfile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String = "",
    val username: String = "",
    val joinDate: String = LocalDateTime.now().toFormattedJoinDate(),

    @ElementCollection(targetClass = LanguageOption::class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_courses", joinColumns = [JoinColumn(name = "user_profile_id")])
    @Column(name = "courses")
    var courses: Set<LanguageOption> = emptySet(),

    @ElementCollection(targetClass = SurveyOption::class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_survey_selections", joinColumns = [JoinColumn(name = "user_profile_id")])
    @Column(name = "survey")
    var selectedSurveys: Set<SurveyOption> = emptySet(),

    @Enumerated(EnumType.STRING)
    @Column(name = "daily_goal")
    var dailyGoal: DailyGoalOption = DailyGoalOption.CASUAL,

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
    val user: User? = null,
    val streakHistory: List<LocalDate> = emptyList() // Add streak history
)
