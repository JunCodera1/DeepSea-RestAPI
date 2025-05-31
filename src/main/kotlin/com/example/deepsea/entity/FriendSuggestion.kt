package com.example.deepsea.entity

import jakarta.persistence.*

@Entity
@Table(name = "friend_suggestions")
data class FriendSuggestion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    val userProfile: UserProfile = UserProfile(),

    @ManyToOne
    @JoinColumn(name = "suggested_profile_id")
    val suggestedProfile: UserProfile = UserProfile(),

    val similarityScore: Double = 0.0,
    var isDismissed: Boolean = false,
    var isFollowed: Boolean = false,
    val suggestionReason: String = ""
) {
    // Default constructor for JPA
    constructor() : this(
        0, UserProfile(), UserProfile(), 0.0, false, false, ""
    )
}
