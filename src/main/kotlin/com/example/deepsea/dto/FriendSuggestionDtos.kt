package com.example.deepsea.dto

import com.example.deepsea.domain.enums.LanguageOption

/**
 * DTO for friend suggestions
 */
data class FriendSuggestionDto(
    val id: Long,
    val suggestedUserId: Long,
    val suggestedUserName: String,
    val suggestedUsername: String,
    val suggestedUserAvatarUrl: String?,
    val similarityScore: Double,
    val suggestionReason: String,
    val sharedLanguages: List<LanguageOption> = emptyList(),
    val suggestedUserStats: FriendInfoDto
)

/**
 * Simple user stats DTO for friend suggestions
 */
data class FriendInfoDto(
    val totalXp: Int,
    val dayStreak: Int,
    val currentLeague: String
)

/**
 * Response wrapper for friend suggestions
 */
data class FriendSuggestionResponseDto(
    val suggestions: List<FriendSuggestionDto>,
    val count: Int
)

/**
 * DTO for following a suggestion
 */
data class FollowSuggestionRequestDto(
    val suggestionId: Long
)

/**
 * DTO for dismissing a suggestion
 */
data class DismissSuggestionRequestDto(
    val suggestionId: Long
)