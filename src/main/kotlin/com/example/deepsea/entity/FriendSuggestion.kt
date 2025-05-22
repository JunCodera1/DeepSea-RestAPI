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
    val userProfile: UserProfile,

    @ManyToOne
    @JoinColumn(name = "suggested_profile_id")
    val suggestedProfile: UserProfile,

    // Similarilty score for ranking suggestions (higher is better match)
    val similarityScore: Double = 0.0,

    // Track if the suggestion was ignored/dismissed
    var isDismissed: Boolean = false,

    // Track if the user followed the suggestion
    var isFollowed: Boolean = false,

    // Reason for suggestion - useful for showing the user why this person is suggested
    val suggestionReason: String = ""
)