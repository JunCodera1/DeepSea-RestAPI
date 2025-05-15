package com.example.deepsea.mapper

import com.example.deepsea.dto.FriendInfoDto
import com.example.deepsea.dto.FriendSuggestionDto
import com.example.deepsea.dto.UserStatsDto
import com.example.deepsea.model.FriendSuggestion
import com.example.deepsea.model.UserProfile
import org.springframework.stereotype.Component

@Component
class FriendSuggestionMapper {
    /**
     * Convert FriendSuggestion entity to DTO
     */
    fun toDto(suggestion: FriendSuggestion): FriendSuggestionDto {
        val suggestedProfile = suggestion.suggestedProfile
        val userProfile = suggestion.userProfile

        // Find shared languages between users
        val sharedLanguages = userProfile.courses.intersect(suggestedProfile.courses).toList()

        return FriendSuggestionDto(
            id = suggestion.id,
            suggestedUserId = suggestedProfile.id,
            suggestedUserName = suggestedProfile.name,
            suggestedUsername = suggestedProfile.username,
            suggestedUserAvatarUrl = suggestedProfile.user?.avatarUrl,
            similarityScore = suggestion.similarityScore,
            suggestionReason = suggestion.suggestionReason,
            sharedLanguages = sharedLanguages,
            suggestedUserStats = FriendInfoDto(
                totalXp = suggestedProfile.totalXp,
                dayStreak = suggestedProfile.dayStreak,
                currentLeague = suggestedProfile.currentLeague,
            )
        )
    }
}