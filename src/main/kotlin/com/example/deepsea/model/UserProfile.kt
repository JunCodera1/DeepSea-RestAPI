package com.example.deepsea.model

import com.example.deepsea.dto.Language
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
    @Column(name = "course")
    val courses: Set<Language> = setOf(),

    val followers: Int = 0,
    val following: Int = 0,
    val dayStreak: Int = 0,
    val totalXp: Int = 0,
    val currentLeague: String = "",
    val topFinishes: Int = 0,

    @ManyToMany
    @JoinTable(
        name = "user_friends",
        joinColumns = [JoinColumn(name = "user_profile_id")],
        inverseJoinColumns = [JoinColumn(name = "friend_id")]
    )
    val friends: Set<UserProfile> = setOf(),

    @OneToOne
    @JoinColumn(name = "user_id")
    val user: User? = null
)
